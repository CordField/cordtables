import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { AutocompleteRequest, AutocompleteResponse, ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateCoalitionExRequest {
  token: string;
  coalition: {
    neo4j_id: string;
    director: number;
    name: string;
  };
}
class CreateCoalitionExResponse extends GenericResponse {
  coalition: CommonCoalition;
}

class CommonCoalitionListRequest {
  token: string;
}

class CommonCoalitionListResponse {
  error: ErrorType;
  coalitions: CommonCoalition[];
}

class CommonCoalitionUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class CommonCoalitionUpdateResponse {
  error: ErrorType;
  coalition: CommonCoalition | null = null;
}

class DeleteCoalitionExRequest {
  id: string;
  token: string;
}

class DeleteCoalitionExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'common-coalitions',
  styleUrl: 'common-coalitions.css',
  shadow: true,
})
export class CommonCoalitions {
  @State() coalitionsResponse: CommonCoalitionListResponse;

  newNeo4j_id: string;
  newDirector: number;
  newName: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonCoalitionUpdateRequest, CommonCoalitionUpdateResponse>('common/coalitions/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.coalitionsResponse = {
        error: ErrorType.NoError,
        coalitions: this.coalitionsResponse.coalitions.map(coalition => (coalition.id === id ? updateResponse.coalition : coalition)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteCoalitionExRequest, DeleteCoalitionExResponse>('common/coalitions/delete', {
      id,
      token: globals.globalStore.state.token,
    });
    if (deleteResponse.error === ErrorType.NoError) {
      this.getList();
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item deleted successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: deleteResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  async getList() {
    this.coalitionsResponse = await fetchAs<CommonCoalitionListRequest, CommonCoalitionListResponse>('common/coalitions/list', {
      token: globals.globalStore.state.token,
    });
    if (this.coalitionsResponse.error === ErrorType.NoError) {
      await this.updateForeignKeys();
    }
  }

  async updateForeignKeys() {
    for (const thread of this.coalitionsResponse.coalitions) {
      for (const column of this.columnData) {
        if (column.foreignKey !== null && column.foreignKey !== undefined) {
          const autocompleteData = await fetchAs<AutocompleteRequest, AutocompleteResponse>('admin/autocomplete', {
            token: globals.globalStore.state.token,
            searchColumnName: 'id',
            resultColumnName: column.foreignTableColumn,
            tableName: column.foreignKey.split('/').join('.').replace('-', '_'),
            searchKeyword: thread[column.field],
          });
          console.log(autocompleteData);
          if (autocompleteData.error === ErrorType.NoError) {
            this.coalitionsResponse.coalitions.map(thread2 => {
              if (thread.id === thread2.id) {
                thread2[column.field] = {
                  value: thread[column.field],
                  displayValue: autocompleteData.data,
                };
              }
              return thread2;
            });
          }
        }
      }
    }
  }
  coalitionNameChange(event) {
    this.newName = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateCoalitionExRequest, CreateCoalitionExResponse>('common/coalitions/create-read', {
      token: globals.globalStore.state.token,
      coalition: {
        neo4j_id: this.newNeo4j_id,
        director: this.newDirector,
        name: this.newName,
      },
    });

    if (createResponse.error === ErrorType.NoError) {
      globals.globalStore.state.editMode = false;
      this.getList();
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item inserted successfully', id: uuidv4(), type: 'success' });
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: createResponse.error, id: uuidv4(), type: 'error' });
    }
  };

  columnData: ColumnDescription[] = [
    {
      field: 'id',
      displayName: 'ID',
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'name',
      displayName: 'Name',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'created_at',
      displayName: 'Created At',
      width: 250,
      editable: false,
    },
    {
      field: 'created_by',
      displayName: 'Created By',
      width: 100,
      editable: false,
      foreignKey: 'admin/people',
      foreignTableColumn: 'public_first_name',
    },
    {
      field: 'modified_at',
      displayName: 'Last Modified',
      width: 250,
      editable: false,
    },
    {
      field: 'modified_by',
      displayName: 'Last Modified By',
      width: 100,
      editable: false,
      foreignKey: 'admin/people',
      foreignTableColumn: 'public_first_name',
    },
    {
      field: 'owning_person',
      displayName: 'Owning Person ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
      foreignKey: 'admin/people',
      foreignTableColumn: 'public_first_name',
    },
    {
      field: 'owning_group',
      displayName: 'Owning Group ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
      foreignKey: 'admin/groups',
      foreignTableColumn: 'name',
    },
  ];

  async componentWillLoad() {
    await this.getList();
    // await this.getFilesList();
  }

  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.coalitionsResponse && <cf-table rowData={this.coalitionsResponse.coalitions} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="field-region-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="field-region">Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="field-region-name" name="field-region-name" onInput={event => this.coalitionNameChange(event)} />
              </span>
            </div>

            <span class="form-thing">
              <input id="create-button" type="submit" value="Create" onClick={this.handleInsert} />
            </span>
          </form>
        )}
      </Host>
    );
  }
}

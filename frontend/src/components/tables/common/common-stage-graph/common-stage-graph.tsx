import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { AutocompleteRequest, AutocompleteResponse, ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateStageGraphExRequest {
  token: string;
  stageGraph: {
    from_stage: string;
    to_stage: string;
  };
}
class CreateStageGraphExResponse extends GenericResponse {
  stageGraph: CommonStageGraph;
}

class CommonStageGraphListRequest {
  token: string;
}

class CommonStageGraphListResponse {
  error: ErrorType;
  stageGraphs: CommonStageGraph[];
}

class CommonStageGraphUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class CommonStageGraphUpdateResponse {
  error: ErrorType;
  stageGraph: CommonStageGraph | null = null;
}

class DeleteStageGraphExRequest {
  id: string;
  token: string;
}

class DeleteStageGraphExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'common-stage-graph',
  styleUrl: 'common-stage-graph.css',
  shadow: true,
})
export class CommonStageGraphs {
  @State() stageGraphsResponse: CommonStageGraphListResponse;

  newFrom_stage: string;
  newTo_stage: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonStageGraphUpdateRequest, CommonStageGraphUpdateResponse>('common/stage-graph/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.stageGraphsResponse = {
        error: ErrorType.NoError,
        stageGraphs: this.stageGraphsResponse.stageGraphs.map(stageGraph => (stageGraph.id === id ? updateResponse.stageGraph : stageGraph)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteStageGraphExRequest, DeleteStageGraphExResponse>('common/stage-graph/delete', {
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
    this.stageGraphsResponse = await fetchAs<CommonStageGraphListRequest, CommonStageGraphListResponse>('common/stage-graph/list', {
      token: globals.globalStore.state.token,
    });
    if (this.stageGraphsResponse.error === ErrorType.NoError) {
      await this.updateForeignKeys();
    }
  }
  async updateForeignKeys() {
    for (const post of this.stageGraphsResponse.stageGraphs) {
      for (const column of this.columnData) {
        if (column.foreignKey !== null && column.foreignKey !== undefined) {
          const autocompleteData = await fetchAs<AutocompleteRequest, AutocompleteResponse>('admin/autocomplete', {
            token: globals.globalStore.state.token,
            searchColumnName: 'id',
            resultColumnName: column.foreignTableColumn,
            tableName: column.foreignKey.split('/').join('.').replace('-', '_'),
            searchKeyword: post[column.field],
          });
          console.log(autocompleteData);
          if (autocompleteData.error === ErrorType.NoError) {
            this.stageGraphsResponse.stageGraphs.map(post2 => {
              if (post.id === post2.id) {
                post2[column.field] = {
                  value: post[column.field],
                  displayValue: autocompleteData.data,
                };
              }
              return post2;
            });
          }
        }
      }
    }
  }

  from_stageChange(event) {
    this.newFrom_stage = event.target.value;
  }

  to_stageChange(event) {
    this.newTo_stage = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateStageGraphExRequest, CreateStageGraphExResponse>('common/stage-graph/create-read', {
      token: globals.globalStore.state.token,
      stageGraph: {
        from_stage: this.newFrom_stage,
        to_stage: this.newTo_stage,
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
      field: 'from_stage',
      displayName: 'From Stage',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
      foreignKey: 'common/stages',
      foreignTableColumn: 'title',
    },
    {
      field: 'to_stage',
      displayName: 'To Stage',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
      foreignKey: 'common/stages',
      foreignTableColumn: 'title',
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
        {this.stageGraphsResponse && <cf-table rowData={this.stageGraphsResponse.stageGraphs} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="from_stage-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="from_stage">From Stage</label>
              </span>
              <span class="form-thing">
                <input type="text" id="from_stage" name="from_stage" onInput={event => this.from_stageChange(event)} />
              </span>
            </div>

            <div id="to_stage-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="to_stage">To Stage</label>
              </span>
              <span class="form-thing">
                <input type="text" id="to_stage" name="to_stage" onInput={event => this.to_stageChange(event)} />
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

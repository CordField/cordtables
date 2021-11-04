import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateFieldZoneExRequest {
  token: string;
  fieldZone: {
    neo4j_id: string;
    director: number;
    name: string;
  };
}
class CreateFieldZoneExResponse extends GenericResponse {
  fieldZone: ScFieldZone;
}

class ScFieldZoneListRequest {
  token: string;
}

class ScFieldZoneListResponse {
  error: ErrorType;
  fieldZones: ScFieldZone[];
}


class ScFieldZoneUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class ScFieldZoneUpdateResponse {
  error: ErrorType;
  fieldZone: ScFieldZone | null = null;
}

class DeleteFieldZoneExRequest {
  id: number;
  token: string;
}

class DeleteFieldZoneExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'sc-field-zones',
  styleUrl: 'sc-field-zones.css',
  shadow: true,
})
export class ScFieldZones {

  @State() fieldZonesResponse: ScFieldZoneListResponse;

  newNeo4j_id: string;
  newDirector: number;
  newName: string;
  
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScFieldZoneUpdateRequest, ScFieldZoneUpdateResponse>('sc-field-zones/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.fieldZonesResponse = { error: ErrorType.NoError, fieldZones: this.fieldZonesResponse.fieldZones.map(fieldZone => (fieldZone.id === id ? updateResponse.fieldZone : fieldZone)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteFieldZoneExRequest, DeleteFieldZoneExResponse>('sc-field-zones/delete', {
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
    this.fieldZonesResponse = await fetchAs<ScFieldZoneListRequest, ScFieldZoneListResponse>('sc-field-zones/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  neo4jChange(event) {
    this.newNeo4j_id = event.target.value;
  }

  directorChange(event) {
    this.newDirector = event.target.value;
  }

  fieldZoneNameChange(event) {
    this.newName = event.target.value;
  }


  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateFieldZoneExRequest, CreateFieldZoneExResponse>('sc-field-zones/create-read', {
      token: globals.globalStore.state.token,
      fieldZone: {
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
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'neo4j_id',
      displayName: 'neo4j_id',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'director',
      displayName: 'Director',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'name',
      displayName: 'File Version Name',
      width: 200,
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
    },
    {
      field: 'owning_person',
      displayName: 'Owning Person ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'owning_group',
      displayName: 'Owning Group ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
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
        {this.fieldZonesResponse && <cf-table rowData={this.fieldZonesResponse.fieldZones} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="neo4j_id-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="neo4j_id">neo4j_id</label>
              </span>
              <span class="form-thing">
                <input type="text" id="neo4j_id" name="neo4j_id" onInput={event => this.neo4jChange(event)} />
              </span>
            </div>

            <div id="director-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="director">Director</label>
              </span>
              <span class="form-thing">
                <input type="number" id="director" name="director" onInput={event => this.directorChange(event)} />
              </span>
            </div>

            <div id="field-Zone-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="field-Zone">New Field Zone Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="field-Zone-name" name="field-Zone-name" onInput={event => this.fieldZoneNameChange(event)} />
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

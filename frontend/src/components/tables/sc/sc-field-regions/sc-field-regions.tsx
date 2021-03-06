import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateFieldRegionExRequest {
  token: string;
  fieldRegion: {
    // neo4j_id: string;
    field_zone: string;
    director: string;
    name: string;
  };
}
class CreateFieldRegionExResponse extends GenericResponse {
  fieldRegion: ScFieldRegion;
}

class ScFieldRegionListRequest {
  token: string;
}

class ScFieldRegionListResponse {
  error: ErrorType;
  fieldRegions: ScFieldRegion[];
}

class ScFieldRegionUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScFieldRegionUpdateResponse {
  error: ErrorType;
  fieldRegion: ScFieldRegion | null = null;
}

class DeleteFieldRegionExRequest {
  id: string;
  token: string;
}

class DeleteFieldRegionExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-field-regions-table',
  styleUrl: 'sc-field-regions.css',
  shadow: true,
})
export class ScFieldRegions {
  @State() fieldRegionsResponse: ScFieldRegionListResponse;

  // newNeo4j_id: string;
  newField_zone: string;
  newDirector: string;
  newName: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScFieldRegionUpdateRequest, ScFieldRegionUpdateResponse>('sc/field-regions/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.fieldRegionsResponse = {
        error: ErrorType.NoError,
        fieldRegions: this.fieldRegionsResponse.fieldRegions.map(fieldRegion => (fieldRegion.id === id ? updateResponse.fieldRegion : fieldRegion)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteFieldRegionExRequest, DeleteFieldRegionExResponse>('sc/field-regions/delete', {
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
    this.fieldRegionsResponse = await fetchAs<ScFieldRegionListRequest, ScFieldRegionListResponse>('sc/field-regions/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }

  // neo4jChange(event) {
  //   this.newNeo4j_id = event.target.value;
  // }

  field_zoneChange(event) {
    this.newField_zone = event.target.value;
  }

  directorChange(event) {
    this.newDirector = event.target.value;
  }

  fieldRegionNameChange(event) {
    this.newName = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateFieldRegionExRequest, CreateFieldRegionExResponse>('sc/field-regions/create-read', {
      token: globals.globalStore.state.token,
      fieldRegion: {
        // neo4j_id: this.newNeo4j_id,
        field_zone: this.newField_zone,
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
    // {
    //   field: 'neo4j_id',
    //   displayName: 'neo4j_id',
    //   width: 50,
    //   editable: false,
    //   deleteFn: this.handleDelete,
    // },

    {
      field: 'field_zone',
      displayName: 'Field Zone',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'director',
      displayName: 'Director',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'name',
      displayName: 'Name',
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
        {this.fieldRegionsResponse && <cf-table rowData={this.fieldRegionsResponse.fieldRegions} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            {/* <div id="neo4j_id-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="neo4j_id">neo4j_id</label>
              </span>
              <span class="form-thing">
                <input type="text" id="neo4j_id" name="neo4j_id" onInput={event => this.neo4jChange(event)} />
              </span>
            </div> */}

            <div id="field_zone-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="field_zone">Field Zone</label>
              </span>
              <span class="form-thing">
                <input type="text" id="field_zone" name="field_zone" onInput={event => this.field_zoneChange(event)} />
              </span>
            </div>

            <div id="director-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="director">Director</label>
              </span>
              <span class="form-thing">
                <input type="text" id="director" name="director" onInput={event => this.directorChange(event)} />
              </span>
            </div>

            <div id="field-region-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="field-region">New Field Region Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="field-region-name" name="field-region-name" onInput={event => this.fieldRegionNameChange(event)} />
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

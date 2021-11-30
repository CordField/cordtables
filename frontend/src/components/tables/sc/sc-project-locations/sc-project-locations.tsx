import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateProjectLocationExRequest {
  token: string;
  projectLocation: {
    active: boolean;
    change_to_plan: number;
    location: number;
    project: number;
  };
}
class CreateProjectLocationExResponse extends GenericResponse {
  projectLocation: ScProjectLocation;
}

class ScProjectLocationListRequest {
  token: string;
}

class ScProjectLocationListResponse {
  error: ErrorType;
  projectLocations: ScProjectLocation[];
}


class ScProjectLocationUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class ScProjectLocationUpdateResponse {
  error: ErrorType;
  projectLocation: ScProjectLocation | null = null;
}

class DeleteProjectLocationExRequest {
  id: number;
  token: string;
}

class DeleteProjectLocationExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'sc-project-locations',
  styleUrl: 'sc-project-locations.css',
  shadow: true,
})
export class ScProjectLocations {

  @State() projectLocationsResponse: ScProjectLocationListResponse;

  newActive: boolean;
  newChange_to_plan: number;
  newLocation: number;
  newProject: number;
  
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScProjectLocationUpdateRequest, ScProjectLocationUpdateResponse>('sc-project-locations/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.projectLocationsResponse = { error: ErrorType.NoError, projectLocations: this.projectLocationsResponse.projectLocations.map(projectLocation => (projectLocation.id === id ? updateResponse.projectLocation : projectLocation)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteProjectLocationExRequest, DeleteProjectLocationExResponse>('sc-project-locations/delete', {
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
    this.projectLocationsResponse = await fetchAs<ScProjectLocationListRequest, ScProjectLocationListResponse>('sc-project-locations/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  activeChange(event) {
    this.newActive = event.target.value;
  }

  change_to_planChange(event) {
    this.newChange_to_plan = event.target.value;
  }

  locationChange(event) {
    this.newLocation = event.target.value;
  }

  projectChange(event) {
    this.newProject = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateProjectLocationExRequest, CreateProjectLocationExResponse>('sc-project-locations/create-read', {
      token: globals.globalStore.state.token,
      projectLocation: {
        active: this.newActive,
        change_to_plan: this.newChange_to_plan,
        location: this.newLocation,
        project: this.newProject,
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
      field: 'active',
      displayName: 'Active',
      width: 200,
      editable: true,
      selectOptions: [
        {display: "True", value: "true"},
        {display: "False", value: "false"},
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'change_to_plan',
      displayName: 'Change To Plan',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'location',
      displayName: 'Location',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'project',
      displayName: 'Project',
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
        {this.projectLocationsResponse && <cf-table rowData={this.projectLocationsResponse.projectLocations} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">

            <div id="active-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="active">Active</label>
              </span>
              <span class="form-thing">
                <select id="active" name="active" onInput={event => this.activeChange(event)}>
                  <option value="">Select Active</option>
                  <option value="true" selected={this.newActive === true}>True</option>
                   <option value="false" selected={this.newActive === false}>False</option>
                </select>
              </span>
            </div>  

            <div id="change_to_plan-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="change_to_plan">Change To Plan</label>
              </span>
              <span class="form-thing">
                <input type="number" id="change_to_plan" name="change_to_plan" onInput={event => this.change_to_planChange(event)} />
              </span>
            </div>        

            <div id="location-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="location">Location</label>
              </span>
              <span class="form-thing">
                <input type="number" id="location" name="location" onInput={event => this.locationChange(event)} />
              </span>
            </div>

            <div id="project-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="project">Project</label>
              </span>
              <span class="form-thing">
                <input type="number" id="project" name="project" onInput={event => this.projectChange(event)} />
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
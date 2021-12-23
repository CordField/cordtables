import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreatePinnedProjectExRequest {
  token: string;
  pinnedProject: {
    person: number;
    project: number;
  };
}
class CreatePinnedProjectExResponse extends GenericResponse {
  pinnedProject: ScPinnedProject;
}

class ScPinnedProjectListRequest {
  token: string;
}

class ScPinnedProjectListResponse {
  error: ErrorType;
  pinnedProjects: ScPinnedProject[];
}


class ScPinnedProjectUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScPinnedProjectUpdateResponse {
  error: ErrorType;
  pinnedProject: ScPinnedProject | null = null;
}

class DeletePinnedProjectExRequest {
  id: string;
  token: string;
}

class DeletePinnedProjectExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-pinned-projects',
  styleUrl: 'sc-pinned-projects.css',
  shadow: true,
})
export class ScPinnedProjects {

  @State() pinnedProjectsResponse: ScPinnedProjectListResponse;

  newPerson: number;
  newProject: number;
  
  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScPinnedProjectUpdateRequest, ScPinnedProjectUpdateResponse>('sc-pinned-projects/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.pinnedProjectsResponse = { error: ErrorType.NoError, pinnedProjects: this.pinnedProjectsResponse.pinnedProjects.map(pinnedProject => (pinnedProject.id === id ? updateResponse.pinnedProject : pinnedProject)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeletePinnedProjectExRequest, DeletePinnedProjectExResponse>('sc-pinned-projects/delete', {
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
    this.pinnedProjectsResponse = await fetchAs<ScPinnedProjectListRequest, ScPinnedProjectListResponse>('sc-pinned-projects/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  projectChange(event) {
    this.newProject = event.target.value;
  }

  personChange(event) {
    this.newPerson = event.target.value;
  }


  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreatePinnedProjectExRequest, CreatePinnedProjectExResponse>('sc-pinned-projects/create-read', {
      token: globals.globalStore.state.token,
      pinnedProject: {
        person: this.newPerson,
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
      field: 'person',
      displayName: 'Person',
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
        {this.pinnedProjectsResponse && <cf-table rowData={this.pinnedProjectsResponse.pinnedProjects} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">

            <div id="person-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="person">Person</label>
              </span>
              <span class="form-thing">
                <input type="text" id="person" name="person" onInput={event => this.personChange(event)} />
              </span>
            </div> 

            <div id="project-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="project">Project</label>
              </span>
              <span class="form-thing">
                <input type="text" id="project" name="project" onInput={event => this.projectChange(event)} />
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
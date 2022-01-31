import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateProjectMemberExRequest {
  token: string;
  projectMember: {
    project: string;
    person: string;
    group_id: string;
    role: string;
    sensitivity: string;

  };
}
class CreateProjectMemberExResponse extends GenericResponse {
  projectMember: ScProjectMember;
}

class ScProjectMemberListRequest {
  token: string;
}

class ScProjectMemberListResponse {
  error: ErrorType;
  projectMembers: ScProjectMember[];
}

class ScProjectMemberUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScProjectMemberUpdateResponse {
  error: ErrorType;
  projectMember: ScProjectMember | null = null;
}

class DeleteProjectMemberExRequest {
  id: string;
  token: string;
}

class DeleteProjectMemberExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-project-members',
  styleUrl: 'sc-project-members.css',
  shadow: true,
})
export class ScProjectMembers {
  @State() projectMembersResponse: ScProjectMemberListResponse;

  newProject: string;
  newPerson: string;
  newGroup_id: string;
  newRole: string;
  newSensitivity: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScProjectMemberUpdateRequest, ScProjectMemberUpdateResponse>('sc/project-members/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.projectMembersResponse = {
        error: ErrorType.NoError,
        projectMembers: this.projectMembersResponse.projectMembers.map(projectMember => (projectMember.id === id ? updateResponse.projectMember : projectMember)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteProjectMemberExRequest, DeleteProjectMemberExResponse>('sc/project-members/delete', {
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
    this.projectMembersResponse = await fetchAs<ScProjectMemberListRequest, ScProjectMemberListResponse>('sc/project-members/list', {
      token: globals.globalStore.state.token,
    });
  }

  projectChange(event) {
    this.newProject = event.target.value;
  }

  personChange(event) {
    this.newPerson = event.target.value;
  }

  group_idChange(event) {
    this.newGroup_id = event.target.value;
  }

  roleChange(event) {
    this.newRole = event.target.value;
  }

  sensitivityChange(event) {
    this.newSensitivity = event.target.value;
  }
  

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateProjectMemberExRequest, CreateProjectMemberExResponse>('sc/project-members/create-read', {
      token: globals.globalStore.state.token,
      projectMember: {
        project: this.newProject,
        group_id: this.newGroup_id,
        person: this.newPerson,
        role: this.newRole,
        sensitivity: this.newSensitivity
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
      field: 'project',
      displayName: 'Project',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },

    {
      field: 'person',
      displayName: 'person',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'group_id',
      displayName: 'Group ID',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },


    {
      field: 'role',
      displayName: 'Role',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'sensitivity',
      displayName: 'Sensitivity',
      width: 250,
      editable: true,
      selectOptions: [
        { display: 'Low', value: 'Low' },
        { display: 'Medium', value: 'Medium' },
        { display: 'High', value: 'High' },
      ],
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
        {this.projectMembersResponse && <cf-table rowData={this.projectMembersResponse.projectMembers} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">

            <div id="project-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="project">Project</label>
              </span>
              <span class="form-thing">
                <input type="text" id="project" name="project" onInput={event => this.projectChange(event)} />
              </span>
            </div>
            

            <div id="person-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="person">Person</label>
              </span>
              <span class="form-thing">
                <input type="text" id="person" name="person" onInput={event => this.personChange(event)} />
              </span>
            </div>

            <div id="group_id-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="group_id">Group ID</label>
              </span>
              <span class="form-thing">
                <input type="text" id="director" name="director" onInput={event => this.group_idChange(event)} />
              </span>
            </div>

            <div id="role-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="role">Role</label>
              </span>
              <span class="form-thing">
                <input type="text" id="role" name="role" onInput={event => this.roleChange(event)} />
              </span>
            </div>


            <div id="sensitivity-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="sensitivity">Sensitivity</label>
              </span>
              <span class="form-thing">
                <select id="sensitivity" name="sensitivity" onInput={event => this.sensitivityChange(event)}>
                  <option value="">Select Sensitivity</option>
                  <option value="Low" selected={this.newSensitivity === 'Low'}>
                    Low
                  </option>
                  <option value="Medium" selected={this.newSensitivity === 'Medium'}>
                    Medium
                  </option>
                  <option value="High" selected={this.newSensitivity === 'High'}>
                    High
                  </option>
                </select>
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

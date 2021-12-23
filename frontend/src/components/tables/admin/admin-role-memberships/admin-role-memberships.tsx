import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateRoleMembershipExRequest {
  token: string;
  roleMembership: {
    role: number;
    person: number;
  };
}
class CreateRoleMembershipExResponse extends GenericResponse {
  roleMembership: AdminRoleMembership;
}

class AdminRoleMembershipListRequest {
  token: string;
}

class AdminRoleMembershipListResponse {
  error: ErrorType;
  roleMemberships: AdminRoleMembership[];
}

class AdminRoleMembershipUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class AdminRoleMembershipUpdateResponse {
  error: ErrorType;
  roleMembership: AdminRoleMembership | null = null;
}

class DeleteRoleMembershipExRequest {
  id: string;
  token: string;
}

class DeleteRoleMembershipExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'admin-role-memberships',
  styleUrl: 'admin-role-memberships.css',
  shadow: true,
})
export class AdminRoleMemberships {
  @State() roleMembershipsResponse: AdminRoleMembershipListResponse;

  newRole: number;
  newPerson: number;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<AdminRoleMembershipUpdateRequest, AdminRoleMembershipUpdateResponse>('admin/role-memberships/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.roleMembershipsResponse = {
        error: ErrorType.NoError,
        roleMemberships: this.roleMembershipsResponse.roleMemberships.map(roleMembership => (roleMembership.id === id ? updateResponse.roleMembership : roleMembership)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteRoleMembershipExRequest, DeleteRoleMembershipExResponse>('admin/role-memberships/delete', {
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
    this.roleMembershipsResponse = await fetchAs<AdminRoleMembershipListRequest, AdminRoleMembershipListResponse>('admin/role-memberships/list', {
      token: globals.globalStore.state.token,
    });
  }

  roleChange(event) {
    this.newRole = event.target.value;
  }

  personChange(event) {
    this.newPerson = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateRoleMembershipExRequest, CreateRoleMembershipExResponse>('admin/role-memberships/create-read', {
      token: globals.globalStore.state.token,
      roleMembership: {
        role: this.newRole,
        person: this.newPerson,
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
      field: 'role',
      displayName: 'Role',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'person',
      displayName: 'Person',
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
        {this.roleMembershipsResponse && <cf-table rowData={this.roleMembershipsResponse.roleMemberships} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="role-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="role">Role</label>
              </span>
              <span class="form-thing">
                <input type="text" id="role" name="role" onInput={event => this.roleChange(event)} />
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

            <span class="form-thing">
              <input id="create-button" type="submit" value="Create" onClick={this.handleInsert} />
            </span>
          </form>
        )}
      </Host>
    );
  }
}

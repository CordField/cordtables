import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateRoleTablePermissionExRequest {
  token: string;
  roleTablePermission: {
    role: number;
    table_name: string;
    table_permission: string;
  };
}
class CreateRoleTablePermissionExResponse extends GenericResponse {
  roleTablePermission: AdminRoleTablePermission;
}

class AdminRoleTablePermissionListRequest {
  token: string;
}

class AdminRoleTablePermissionListResponse {
  error: ErrorType;
  roleTablePermissions: AdminRoleTablePermission[];
}


class AdminRoleTablePermissionUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class AdminRoleTablePermissionUpdateResponse {
  error: ErrorType;
  roleTablePermission: AdminRoleTablePermission | null = null;
}

class DeleteRoleTablePermissionExRequest {
  id: number;
  token: string;
}

class DeleteRoleTablePermissionExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'admin-role-table-permissions',
  styleUrl: 'admin-role-table-permissions.css',
  shadow: true,
})
export class AdminRoleTablePermissions {

  @State() roleTablePermissionsResponse: AdminRoleTablePermissionListResponse;

  newRole: number;
  newTable_name: string;
  newTable_permission: string;
  
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<AdminRoleTablePermissionUpdateRequest, AdminRoleTablePermissionUpdateResponse>('admin-role-table-permissions/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.roleTablePermissionsResponse = { error: ErrorType.NoError, roleTablePermissions: this.roleTablePermissionsResponse.roleTablePermissions.map(roleTablePermission => (roleTablePermission.id === id ? updateResponse.roleTablePermission : roleTablePermission)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteRoleTablePermissionExRequest, DeleteRoleTablePermissionExResponse>('admin-role-table-permissions/delete', {
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
    this.roleTablePermissionsResponse = await fetchAs<AdminRoleTablePermissionListRequest, AdminRoleTablePermissionListResponse>('admin-role-table-permissions/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  roleChange(event) {
    this.newRole = event.target.value;
  }

  table_nameChange(event) {
    this.newTable_name = event.target.value;
  }

  table_permissionChange(event) {
    this.newTable_permission = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateRoleTablePermissionExRequest, CreateRoleTablePermissionExResponse>('admin-role-table-permissions/create-read', {
      token: globals.globalStore.state.token,
      roleTablePermission: {
        role: this.newRole,
        table_name: this.newTable_name,
        table_permission: this.newTable_permission,
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
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'table_name',
      displayName: 'Table Name',
      width: 200,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'table_permission',
      displayName: 'Table Permission',
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
        {this.roleTablePermissionsResponse && <cf-table rowData={this.roleTablePermissionsResponse.roleTablePermissions} columnData={this.columnData}></cf-table>}

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

            <div id="table_name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="table_name">Table Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="table_name" name="table_name" onInput={event => this.table_nameChange(event)} />
              </span>
            </div>

            <div id="table_permission-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="table_permission">Table Permission</label>
              </span>
              <span class="form-thing">
                <input type="text" id="table_permission" name="table_permission" onInput={event => this.table_permissionChange(event)} />
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
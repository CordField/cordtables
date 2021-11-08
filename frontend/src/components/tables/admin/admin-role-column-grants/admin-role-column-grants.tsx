import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateRoleColumnGrantExRequest {
  token: string;
  roleColumnGrant: {
    role: number;
    table_name: string;
    column_name: string;
    access_level: string;
  };
}
class CreateRoleColumnGrantExResponse extends GenericResponse {
  roleColumnGrant: AdminRoleColumnGrant;
}

class AdminRoleColumnGrantListRequest {
  token: string;
}

class AdminRoleColumnGrantListResponse {
  error: ErrorType;
  roleColumnGrants: AdminRoleColumnGrant[];
}


class AdminRoleColumnGrantUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class AdminRoleColumnGrantUpdateResponse {
  error: ErrorType;
  roleColumnGrant: AdminRoleColumnGrant | null = null;
}

class DeleteRoleColumnGrantExRequest {
  id: number;
  token: string;
}

class DeleteRoleColumnGrantExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'admin-role-column-grants',
  styleUrl: 'admin-role-column-grants.css',
  shadow: true,
})
export class AdminRoleColumnGrants {

  @State() roleColumnGrantsResponse: AdminRoleColumnGrantListResponse;

  newRole: number;
  newTable_name: string;
  newColumn_name: string;
  newAccess_level: string;
  
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<AdminRoleColumnGrantUpdateRequest, AdminRoleColumnGrantUpdateResponse>('admin-role-column-grants/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.roleColumnGrantsResponse = { error: ErrorType.NoError, roleColumnGrants: this.roleColumnGrantsResponse.roleColumnGrants.map(roleColumnGrant => (roleColumnGrant.id === id ? updateResponse.roleColumnGrant : roleColumnGrant)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteRoleColumnGrantExRequest, DeleteRoleColumnGrantExResponse>('admin-role-column-grants/delete', {
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
    this.roleColumnGrantsResponse = await fetchAs<AdminRoleColumnGrantListRequest, AdminRoleColumnGrantListResponse>('admin-role-column-grants/list', {
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

  column_nameChange(event) {
    this.newColumn_name = event.target.value;
  }

  access_levelChange(event) {
    this.newAccess_level = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateRoleColumnGrantExRequest, CreateRoleColumnGrantExResponse>('admin-role-column-grants/create-read', {
      token: globals.globalStore.state.token,
      roleColumnGrant: {
        role: this.newRole,
        table_name: this.newTable_name,
        column_name: this.newColumn_name,
        access_level: this.newAccess_level,
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
      field: 'column_name',
      displayName: 'Column Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'access_level',
      displayName: 'Access Level',
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
        {this.roleColumnGrantsResponse && <cf-table rowData={this.roleColumnGrantsResponse.roleColumnGrants} columnData={this.columnData}></cf-table>}

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

            <div id="column_name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="column_namen">Column Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="column_name" name="column_name" onInput={event => this.column_nameChange(event)} />
              </span>
            </div>        

            <div id="access_level-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="access_level">Access Level</label>
              </span>
              <span class="form-thing">
                <input type="text" id="access_level" name="access_level" onInput={event => this.access_levelChange(event)} />
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

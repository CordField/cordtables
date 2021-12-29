import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateUserExRequest {
  token: string;
  user: {
    person?: number;
    email?: string;
    password?: string;
  };
}
class CreateUserExResponse extends GenericResponse {
  user: AdminUser;
}

class AdminUserListRequest {
  token: string;
}

class AdminUserListResponse {
  error: ErrorType;
  users: AdminUser[];
}

class AdminUserUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class AdminUserUpdateResponse {
  error: ErrorType;
  user: AdminUser | null = null;
}

class DeleteUserExRequest {
  id: number;
  token: string;
}

class DeleteUserExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'admin-users',
  styleUrl: 'admin-users.css',
  shadow: true,
})
export class AdminUsers {
  @State() usersResponse: AdminUserListResponse;

  newPerson: number;
  newEmail?: string;
  newPassword?: string;

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<AdminUserUpdateRequest, AdminUserUpdateResponse>('admin/users/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.usersResponse = { error: ErrorType.NoError, users: this.usersResponse.users.map(user => (user.id === id ? updateResponse.user : user)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteUserExRequest, DeleteUserExResponse>('admin/users/delete', {
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
    this.usersResponse = await fetchAs<AdminUserListRequest, AdminUserListResponse>('admin/users/list', {
      token: globals.globalStore.state.token,
    });
  }

  personChange(event) {
    this.newPerson = event.target.value;
  }

  emailChange(event) {
    this.newEmail = event.target.value;
  }

  passwordChange(event) {
    this.newPassword = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateUserExRequest, CreateUserExResponse>('admin/users/create-read', {
      token: globals.globalStore.state.token,
      user: {
        person: this.newPerson,
        email: this.newEmail,
        password: this.newPassword,
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
      editable: false,
      updateFn: this.handleUpdate,
    },
    {
      field: 'email',
      displayName: 'Email',
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
        {this.usersResponse && <cf-table rowData={this.usersResponse.users} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="person-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="person">Person</label>
              </span>
              <span class="form-thing">
                <input type="number" id="person" name="person" onInput={event => this.personChange(event)} />
              </span>
            </div>

            <div id="email-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="email">Email</label>
              </span>
              <span class="form-thing">
                <input type="text" id="email" name="email" onInput={event => this.emailChange(event)} />
              </span>
            </div>

            <div id="password-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="password">Password</label>
              </span>
              <span class="form-thing">
                <input type="password" id="password" name="password" onInput={event => this.passwordChange(event)} />
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

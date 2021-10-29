import { Component, Host, h, State } from '@stencil/core';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { AdminUser } from '../../../../common/types';
import './admin-users.css';

type MutableAdminUserFields = Omit<AdminUser, 'id' | 'createdAt' | 'createdBy' | 'modifiedAt' | 'modifiedBy'>;

class CreateAdminUserRequest {
  insertedFields: MutableAdminUserFields;
  token: string;
}
class CreateAdminUserResponse extends GenericResponse {
  data: AdminUser;
}

class UpdateAdminUserRequest {
  token: string;
  columnToUpdate: string;
  updatedColumnValue: string | number;
  id: number;
}

class UpdateAdminUserResponse extends GenericResponse {
  data: AdminUser;
}

class DeleteAdminUserRequest {
  id: number;
  token: string;
}

class DeleteAdminUserResponse extends GenericResponse {
  id: number;
}

class ListAdminUserResponse extends GenericResponse {
  data: AdminUser[];
}

class ListAdminUserRequest {
  token: string;
}

@Component({
  tag: 'admin-users',
  styleUrl: 'admin-users.css',
  shadow: true,
})
export class AdminUsers {
  defaultFields = {
    id: null,
    person: null,
    email: null,
    password: null,
    chat: null,
    created_at: null,
    created_by: null,
    modified_at: null,
    modified_by: null,
    owning_person: null,
    owning_group: null,
    peer: null,
  };
  nonEditableColumns = ['id', 'modified_at', 'created_at', 'created_by', 'modified_by'];
  @State() adminUsers: AdminUser[] = [];
  @State() insertedFields: MutableAdminUserFields = this.defaultFields;
  @State() error: string;
  @State() success: string;
  @State() showNewForm = false;
  insertFieldChange(event, fieldName) {
    this.insertedFields[fieldName] = event.target.value;
  }
  getInputCell(fieldName) {
    if (this.nonEditableColumns.includes(fieldName)) {
      return <td>&nbsp;</td>;
    }
    return (
      <td>
        <input type="text" id={`input-${fieldName}`} name={fieldName} onInput={event => this.insertFieldChange(event, fieldName)}></input>
      </td>
    );
  }
  getEditableCell(columnName: string, adminUser: AdminUser) {
    return (
      <td>
        <cf-cell
          key={columnName}
          rowId={adminUser.id}
          propKey={columnName}
          value={typeof adminUser[columnName] === 'string' ? adminUser[columnName] : adminUser[columnName]?.toString()}
          isEditable={!this.nonEditableColumns.includes(columnName)}
          updateFn={!this.nonEditableColumns.includes(columnName) ? this.handleUpdate : null}
        />
      </td>
    );
  }

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<UpdateAdminUserRequest, UpdateAdminUserResponse>('table/admin-users/update', {
      token: globals.globalStore.state.token,
      updatedColumnValue: value,
      columnToUpdate: columnName,
      id,
    });

    if (updateResponse.error == ErrorType.NoError) {
      const result = await fetchAs<ListAdminUserRequest, ListAdminUserResponse>('table/admin-users/list', { token: globals.globalStore.state.token });
      this.adminUsers = result.data.sort((a, b) => a.id - b.id);
      return true;
    } else {
      alert(updateResponse.error);
    }
  };

  handleDelete = async id => {
    const result = await fetchAs<DeleteAdminUserRequest, DeleteAdminUserResponse>('table/admin-users/delete', {
      id,
      token: globals.globalStore.state.token,
    });
    if (result.error === ErrorType.NoError) {
      this.success = `Row with id ${result.id} deleted successfully!`;
      this.adminUsers = this.adminUsers.filter(adminUser => adminUser.id !== result.id);
    } else {
      this.error = result.error;
    }
  };

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();
    const result = await fetchAs<CreateAdminUserRequest, CreateAdminUserResponse>('table/admin-users/create', {
      insertedFields: this.insertedFields,
      token: globals.globalStore.state.token,
    });

    this.showNewForm = false;
    this.insertedFields = this.defaultFields;
    if (result.error === ErrorType.NoError) {
      this.adminUsers = this.adminUsers.concat(result.data);
      this.success = `New Row with id ${result.data.id} inserted successfully`;
    } else {
      console.error('Failed to create admin user');
      this.error = result.error;
    }
  };

  componentWillLoad() {
    fetchAs<ListAdminUserRequest, ListAdminUserResponse>('table/admin-users/list', {
      token: globals.globalStore.state.token,
    }).then(res => {
      this.adminUsers = res.data.sort((a, b) => a.id - b.id);
    });
  }

  render() {
    return (
      <Host>
        <div>{this.error}</div>
        <header>
          <h1>Language Ex</h1>
        </header>

        <main>
          <div id="table-wrap">
            <table>
              <thead>
                <tr>
                  {globals.globalStore.state.editMode && <th>*</th>}
                  {Object.keys(this.defaultFields).map(key => (
                    <th>{key.replaceAll('_', ' ')}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {this.adminUsers &&
                  this.adminUsers.length > 0 &&
                  this.adminUsers.map(adminUser => (
                    <tr>
                      {globals.globalStore.state.editMode && (
                        <div class="button-parent">
                          <button class="delete-button" onClick={() => this.handleDelete(adminUser.id)}>
                            Delete
                          </button>
                        </div>
                      )}
                      {Object.keys(adminUser).map(key => this.getEditableCell(key, adminUser))}
                    </tr>
                  ))}
              </tbody>
              {this.showNewForm && (
                <tr>
                  {globals.globalStore.state.editMode && <td>&nbsp;</td>}
                  {Object.keys(this.defaultFields).map(key => this.getInputCell(key))}
                </tr>
              )}
            </table>
          </div>
          <div id="button-group">
            {!this.showNewForm && (
              <button
                id="new-button"
                onClick={() => {
                  this.showNewForm = !this.showNewForm;
                }}
              >
                Create New Admin User
              </button>
            )}

            {this.showNewForm && (
              <div>
                <button
                  id="cancel-button"
                  onClick={() => {
                    this.showNewForm = !this.showNewForm;
                  }}
                >
                  Cancel
                </button>
                <button id="submit-button" onClick={this.handleInsert}>
                  Submit
                </button>
              </div>
            )}
          </div>
        </main>
      </Host>
    );
  }
}

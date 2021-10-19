import { Component, Host, h, State } from '@stencil/core';
import { ErrorType, GenericResponse, GlobalRole } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';

type MutableGlobalRoleFields = Omit<GlobalRole, 'id' | 'createdBy' | 'modifiedBy' | 'modifiedAt' | 'createdAt'>;

class CreateGlobalRoleRequest {
  insertedFields: MutableGlobalRoleFields;
  token: string;
}

class CreateGlobalRoleResponse extends GenericResponse {
  data: GlobalRole;
}

class UpdateGlobalRoleRequest {
  token: string;
  columnToUpdate: string;
  updatedColumnValue: string | number;
  id: number;
}

class UpdateGlobalRoleResponse extends GenericResponse {
  data: GlobalRole;
}

class DeleteGlobalRoleRequest {
  id: number;
  token: string;
}

class DeleteGlobalRoleResponse extends GenericResponse {
  id: number;
}

class ReadGlobalRoleRequest {
  token: string;
}

class ReadGlobalRoleResponse extends GenericResponse {
  data: GlobalRole[];
}

@Component({
  tag: 'global-roles',
  styleUrl: 'global-roles.css',
  shadow: true,
})
export class GlobalRoles {
  defaultFields = {
    id: null,
    created_at: null,
    created_by: null,
    modified_at: null,
    modified_by: null,
    name: null,
    owning_group: null,
    owning_person: null,
    chat: null,
  };
  nonEditableColumns = ['id', 'modified_at', 'created_at', 'created_by', 'modified_by'];
  @State() globalRoles: GlobalRole[] = [];
  @State() insertedFields: MutableGlobalRoleFields = this.defaultFields;
  @State() error: string;
  @State() success: string;
  @State() showNewForm = false;
  insertFieldChange(event, fieldName) {
    console.log(fieldName, event.target.value);
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
  getEditableCell(columnName: string, globalRole: GlobalRole) {
    return (
      <td>
        <cf-cell
          key={columnName}
          rowId={globalRole.id}
          propKey={columnName}
          value={globalRole[columnName]}
          isEditable={!this.nonEditableColumns.includes(columnName)}
          updateFn={!this.nonEditableColumns.includes(columnName) ? this.handleUpdate : null}
        ></cf-cell>
      </td>
    );
  }

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<UpdateGlobalRoleRequest, UpdateGlobalRoleResponse>('role/update', {
      token: globals.globalStore.state.token,
      updatedColumnValue: value,
      columnToUpdate: columnName,
      id,
    });

    if (updateResponse.error == ErrorType.NoError) {
      const result = await fetchAs<ReadGlobalRoleRequest, ReadGlobalRoleResponse>('role/read', { token: globals.globalStore.state.token });
      this.globalRoles = result.data.sort((a, b) => a.id - b.id);
      return true;
    } else {
      alert(updateResponse.error);
    }
  };

  handleDelete = async id => {
    const result = await fetchAs<DeleteGlobalRoleRequest, DeleteGlobalRoleResponse>('role/delete', {
      id,
      token: globals.globalStore.state.token,
    });
    if (result.error === ErrorType.NoError) {
      this.success = `Row with id ${result.id} deleted successfully!`;
      this.globalRoles = this.globalRoles.filter(globalRole => globalRole.id !== result.id);
    } else {
      this.error = result.error;
    }
  };

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();
    console.log(this.insertedFields);
    const result = await fetchAs<CreateGlobalRoleRequest, CreateGlobalRoleResponse>('role/create', {
      insertedFields: this.insertedFields,
      token: globals.globalStore.state.token,
    });

    console.log(result);
    this.showNewForm = false;
    this.insertedFields = this.defaultFields;
    if (result.error === ErrorType.NoError) {
      this.globalRoles = this.globalRoles.concat(result.data);
      this.success = `New Row with id ${result.data.id} inserted successfully`;
    } else {
      console.error('Failed to create global role');
      this.error = result.error;
    }
  };

  componentWillLoad() {
    fetchAs<ReadGlobalRoleRequest, ReadGlobalRoleResponse>('role/read', {
      token: globals.globalStore.state.token,
    }).then(res => {
      this.globalRoles = res.data.sort((a, b) => a.id - b.id);
    });
  }
  render() {
    return (
      <Host>
        <div>{this.error}</div>
        <header>
          <h1>Global Roles</h1>
        </header>
        <main>
          <div id="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>*</th>
                  {Object.keys(this.defaultFields).map(key => (
                    <th>{key}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {this.globalRoles.map(globalRole => (
                  <tr>
                    <div class="button-parent">
                      <button class="delete-button" onClick={() => this.handleDelete(globalRole.id)}>
                        Delete
                      </button>
                    </div>
                    {Object.keys(globalRole).map(key => this.getEditableCell(key, globalRole))}
                  </tr>
                ))}
              </tbody>
              {this.showNewForm && (
                <tr>
                  <td>&nbsp;</td>
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
                Create New Global Role
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

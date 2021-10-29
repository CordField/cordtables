import { Component, Host, h, State } from '@stencil/core';
import { ErrorType, GenericResponse, globalRoleTablePermissions } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';

type MutablePermissionsFields = Omit<globalRoleTablePermissions, 'id' | 'createdBy' | 'modifiedBy' | 'modifiedAt' | 'createdAt'>;
class CreateGlobalRoleTablePermissionsRequest {
  insertedFields: MutablePermissionsFields;
  email: string;
}

class DeletePermissionsResponse extends GenericResponse {
  data: { id: number };
}

class DeletePermissionsRequest {
  id: number;
}

class UpdatePermissionsRequest {
  table_name: string;
  role: number;
  table_permission: string;
  email: string;
  id: number;
}

class UpdatePermissionsResponse extends GenericResponse {
  globalRolesTablePermissions: globalRoleTablePermissions;
}
class ReadGlobalRoleTablePermissionsResponse extends GenericResponse {
  data: globalRoleTablePermissions[];
}

class CreateGlobalRoleTablePermissionsReponse extends GenericResponse {
  data: globalRoleTablePermissions[];
}
@Component({
  tag: 'role-table-permissions',
  styleUrl: 'global-role-table-permissions.css',
  shadow: true,
})
export class GlobalRoleTablePermissions {
  //@State()
  //globalRoleTablePermissions: globalRoleTablePermissions[] = [];
  defaultFields = { tableName: '', globalRole: null, tablePermission: '' };
  @State() globalRoleTablePermissions: globalRoleTablePermissions[] = [];
  @State() insertedFields: MutablePermissionsFields = this.defaultFields;
  @State() updatedFields: MutablePermissionsFields = this.defaultFields;
  @State() error: string;
  @State() success: string;

  insertFieldChange(event, fieldName) {
    this.insertedFields[fieldName] = event.target.value;
  }

  updateFieldChange(event, columnName) {
    this.updatedFields[columnName] = event.currentTarget.textContent;
  }

  handleUpdate = async id => {
    const result = await fetchAs<UpdatePermissionsRequest, UpdatePermissionsResponse>('globalrolestablepermissions/update', {
      table_name: this.updatedFields.tableName,
      role: this.updatedFields.globalRole,
      table_permission: this.updatedFields.tablePermission,
      email: globals.globalStore.state.email,
      id,
    });
    if (result.error === ErrorType.NoError) {
      this.updatedFields = this.defaultFields;

      this.globalRoleTablePermissions = this.globalRoleTablePermissions.map(globalRoleTablePermissions =>
        globalRoleTablePermissions.id === result.globalRolesTablePermissions.id ? result.globalRolesTablePermissions : globalRoleTablePermissions,
      );
      this.success = 'Row with id ${result.data.id} updated successfully!';
    } else {
      console.error('Failed to update row');
      this.error = result.error;
      this.globalRoleTablePermissions = this.globalRoleTablePermissions.map(globalRoleTablePermissions =>
        globalRoleTablePermissions.id === result.globalRolesTablePermissions.id ? result.globalRolesTablePermissions : globalRoleTablePermissions,
      );
    }
  };
  handleDelete = async id => {
    const result = await fetchAs<DeletePermissionsRequest, DeletePermissionsResponse>('globalrolestablepermissions/delete', {
      id,
    });
    if (result.error === ErrorType.NoError) {
      this.success = 'Row with id ${result.data.id} deleted successfully!';
      this.globalRoleTablePermissions = this.globalRoleTablePermissions.filter(globalRoleTablePermissions => globalRoleTablePermissions.id !== result.data.id);
    } else {
      this.error = result.error;
    }
  };
  handleSubmit = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();
    // return new row to avoid page refresh
    const result = await fetchAs<CreateGlobalRoleTablePermissionsRequest, CreateGlobalRoleTablePermissionsReponse>('globalrolestablepermissions/create', {
      insertedFields: this.insertedFields,
      email: globals.globalStore.state.email,
    });

    if (result.error === ErrorType.NoError) {
      this.insertedFields = this.defaultFields;
      this.globalRoleTablePermissions = this.globalRoleTablePermissions.concat(result.data);
      this.success = 'New Row with id ${result.data.id} submitted successfully';
    } else {
      console.error('failed to create global role table permission');
      this.error = result.error;
    }
  };
  componentWillLoad() {
    fetchAs<null, ReadGlobalRoleTablePermissionsResponse>('globalrolestablepermissions/read', null).then(res => {
      this.globalRoleTablePermissions = res.data;
    });
  }
  render() {
    return (
      <Host>
        <div>{this.error}</div>
        <header>
          <h1>Global Roles Table Permissions</h1>
        </header>
        {/*create and update form should be on the side*/}
        <main>
          <form class="form insert-form">
            <div class="form-row">
              <label htmlFor="tableName" class="label insert-form__label">
                tableName
              </label>
              <input type="text" value={this.insertedFields.tableName} onInput={event => this.insertFieldChange(event, 'tableName')} class="input insert-form__input" />
            </div>
            <div class="form form-row">
              <label htmlFor="tablePermission" class="label insert-form__label">
                tablePermission
              </label>
              <input type="text" value={this.insertedFields.tablePermission} onInput={event => this.insertFieldChange(event, 'tablePermission')} class="insert-form__input" />
            </div>
            <div class="form-row">
              <label htmlFor="globalRole" class="label insert-form__label">
                globalRole
              </label>
              <input type="number" value={this.insertedFields.globalRole} onInput={event => this.insertFieldChange(event, 'globalRole')} class="insert-form__input" />
            </div>

            <button onClick={this.handleSubmit}>Submit</button>
          </form>
          <table>
            <thead>
              {/* fixed on shared components, will be passed in and use Map to preserve order */}
              <tr>
                <th>id</th>
                <th>created_at</th>
                <th>created_by</th>
                <th>modified_at</th>
                <th>modified_by</th>
                <th>table_name</th>
                <th>table_permissions</th>
                <th>roles</th>
              </tr>
            </thead>
            <tbody>
              {this.globalRoleTablePermissions.map(globalRoleTablePermissions => {
                return (
                  <tr>
                    {/* use map to preserve order, loop over them*/}
                    <td>{globalRoleTablePermissions.id}</td>
                    <td>{globalRoleTablePermissions.createdAt}</td>
                    <td>{globalRoleTablePermissions.createdBy}</td>
                    <td>{globalRoleTablePermissions.modifiedAt}</td>
                    <td>{globalRoleTablePermissions.modifiedBy}</td>
                    <td contentEditable onInput={event => this.updateFieldChange(event, 'tableName')}>
                      {globalRoleTablePermissions.tableName}
                    </td>
                    <td contentEditable onInput={event => this.updateFieldChange(event, 'tablePermission')}>
                      {globalRoleTablePermissions.tablePermission}
                    </td>
                    <td contentEditable onInput={event => this.updateFieldChange(event, 'globalRole')}>
                      {globalRoleTablePermissions.globalRole}
                    </td>

                    <button onClick={() => this.handleUpdate(globalRoleTablePermissions.id)}>Update</button>
                    <button onClick={() => this.handleDelete(globalRoleTablePermissions.id)}>Delete</button>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </main>
      </Host>
    );
  }
}

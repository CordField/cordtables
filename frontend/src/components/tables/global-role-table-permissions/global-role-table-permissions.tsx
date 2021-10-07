import { Component, Host, h, State } from '@stencil/core';
import { ErrorType, GenericResponse, globalRoleTablePermissions } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store'

class CreateGlobalRoleTablePermissionsRequest {
  tableName: string;
  globalRole: number;
  tablePermissions: string;
  email: string;

}
class ReadGlobalRoleTablePermissionsResponse extends GenericResponse {
  data: globalRoleTablePermissions[];
}

class CreateGlobalRoleTablePermissionsReponse extends GenericResponse {
  data: globalRoleTablePermissions[];
}

@Component({
  tag: 'global-role-table-permissions',
  styleUrl: 'global-role-table-permissions.css',
  shadow: true,
})
export class GlobalRoleTablePermissions {
  //@State()
  //globalRoleTablePermissions: globalRoleTablePermissions[] = [];
  @State() globalRoleTablePermissions: globalRoleTablePermissions[] = [];
  @State() tableName: string;
  @State() tablePermissions: string;
  @State() globalRole: number;
  @State() error: string;
  @State() updatedGlobalRole: number;
  @State() updatedTableName: string;
  @State() idToUpdateOrDelete: number;
  @State() updatedTablePermissions: string;

  tableNameChange(event) {
    this.tableName = event.target.value;
  }
  updateTableNameChange(event) {
    this.updatedTableName = event.targett.value;
  }
  updateGlobalRoleChange(event) {
    this.updatedGlobalRole = event.target.value;
  }
  globalRolesChange(event) {
    this.globalRole = event.target.value;
  }
  updateTablePermissionsChange(event) {
    this.updatedTablePermissions = event.target.value;
  }
  tablePermissionsChange(event) {
    this.tablePermissions = event.target.value;
  }
  handleUpdate = async id => {
    const result = await fetchAs<UpdateGlobalRoleTablePermissionsRequest, UpdateGlobalRoleTablePermissionsResponse>('globalrolestablepermissions/update', {
      tableName: this.updatedTableName,
      tablePermission: this.updatedTablePermissions,
      globalRole: this.updateGlobalRoleChange,
      email: globals.globalStore.state.email,
    });
    };
    handleDelete = async id => {};
    handleSubmit = async (event: MouseEvent) => {
      event.preventDefault();
      event.stopPropagation();
    // return new row to avoid page refresh
      const result = await fetchAs<CreateGlobalRoleTablePermissionsRequest, CreateGlobalRoleTablePermissionsReponse>('globalrolestablepermissions/create', {
        tableName: this.tableName,
        tablePermissions: this.tablePermissions,
        globalRole: this.globalRole,
        email: globals.globalStore.state.email,

      });
    

      console.log(result);

      if (result.error === ErrorType.NoError) {
        this.tableName = '';
        this.tablePermissions = '';
        this.globalRole = null;
        this.globalRoleTablePermissions = this.globalRoleTablePermissions.concat(result.data);
      } else {
        console.error("failed to create global role table permission");
        this.error = result.error;
      }
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
          <form>
            <div id="tableName-holder" class="form-input-item">
              <div>
                <label htmlFor="tableName">TableName</label>
              </div>
              <input type="text" value={this.tableName} onInput={event => this.tableNameChange(event)} />
            </div>

            <div id="tablePermissions-holder" class="form-input-item">
              <div>
                <label htmlFor="tablePermissions">TablePermissions</label>
              </div>
              <input type="text" value={this.tablePermissions} onInput={event => this.tablePermissionsChange(event)} />
            </div>
            <div id="globalRoles-holder" class="form-input-item">
              <div>
                <label htmlFor="globalRoles">GlobalRoles</label>
              </div>
              <input type="number" value={this.globalRole} onInput={event => this.globalRolesChange(event)} />
            </div>
            <buttton id="Create-Buttton" value="Create" onClick={this.handleSubmit}>
              Submit
            </buttton>
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
                <th>global_roles</th>
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
                  <td contentEditable onChange={() => this.updateTableNameChange(globalRoleTablePermissions.id)}>
                    {globalRoleTablePermissions.tableName}
                  </td>
                  <td contentEditable onChange={() => this.updateTablePermissionsChange(globalRoleTablePermissions.id)}>
                    {globalRoleTablePermissions.tablePermission}
                  </td>
                  <td contentEditable onChange={() => this.updateGlobalRoleChange(globalRoleTablePermissions.id)}>
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

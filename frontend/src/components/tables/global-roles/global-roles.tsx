import { Component, Host, h, State } from '@stencil/core';
import { ErrorType, GenericResponse, globalRole } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';

class CreateGlobalRolesRequest {
  // tableName: string;
  name: string;
  org: number;
  email: string;
}
class ReadGlobalRolesResponse extends GenericResponse {
  data: globalRole[];
}

class CreateGlobalRolesResponse extends GenericResponse {
  data: globalRole;
}

@Component({
  tag: 'global-roles',
  styleUrl: 'global-roles.css',
  shadow: true,
})
export class GlobalRoles {
  @State() globalRoles: globalRole[] = [];
  @State() name: string;
  @State() org: number;
  @State() error: string;
  @State() updatedOrg: number;
  @State() updatedName: string;
  @State() idToUpdateOrDelete: number;

  nameChange(event) {
    this.name = event.target.value;
  }
  updateNameChange(event) {
    this.updatedName = event.target.value;
  }
  orgChange(event) {
    this.org = event.target.value;
  }
  updateOrgChange(event) {
    this.updatedOrg = event.target.value;
  }
  handleUpdate = async id => {
    const result = await fetchAs<UpdateGlobalRoleRequest, UpdateGlobalRoleResponse>('globalroles/update', {
      name: this.updatedName,
      org: this.updatedOrg,
      email: globals.globalStore.state.email,
    });
  };
  handleDelete = async id => {};
  handleSubmit = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();
    // can return the new row instead to avoid page refresh
    const result = await fetchAs<CreateGlobalRolesRequest, CreateGlobalRolesResponse>('globalroles/create', {
      name: this.name,
      org: this.org,
      email: globals.globalStore.state.email,
    });

    console.log(result);

    if (result.error === ErrorType.NoError) {
      this.name = '';
      this.org = null;
      this.globalRoles = this.globalRoles.concat(result.data);
    } else {
      console.error('Failed to create global role');
      this.error = result.error;
    }
  };

  componentWillLoad() {
    fetchAs<null, ReadGlobalRolesResponse>('globalroles/read', null).then(res => {
      // set global state with data
      // globals.globalStore.state.globalRolesData = JSON.stringify(res.globalRoles);
      // this.globalRoles = res.globalRoles;
      // some weird cors error
      this.globalRoles = res.data;
    });
  }
  render() {
    return (
      <Host>
        <div>{this.error}</div>
        <header>
          <h1>Global Roles</h1>
        </header>
        {/* add flexbox to main -> create and update form should be to the side */}
        <main>
          <form>
            <div id="name-holder" class="form-input-item">
              <div>
                <label htmlFor="name">Name</label>
              </div>
              <input type="text" value={this.name} onInput={event => this.nameChange(event)} />
            </div>

            <div id="org-holder" class="form-input-item">
              <div>
                <label htmlFor="org">Org</label>
              </div>
              <input type="number" value={this.org} onInput={event => this.orgChange(event)} />
            </div>
            <button id="Create-Button" value="Create" onClick={this.handleSubmit}>
              Submit
            </button>
          </form>
          <table>
            <thead>
              {/* this will be fixed -> on a shared component, this will be passed in and use Map to preserve order */}
              <tr>
                <th>id</th>
                <th>created_at</th>
                <th>created_by</th>
                <th>modified_at</th>
                <th>modified_by</th>
                <th>name</th>
                <th>org</th>
              </tr>
            </thead>
            <tbody>
              {this.globalRoles.map(globalRole => {
                return (
                  <tr>
                    {/* can loop over these as well (using Map to preserve order) */}
                    <td>{globalRole.id}</td>
                    <td>{globalRole.createdAt}</td>
                    <td>{globalRole.createdBy}</td>
                    <td>{globalRole.modifiedAt}</td>
                    <td>{globalRole.modifiedBy}</td>
                    <td contentEditable onChange={() => this.updateNameChange(globalRole.id)}>
                      {globalRole.name}
                    </td>
                    <td contentEditable onChange={() => this.updateOrgChange(globalRole.id)}>
                      {globalRole.org}
                    </td>
                    <button onClick={() => this.handleUpdate(globalRole.id)}>Update</button>
                    <button onClick={() => this.handleDelete(globalRole.id)}>Delete</button>
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

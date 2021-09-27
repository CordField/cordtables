import { Component, Host, h, State } from '@stencil/core';
import { ErrorType, GenericResponse, globalRole } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';

class GlobalRolesRequest {
  // tableName: string;
  name: string;
  org: number;
  email: string;
}
class GlobalRolesResponse extends GenericResponse {
  data: globalRole[];
}

@Component({
  tag: 'global-roles',
  styleUrl: 'global-roles.css',
  shadow: true,
})
export class GlobalRoles {
  @State() globalRoles: globalRole[] = [];
  @State() name: string;
  @State() org: string;

  nameChange(event) {
    this.name = event.target.value;
  }
  orgChange(event) {
    this.org = event.target.value;
  }
  clickSubmit = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();
    // can return the new row instead to avoid page refresh
    const result = await fetchAs<GlobalRolesRequest, GenericResponse>('globalroles/create', { name: this.name, org: parseInt(this.org), email: globals.globalStore.state.email });

    console.log(result);

    if (result.error === ErrorType.NoError) {
      this.name = '';
      this.org = '';
    } else {
      console.error('Failed to create global role');
    }
  };

  componentWillLoad() {
    fetchAs<null, GlobalRolesResponse>('globalroles/read', null).then(res => {
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
        <slot></slot>
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
              <input type="text" id="name" name="name" onInput={event => this.nameChange(event)} />
            </div>

            <div id="ord-holder" class="form-input-item">
              <div>
                <label htmlFor="org">Org</label>
              </div>
              <input type="number" id="number" name="number" onInput={event => this.orgChange(event)} />
            </div>
            <button id="Create-Button" value="Create" onClick={this.clickSubmit}>
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
              {this.globalRoles.map(globalRole => (
                <tr>
                  {/* can loop over these as well (using Map to preserve order) */}
                  <td>{globalRole.id}</td>
                  <td>{globalRole.createdAt}</td>
                  <td>{globalRole.createdBy}</td>
                  <td>{globalRole.modifiedAt}</td>
                  <td>{globalRole.modifiedBy}</td>
                  <td>{globalRole.name}</td>
                  <td>{globalRole.org}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </main>
      </Host>
    );
  }
}

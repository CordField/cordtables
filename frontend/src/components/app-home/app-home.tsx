import { Component, h } from '@stencil/core';
import { globals } from '../../core/global.store';

@Component({
  tag: 'app-home',
  styleUrl: 'app-home.css',
  shadow: true,
})
export class AppHome {
  render() {
    return (
      <div class="app-home">
        <h1>Home</h1>

        {globals.globalStore.state.isLoggedIn && (
          <div>
            <div id="nav-menu">
              <label htmlFor="tables">Table </label>

              <select name="tables" id="tables">
                <option value="global-roles">Global Roles</option>
                <option value="global-role-column-grants">Global Role Column Grants</option>
                <option value="global-role-table-permissions">Global Role Table Permissions</option>
                <option value="global-role-memberships">Global Role Memberships</option>
              </select>
            </div>
          </div>
        )}
      </div>
    );
  }
}

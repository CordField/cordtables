import { Component, h, Prop, State } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { globals } from '../../core/global.store';

@Component({
  tag: 'app-root',
  styleUrl: 'app-root.css',
  shadow: true,
})
export class AppRoot {
  @Prop() history: RouterHistory;

  @State() path: string;

  selectChange(event) {
    console.log(event.target.value);
    const table = event.target.value;
    if (table === '-') {
      this.history.push(`/`);
    } else {
      this.history.push(`/table/${event.target.value}`);
    }
  }

  componentDidLoad() {
    this.history.listen(() => {
      this.path = window.location.pathname;
    });
  }

  componentWillRender() {
    this.path = window.location.pathname;
  }

  render() {
    return (
      <div id="root-wrap-outer">
        <cf-header></cf-header>
        <div id="root-wrap-inner">
          <div>
            {!globals.globalStore.state.isLoggedIn && <div>Please login or register</div>}

            {globals.globalStore.state.isLoggedIn && (
              <div>
                <div id="nav-menu">
                  <div id="table-label">
                    <label htmlFor="tables">Table</label>
                  </div>

                  <div>
                    <select name="tables" id="tables" onChange={event => this.selectChange(event)}>
                      <option selected={this.path === '/'} value="-">
                        -
                      </option>
                      <option selected={this.path === '/table/global-roles'} value="global-roles">
                        Global Roles
                      </option>
                      <option selected={this.path === '/table/global-role-column-grants'} value="global-role-column-grants">
                        Global Role Column Grants
                      </option>
                      <option selected={this.path === '/table/global-role-table-permissions'} value="global-role-table-permissions">
                        Global Role Table Permissions
                      </option>
                      <option selected={this.path === '/table/global-role-memberships'} value="global-role-memberships">
                        Global Role Memberships
                      </option>
                    </select>
                  </div>
                </div>
              </div>
            )}
          </div>
          <main>
            <stencil-router>
              <stencil-route-switch scrollTopOffset={0}>
                <stencil-route url="/" component="app-home" exact={true} />
                <stencil-route url="/profile" component="app-profile" />
                <stencil-route url="/register" component="cf-register" />
                <stencil-route url="/login" component="cf-login" />

                <stencil-route url="/table/:table" component="table-root" />
              </stencil-route-switch>
            </stencil-router>
          </main>
        </div>
      </div>
    );
  }
}

injectHistory(AppRoot);

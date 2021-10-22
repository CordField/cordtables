import { Component, h, Host, Prop, State } from '@stencil/core';
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

  @State() showSelect = false;

  pages = ['Groups', 'Roles', 'Organizations'];

  selectChange(event) {
    const table = event.target.value;
    if (table === '-') {
      this.history.push(`/`);
    } else {
      if (this.pages.includes(event.target.value)) {
        this.history.push(`/page/${event.target.value.toLowerCase()}`);
      } else {
        this.history.push(`/table/${event.target.value}`);
      }
    }
  }

  componentDidLoad() {
    this.history.listen(() => {
      this.path = window.location.pathname;
      this.updateSelect();
    });
  }

  componentWillRender() {
    this.path = window.location.pathname;
    this.updateSelect();
  }

  updateSelect() {
    if (this.path === '/' || this.path.includes('table') || this.path.includes('page')) {
      this.showSelect = true;
    } else {
      this.showSelect = false;
    }
  }

  toggleEditMode = () => {
    globals.globalStore.state.editMode = !globals.globalStore.state.editMode;
  };

  render() {
    return (
      <div id="root-wrap-outer">
        <cf-header></cf-header>
        <div id="root-wrap-inner">
          <div>
            {!globals.globalStore.state.isLoggedIn && <div>Please login or register</div>}

            {globals.globalStore.state.isLoggedIn && this.showSelect && (
              <div id="top-thing">
                <div id="nav-menu">
                  <div>
                    <select name="tables" id="tables" onChange={event => this.selectChange(event)}>
                      <option selected={this.path === '/'} value="-">
                        -
                      </option>

                      {this.pages.map(page => (
                        <option selected={this.path === `/page/${page.toLowerCase()}`} value={page}>
                          {page} Page
                        </option>
                      ))}

                      {globals.globalStore.state.readableTables.map(table => (
                        <option selected={this.path === `/table/${table.split('.').join('-')}`} value={table.split('.').join('-')}>
                          {table}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>

                <button onClick={this.toggleEditMode}>Edit Mode: {globals.globalStore.state.editMode.toString()}</button>
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
                <stencil-route url="/page/:page" component="page-root" />
              </stencil-route-switch>
            </stencil-router>
          </main>
        </div>
        <footer>
          <div id="version-info">
            <div>pre-alpha</div>
            <div>low bandwidth</div>
            <div>rapid application development</div>
          </div>
        </footer>
      </div>
    );
  }
}

injectHistory(AppRoot);

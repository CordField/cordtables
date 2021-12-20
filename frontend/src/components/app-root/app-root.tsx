import { Component, h, Host, Prop, State } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { globals } from '../../core/global.store';
import { v4 as uuidv4 } from 'uuid';
import { AppState } from '../../common/types';

@Component({
  tag: 'app-root',
  styleUrl: 'app-root.css',
  shadow: true,
})
export class AppRoot {
  @Prop() history: RouterHistory;

  @State() path: string;

  @State() showSelect = false;

  pages = ['Groups', 'Roles', 'Organizations', 'Slack', 'Tickets'];

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
    if(this.history){
      this.history.listen(() => {
        this.path = window.location.pathname;
        this.updateSelect();
      });
    }
    
    const infoText = 'INSECURE CONNECTION';
    if (!process.env.SERVER_URL.startsWith('https')) {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: infoText, id: uuidv4(), type: 'info' });
    }
    if (globals.globalStore.state.token === null || globals.globalStore.state.token === undefined) {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'TOKEN NOT FOUND', id: uuidv4(), type: 'info' });
    }
    // else {
    //   globals.globalStore.state.notifications = globals.globalStore.state.notifications.filter(notif => notif.text !== infoText);
    // }
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
    console.debug("globals.globalStore.state.appState", globals.globalStore.state.appState);
    return (
      <div id="root-wrap-outer">
        <cf-notif />
        <cf-header />
        {globals.globalStore.state.appState === AppState.TranslationLoaded && (
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

                  {this.path != '/page/tickets' && <button onClick={this.toggleEditMode}>Edit Mode: {globals.globalStore.state.editMode.toString()}</button>}
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

                  <stencil-route url="/page/:page/:requestId?" component="page-root" />
                </stencil-route-switch>
              </stencil-router>
            </main>
          </div>
        )}
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

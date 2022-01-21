import { Component, h, Host, Listen, Prop, State } from '@stencil/core';
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

  pages = ['Groups', 'Roles', 'Organizations', 'Slack', 'Tickets', 'Prayer Requests'];

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
    if (this.history) {
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
    console.debug('globals.globalStore.state.appState', globals.globalStore.state.appState);
    return (
      <div id="root-wrap-outer">
        <cf-notif />
        <cf-header />
        <foreign-row />
        {globals.globalStore.state.appState === AppState.TranslationLoaded && (
          <div id="root-wrap-inner">
            <div>
              {!globals.globalStore.state.isLoggedIn && <div>Please login or register</div>}

              {globals.globalStore.state.isLoggedIn && this.showSelect && (
                <div id="container">
                  <div id="top-thing">
                    <div id="nav-menu">
                      <div id="full-width">
                        <custom-accordion width="100%" label="Menu" color="green">
                          <custom-accordion width="100%" label="Pages" color="#64b145" marginLeft="10px">
                            <div class="scrollable">
                              <ion-list style={{ marginLeft: '10px' }}>
                                {this.pages.map(page => (
                                  <ion-item href={`/page/${page.toLowerCase().replace(" ","-")}`}>
                                    <ion-label>{page} Page</ion-label>
                                  </ion-item>
                                ))}
                                <ion-item href={`/sc/partner-crm`}>
                                    <ion-label>Partner CRM</ion-label>
                                  </ion-item>
                              </ion-list>
                            </div>
                          </custom-accordion>
                          <custom-accordion width="100%" label="Tables" color="#64b145" marginLeft="10px">
                            <div class="scrollable">
                              <ion-list style={{ marginLeft: '10px' }}>
                                {globals.globalStore.state.readableTables.map(table => (
                                  <ion-item href={`/table/${table.split('.').join('/')}`}>
                                    <ion-label>{table} </ion-label>
                                  </ion-item>
                                ))}
                              </ion-list>
                            </div>
                          </custom-accordion>
                        </custom-accordion>
                      </div>
                    </div>
                  </div>
                  <div class="edit-button">
                    {this.path != '/page/tickets' && (
                      <button onClick={this.toggleEditMode} style={{ float: 'right' }}>
                        Edit Mode: {globals.globalStore.state.editMode.toString()}
                      </button>
                    )}
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
                  <stencil-route url="/forgot-password" component="cf-forgot-password" />
                  <stencil-route url="/reset-password/:token" component="cf-reset-password" />
                  <stencil-route url="/table/:table" component="table-root" />
                  <stencil-route url="/sc/partner-crm" component="partner-crm" />

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

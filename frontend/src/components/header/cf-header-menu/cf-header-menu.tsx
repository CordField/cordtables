import { Component, Host, h, Event, EventEmitter, Prop } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { globals } from '../../../core/global.store';
import { MenuClickedEvent } from '../types';

@Component({
  tag: 'cf-header-menu',
  styleUrl: 'cf-header-menu.css',
  shadow: true,
})
export class CfHeaderMenu {
  @Prop() history: RouterHistory;

  @Event() menuClicked: EventEmitter<MenuClickedEvent>;

  clickedHome = () => {
    this.menuClicked.emit({ type: 'MenuClicked' });
    this.history.push(`/`);
  };

  clickedPrayerRequests = () => {
    this.menuClicked.emit({ type: 'MenuClicked' });
    this.history.push(`/page/prayer-requests`);
  }

  clickedProfile = () => {
    this.menuClicked.emit({ type: 'MenuClicked' });
    this.history.push(`/profile`);
  };

  clickedRegister = () => {
    this.menuClicked.emit({ type: 'MenuClicked' });
    this.history.push(`/register`);
  };

  clickedLogin = () => {
    this.menuClicked.emit({ type: 'MenuClicked' });
    this.history.push(`/login`);
  };
  

  clickedLogout = () => {
    globals.globalStore.state.isLoggedIn = false;
    globals.globalStore.state.token = null;
    globals.globalStore.state.email = null;
    localStorage.clear();
    this.menuClicked.emit({ type: 'MenuClicked' });
    this.history.push(`/`);
  };

  render() {
    return (
      <Host>
        <slot></slot>
        <div id="header-menu">
          <button class="menu-item" onClick={this.clickedHome}>
            Home
          </button>

          {globals.globalStore.state.isLoggedIn && (
            <button class="menu-item" onClick={this.clickedPrayerRequests}>
              Prayer Requests
            </button>
          )}

          {globals.globalStore.state.isLoggedIn && (
            <button class="menu-item" onClick={this.clickedProfile}>
              Profile page
            </button>
          )}

          {!globals.globalStore.state.isLoggedIn && (
            <button class="menu-item" onClick={this.clickedLogin}>
              Login
            </button>
          )}

          {!globals.globalStore.state.isLoggedIn && (
            <button class="menu-item" onClick={this.clickedRegister}>
              Register
            </button>
          )}

          {globals.globalStore.state.isLoggedIn && (
            <button class="menu-item" onClick={this.clickedLogout}>
              Logout
            </button>
          )}
        </div>
      </Host>
    );
  }
}

injectHistory(CfHeaderMenu);

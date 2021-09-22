import { Component, Host, h, Event, EventEmitter, Prop } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { MenuClickedEvent } from '../types';

@Component({
  tag: 'cf-header-menu',
  styleUrl: 'cf-header-menu.css',
  shadow: true,
})
export class CfHeaderMenu {
  @Prop() history: RouterHistory;

  @Event() menuClicked: EventEmitter<MenuClickedEvent>;

  clickedProfile = () => {
    this.menuClicked.emit({ type: 'MenuClicked' });
    this.history.push(`/profile`);
  };

  clickedRegister = () => {
    this.menuClicked.emit({ type: 'MenuClicked' });
    this.history.push(`/register`);
  };

  render() {
    return (
      <Host>
        <slot></slot>
        <div id="header-menu">
          <button class="menu-item" onClick={this.clickedProfile}>
            Profile page
          </button>
          <button class="menu-item" onClick={this.clickedRegister}>
            Register
          </button>
        </div>
      </Host>
    );
  }
}

injectHistory(CfHeaderMenu);

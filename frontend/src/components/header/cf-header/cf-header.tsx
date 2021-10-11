import { Component, Host, h, State, Prop, Listen } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { MenuClickedEvent } from '../types';

@Component({
  tag: 'cf-header',
  styleUrl: 'cf-header.css',
  shadow: true,
})
export class CfHeader {
  @Prop() history: RouterHistory;

  @State() showMenu = false;

  @Listen('menuClicked')
  menuClickHandler(event: CustomEvent<MenuClickedEvent>) {
    event.preventDefault();
    event.stopPropagation();
    this.showMenu = !this.showMenu;
  }

  clickTitle = () => {
    this.history.push(`/`);
  };

  toggleMenu = () => {
    this.showMenu = !this.showMenu;
  };

  render() {
    return (
      <Host>
        <slot></slot>
        <div id="header">
          <div id="title" onClick={this.clickTitle}>
            <span id="cord">CORD</span>
            <span id="field">TABLES</span>
            <span id="dot-org"></span>
          </div>

          <div id="menu-button">
            <ion-icon name="menu-outline" size="large" onClick={this.toggleMenu}></ion-icon>
          </div>
        </div>
        {this.showMenu && <cf-header-menu></cf-header-menu>}
      </Host>
    );
  }
}

injectHistory(CfHeader);

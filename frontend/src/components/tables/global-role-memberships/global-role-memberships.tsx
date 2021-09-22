import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'global-role-memberships',
  styleUrl: 'global-role-memberships.css',
  shadow: true,
})
export class GlobalRoleMemberships {
  render() {
    return (
      <Host>
        <slot></slot>
        <h1>Global Role Memberships</h1>
      </Host>
    );
  }
}

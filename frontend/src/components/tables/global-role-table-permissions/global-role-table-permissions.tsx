import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'global-role-table-permissions',
  styleUrl: 'global-role-table-permissions.css',
  shadow: true,
})
export class GlobalRoleTablePermissions {
  render() {
    return (
      <Host>
        <slot></slot>
        <h1>Global Role Table Permissions</h1>
      </Host>
    );
  }
}

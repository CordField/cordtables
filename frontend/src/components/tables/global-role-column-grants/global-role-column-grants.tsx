import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'global-role-column-grants',
  styleUrl: 'global-role-column-grants.css',
  shadow: true,
})
export class GlobalRoleColumnGrants {
  render() {
    return (
      <Host>
        <slot></slot>
        <h1>Global Role Column Grants</h1>
      </Host>
    );
  }
}

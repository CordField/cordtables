import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'roles-page',
  styleUrl: 'roles-page.css',
  shadow: true,
})
export class RolesPage {
  render() {
    return (
      <Host>
        <slot></slot>
        Roles Page
        <roles-table></roles-table>
        <role-memberships></role-memberships>
        <role-table-permissions></role-table-permissions>
        <role-column-grants></role-column-grants>
      </Host>
    );
  }
}

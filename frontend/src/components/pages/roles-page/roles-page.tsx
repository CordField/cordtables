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
        <div class="table-wrap">
          <roles-table></roles-table>
        </div>
        <div class="table-wrap">
          <role-memberships></role-memberships>
        </div>
        <div class="table-wrap">
          <role-table-permissions></role-table-permissions>
        </div>
        <div class="table-wrap">
          <role-column-grants></role-column-grants>
        </div>
      </Host>
    );
  }
}

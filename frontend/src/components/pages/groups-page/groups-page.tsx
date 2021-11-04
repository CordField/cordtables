import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'groups-page',
  styleUrl: 'groups-page.css',
  shadow: true,
})
export class GroupsPage {
  render() {
    return (
      <Host>
        <slot></slot>
        <div class="table-wrap">
          <groups-table></groups-table>
        </div>
        <div class="table-wrap">
          <group-memberships></group-memberships>
        </div>
        <div class="table-wrap">
          <groups-row-access></groups-row-access>
        </div>
      </Host>
    );
  }
}

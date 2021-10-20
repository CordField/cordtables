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
        Groups Page
        <groups-table></groups-table>
        <group-memberships></group-memberships>
        <groups-row-access></groups-row-access>
      </Host>
    );
  }
}

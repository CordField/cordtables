import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'groups-row-access',
  styleUrl: 'groups-row-access.css',
  shadow: true,
})
export class GroupsRowAccess {
  render() {
    return (
      <Host>
        <slot></slot>
        group row access
      </Host>
    );
  }
}

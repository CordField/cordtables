import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'locations-table',
  styleUrl: 'locations-table.css',
  shadow: true,
})
export class LocationsTable {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

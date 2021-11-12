import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'peers-table',
  styleUrl: 'peers-table.css',
  shadow: true,
})
export class PeersTable {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

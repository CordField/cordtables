import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'threads-table',
  styleUrl: 'threads-table.css',
  shadow: true,
})
export class ThreadsTable {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'tickets-table',
  styleUrl: 'tickets-table.css',
  shadow: true,
})
export class TicketsTable {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

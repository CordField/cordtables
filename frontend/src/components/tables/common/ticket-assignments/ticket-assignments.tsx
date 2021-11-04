import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'ticket-assignments',
  styleUrl: 'ticket-assignments.css',
  shadow: true,
})
export class TicketAssignments {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

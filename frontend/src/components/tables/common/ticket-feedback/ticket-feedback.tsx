import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'ticket-feedback',
  styleUrl: 'ticket-feedback.css',
  shadow: true,
})
export class TicketFeedback {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

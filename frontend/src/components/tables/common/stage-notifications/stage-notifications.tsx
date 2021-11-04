import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'stage-notifications',
  styleUrl: 'stage-notifications.css',
  shadow: true,
})
export class StageNotifications {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

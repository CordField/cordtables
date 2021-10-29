import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'discussion-channels',
  styleUrl: 'discussion-channels.css',
  shadow: true,
})
export class DiscussionChannels {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

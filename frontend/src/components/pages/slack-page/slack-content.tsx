import { Component, Host, h, Prop, Watch, Event, EventEmitter, Listen } from '@stencil/core';
import { CommonDiscussionChannel, CommonDiscussionChannelListResponse } from '../../tables/common/discussion-channels/types';

// will take discussion channels as a prop
@Component({
  tag: 'slack-content',
  styleUrl: 'slack-page.css',
  shadow: true,
})
export class SlackSidebar {
  // @Prop({ mutable: true }) selectedDiscussionChannel: CommonDiscussionChannel;
  componentDidLoad() {
    console.log('slack-content component loaded');
  }
 
  render() {
    return (
      <Host class="slack-content">
        <slot></slot>
      </Host>
    );
  }
}

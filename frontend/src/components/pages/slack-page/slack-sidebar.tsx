import { Component, Host, h, Prop, Watch, Event, EventEmitter } from '@stencil/core';
import { DiscussionChannels } from '../../tables/common/discussion-channels/discussion-channels';
import { CommonDiscussionChannel, CommonDiscussionChannelListResponse } from '../../tables/common/discussion-channels/types';

// will take discussion channels as a prop
@Component({
  tag: 'slack-sidebar',
  styleUrl: 'slack-page.css',
  shadow: true,
})
export class SlackSidebar {
  @Prop() discussionChannels: CommonDiscussionChannelListResponse;
  @Event({ eventName: 'channelSelected' }) channelSelected: EventEmitter<CommonDiscussionChannel>;

  // @Prop({ mutable: true }) selectedDiscussionChannel: CommonDiscussionChannel;

  componentWillLoad() {
    console.log('component did load', this.discussionChannels);
  }

  clickHandler(e) {
    const selectedDiscussionChannel = this.discussionChannels.discussion_channels.find(discussion_channel => discussion_channel.name === e.target.innerText);
    console.log(selectedDiscussionChannel);
    this.channelSelected.emit(selectedDiscussionChannel);
  }

  render() {
    const jsx = this.discussionChannels ? (
      this.discussionChannels.discussion_channels?.map(discussionChannel => (
        <div class="discussion-channel" onClick={e => this.clickHandler(e)}>
          {discussionChannel.name}
        </div>
      ))
    ) : (
      <div>Loading...</div>
    );
    console.log('discussion_channel', this.discussionChannels);
    return (
      <Host class="slack-sidebar">
        <slot></slot>
        {jsx}
      </Host>
    );
  }
}

import { Component, Host, h, Prop, Watch, Event, EventEmitter, State } from '@stencil/core';
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
  @State() selectedDiscussionChannel: CommonDiscussionChannel;

  // @Prop({ mutable: true }) selectedDiscussionChannel: CommonDiscussionChannel;

  componentWillLoad() {
    console.log('component did load', this.discussionChannels);
    this.selectedDiscussionChannel = this.discussionChannels?.discussion_channels.length > 0 ? this.discussionChannels.discussion_channels[0] : null;
    this.channelSelected.emit(this.selectedDiscussionChannel);
  }

  clickHandler(e) {
    this.selectedDiscussionChannel = this.discussionChannels.discussion_channels.find(discussion_channel => discussion_channel.name === e.target.innerText);
    console.log(this.selectedDiscussionChannel);
    this.channelSelected.emit(this.selectedDiscussionChannel);
  }

  render() {
    const jsx = this.discussionChannels ? (
      this.discussionChannels.discussion_channels?.map(discussionChannel => {
        const discussionChannelClassName = `discussion-channel ${discussionChannel.id === this.selectedDiscussionChannel?.id ? 'selected-discussion-channel' : ''}`;
        return (
          <div class={discussionChannelClassName} onClick={e => this.clickHandler(e)}>
            {discussionChannel.name}
          </div>
        );
      })
    ) : (
      <div>Loading...</div>
    );
    return (
      <Host>
        <slot></slot>
        {jsx}
      </Host>
    );
  }
}

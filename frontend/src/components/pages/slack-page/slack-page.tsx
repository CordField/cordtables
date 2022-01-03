import { Component, Host, h, State, Watch, Listen } from '@stencil/core';
import { ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { CommonDiscussionChannel, CommonDiscussionChannelListRequest, CommonDiscussionChannelListResponse } from '../../tables/common/discussion-channels/types';

@Component({
  tag: 'slack-page',
  styleUrl: 'slack-page.css',
  shadow: true,
})

// discussion channels, threads, posts.
//  fetch discussion channels first, then fetch the threads for that discussion channel, then fetch the posts
// based on selected value select the threads and posts of the discussion channel
export class SlackPage {
  @State() discussionChannels: CommonDiscussionChannel[] = [];
  @State() loading = true;
  @State() selectedDiscussionChannel?: CommonDiscussionChannel = null;
  @State() showForm?: boolean = false;

  async componentWillLoad() {
    await this.getDiscussionChannels();
    this.selectedDiscussionChannel = this.discussionChannels[0];
  }
  async getDiscussionChannels() {
    const discussionChannelsResponse = await fetchAs<CommonDiscussionChannelListRequest, CommonDiscussionChannelListResponse>('common/discussion-channels/list', {
      token: globals.globalStore.state.token,
    });
    if (discussionChannelsResponse.error === ErrorType.NoError) {
      this.discussionChannels = discussionChannelsResponse.discussion_channels;
    }
    this.loading = false;
  }
  @Listen('channelSelected')
  channelSelectedHandler(event: CustomEvent<CommonDiscussionChannel>) {
    console.log('event received', event);
    this.selectedDiscussionChannel = event.detail;
  }

  render() {
    return (
      <Host>
        <slot></slot>
        <div class="slack-page">
          <slack-sidebar discussionChannels={this.discussionChannels} loading={this.loading} />
          <slack-content selectedDiscussionChannel={this.selectedDiscussionChannel} />
        </div>
      </Host>
    );
  }
}

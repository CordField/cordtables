import { Component, Host, h, Prop, Watch, Event, EventEmitter, Listen, State } from '@stencil/core';
import { ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { CommonDiscussionChannel } from '../../tables/common/discussion-channels/types';
import { CommonPost, CommonPostsListRequest, CommonPostsListResponse } from '../../tables/common/posts/types';
import { CommonThread, CommonThreadsListRequest, CommonThreadsListResponse } from '../../tables/common/threads/types';

// will take discussion channels as a prop
@Component({
  tag: 'slack-content',
  styleUrl: 'slack-page.css',
  shadow: true,
})
export class SlackContent {
  @Prop() selectedDiscussionChannel: CommonDiscussionChannel;
  @State() threadListResponse: CommonThreadsListResponse = null;
  @State() postListResponse: CommonPostsListResponse = null;
  @State() channelThreads: CommonThread[] = [];
  @State() channelPosts: CommonPost[] = [];
  @Watch('selectedDiscussionChannel')
  async handleSelectedDiscussionChannelChange(newValue: CommonDiscussionChannel, oldValue: CommonDiscussionChannel) {
    // if (this.threadListResponse.error === ErrorType.NoError && newValue) {
    if (newValue) await this.getThreads(newValue.id);
    // this.channelThreads = this.threadListResponse.threads.filter(thread => thread.channel === newValue?.id);
    // console.log(this.channelThreads);
    // }
    if (newValue && this.postListResponse.error === ErrorType.NoError) {
      this.channelPosts = this.postListResponse.posts.filter(post => this.channelThreads.findIndex(thread => thread.id === post.thread) !== -1);
    }
  }
  @Listen('threadAdded')
  handleThreadAddedChange(event: CustomEvent<CommonThread>) {
    if (this.threadListResponse.error === ErrorType.NoError) {
      this.channelThreads = this.channelThreads.concat(event.detail);
    }
  }
  async componentWillLoad() {
    await this.getThreads(this.selectedDiscussionChannel.id);

    this.postListResponse = await this.getPosts();
    if (this.selectedDiscussionChannel && this.postListResponse.error === ErrorType.NoError) {
      this.channelPosts = this.postListResponse.posts.filter(post => this.channelThreads.findIndex(thread => thread.id === post.thread) !== -1);
    }
  }
  async getThreads(discussionChannelId: number) {
    this.threadListResponse = await fetchAs<CommonThreadsListRequest, CommonThreadsListResponse>('common-threads/list', {
      token: globals.globalStore.state.token,
      channelId: discussionChannelId,
    });
    if (this.threadListResponse.error === ErrorType.NoError) {
      this.channelThreads = this.threadListResponse.threads;
    }
  }
  async getPosts() {
    return await fetchAs<CommonPostsListRequest, CommonPostsListResponse>('common-posts/list', {
      token: globals.globalStore.state.token,
    });
  }

  render() {
    const threadsJsx =
      this.channelThreads === null ? (
        <div>Loading..</div>
      ) : this.channelThreads.length > 0 ? (
        <div>
          {this.channelThreads.map(thread => (
            <slack-thread threadPosts={this.channelPosts.filter(post => post.thread === thread.id)} thread={thread} key={thread.id} />
          ))}
        </div>
      ) : this.selectedDiscussionChannel !== null ? (
        <div>No threads in this channel yet!</div>
      ) : (
        <div>No channels found!</div>
      );

    return (
      <Host>
        <slot></slot>
        {threadsJsx}
        <slack-form selectedChannelId={this.selectedDiscussionChannel?.id} type="thread" />
      </Host>
    );
  }
}

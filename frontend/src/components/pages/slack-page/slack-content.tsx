import { Component, Host, h, Prop, Watch, Event, EventEmitter, Listen, State } from '@stencil/core';
import { ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { CommonDiscussionChannel } from '../../tables/common/discussion-channels/types';
import { CommonThread, CommonThreadsListRequest, CommonThreadsListResponse, DeleteCommonThreadsRequest, DeleteCommonThreadsResponse } from '../../tables/common/threads/types';
import { v4 as uuidv4 } from 'uuid';

// will take discussion channels as a prop
@Component({
  tag: 'slack-content',
  styleUrl: 'slack-page.css',
  shadow: true,
})
export class SlackContent {
  @Prop() selectedDiscussionChannel: CommonDiscussionChannel;
  @State() threadListResponse: CommonThreadsListResponse = null;
  @State() channelThreads: CommonThread[] = [];
  contentThreads: HTMLDivElement;
  @Watch('selectedDiscussionChannel')
  async handleSelectedDiscussionChannelChange(newValue: CommonDiscussionChannel, oldValue: CommonDiscussionChannel) {
    if (newValue) await this.getThreads(newValue.id);
  }
  @Listen('threadDeleted')
  async handleThreadDeletedChange(event: CustomEvent<number>) {
    const deleteResponse = await fetchAs<DeleteCommonThreadsRequest, DeleteCommonThreadsResponse>('common-threads/delete', {
      token: globals.globalStore.state.token,
      id: event.detail,
    });
    if (deleteResponse.error === ErrorType.NoError) {
      this.channelThreads = this.channelThreads?.filter(thread => thread.id !== event.detail);
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'unable to delete thread', id: uuidv4(), type: 'error' });
    }
  }
  @Listen('threadAdded')
  handleThreadAddedChange(event: CustomEvent<CommonThread>) {
    this.channelThreads = this.channelThreads?.concat(event.detail);
  }
  async componentWillRender() {
    await this.getThreads(this.selectedDiscussionChannel?.id);
  }

  componentDidRender() {
    (this.contentThreads.lastChild as HTMLDivElement).scrollIntoView({ behavior: 'smooth' });
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

  render() {
    const threadsJsx = (
      <div class="content-threads" ref={contentThreads => (this.contentThreads = contentThreads)}>
        {this.channelThreads === null
          ? 'Loading..'
          : this.channelThreads.length > 0
          ? this.channelThreads.map(thread => <slack-thread thread={thread} key={thread.id} />)
          : this.selectedDiscussionChannel !== null
          ? 'No threads in this channel yet!'
          : 'No channels found!'}
      </div>
    );

    return (
      <Host>
        <slot></slot>
        {threadsJsx}
        <slack-form selectedChannelId={this.selectedDiscussionChannel?.id} type="thread" class="thread-form" />
      </Host>
    );
  }
}

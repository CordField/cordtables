import { Component, Host, h, Prop, Watch, Event, EventEmitter, Listen, State } from '@stencil/core';
import { ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { CommonDiscussionChannel } from '../../tables/common/discussion-channels/types';
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
  @State() channelThreads: CommonThread[] = null;
  @Watch('selectedDiscussionChannel')
  handleSelectedDiscussionChannelChange(newValue: CommonDiscussionChannel, oldValue: CommonDiscussionChannel) {
    if (this.threadListResponse.error === ErrorType.NoError && newValue) {
      this.channelThreads = this.threadListResponse.threads.filter(thread => thread.channel === newValue?.id);
    }
  }
  async componentWillLoad() {
    this.threadListResponse = await this.getThreads();
    if (this.selectedDiscussionChannel && this.threadListResponse.error === ErrorType.NoError) {
      this.channelThreads = this.threadListResponse.threads.filter(thread => thread.channel === this.selectedDiscussionChannel?.id);
    }
  }
  async getThreads() {
    return await fetchAs<CommonThreadsListRequest, CommonThreadsListResponse>('common-threads/list', {
      token: globals.globalStore.state.token,
    });
  }

  componentDidLoad() {
    console.log('slack-content component loaded', this.selectedDiscussionChannel);
  }

  render() {
    const jsx =
      this.channelThreads === null ? (
        <div>Loading..</div>
      ) : (
        <div>
          {this.channelThreads.map(thread => (
            <div>{thread.content}</div>
          ))}
        </div>
      );
    return (
      <Host class="slack-content">
        <slot></slot>
        {jsx}
      </Host>
    );
  }
}

import { Component, Host, h, Prop, State, Event, EventEmitter } from '@stencil/core';
import { create } from 'domain';
import { runInThisContext } from 'vm';
import { ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

import { CommonDiscussionChannel } from '../../tables/common/discussion-channels/types';
import { CommonPost, CommonPostsListRequest, CommonPostsListResponse, CreateCommonPostsRequest, CreateCommonPostsResponse } from '../../tables/common/posts/types';
import { CommonThread, CommonThreadsListRequest, CommonThreadsListResponse, CreateCommonThreadsRequest, CreateCommonThreadsResponse } from '../../tables/common/threads/types';

@Component({
  tag: 'slack-form',
  styleUrl: 'slack-page.css',
  shadow: true,
})
export class SlackForm {
  // this will be 0 in case of root component
  @Prop() type: 'thread' | 'post' = 'thread';
  @Prop() selectedChannelId: number = null;
  @Prop() selectedThreadId: number = null;
  @State() content: string = null;
  @Event({ eventName: 'threadAdded' }) threadAdded: EventEmitter<CommonThread>;
  @Event({ eventName: 'postAdded' }) postAdded: EventEmitter<CommonPost>;
  handleContentChange(e) {
    this.content = e.target.value;
  }

  componentWillLoad() {
    console.group('slack-form');
    console.log(this.selectedChannelId);
    console.log(this.selectedThreadId);
    console.log(this.type);
    console.groupEnd();
  }
  // event emitter to add to threads or to posts
  async handleCreate(e) {
    e.preventDefault();
    if (this.type === 'thread') {
      const createResponse = await fetchAs<CreateCommonThreadsRequest, CreateCommonThreadsResponse>('common-threads/create-read', {
        token: globals.globalStore.state.token,
        thread: {
          channel: this.selectedChannelId,
          content: this.content,
        },
      });
      if (createResponse.error === ErrorType.NoError) {
        this.threadAdded.emit(createResponse.thread);
      } else {
        globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: createResponse.error, id: uuidv4(), type: 'error' });
      }
    } else {
      const createResponse = await fetchAs<CreateCommonPostsRequest, CreateCommonPostsResponse>('common-posts/create-read', {
        token: globals.globalStore.state.token,
        post: {
          content: this.content,
          thread: this.selectedThreadId,
        },
      });
      if (createResponse.error === ErrorType.NoError) {
        this.postAdded.emit(createResponse.post);
      } else {
        globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: createResponse.error, id: uuidv4(), type: 'error' });
      }
    }
  }
  setContentToNull() {
    this.content = '';
  }
  render() {
    const formClassName = this.type === 'thread' ? `slack-form slack-form-last` : `slack-form`;
    const jsx = (
      <form class={formClassName}>
        <input type="text" name="content" id="content" value={this.content} onChange={e => this.handleContentChange(e)} />
        <button
          onClick={e => {
            this.handleCreate(e);
            this.setContentToNull();
          }}
          class="slack-form-button"
        >
          submit
        </button>
      </form>
    );
    return (
      <Host class="slack-thread">
        <slot></slot>
        {jsx}
      </Host>
    );
  }
}
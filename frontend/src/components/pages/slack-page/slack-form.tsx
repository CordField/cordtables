import { Component, Host, h, Prop, State, Event, EventEmitter, Listen } from '@stencil/core';
import { ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

import { CommonDiscussionChannel, CreateCommonDiscussionChannelRequest, CreateCommonDiscussionChannelResponse } from '../../tables/common/discussion-channels/types';
import { CommonPost, CommonPostsListRequest, CommonPostsListResponse, CreateCommonPostsRequest, CreateCommonPostsResponse } from '../../tables/common/posts/types';
import { CommonThread, CommonThreadsListRequest, CommonThreadsListResponse, CreateCommonThreadsRequest, CreateCommonThreadsResponse } from '../../tables/common/threads/types';
import { TinyUpdateEvent } from '../../cf-tiny/types';
import { idService } from '../../../core/id.service';

@Component({
  tag: 'slack-form',
  styleUrl: 'slack-page.css',
  shadow: true,
})
export class SlackForm {
  tinyMceId: string = idService.getNextId();
  @Prop() type: 'thread' | 'post' = 'thread';
  @Prop() selectedChannelId: number = null;
  @Prop() selectedThreadId: number = null;
  @State() content: string = null;
  @Event({ eventName: 'threadAdded' }) threadAdded: EventEmitter<CommonThread>;
  @Event({ eventName: 'postAdded' }) postAdded: EventEmitter<CommonPost>;

  @Listen('contentUpdate')
  handleContentUpdateChange(e: CustomEvent<TinyUpdateEvent>) {
    if (e.detail.id === this.tinyMceId) this.content = e.detail.content;
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
    this.content = null;
  }
  render() {
    const formClassName = this.type === 'thread' ? `slack-form slack-form-last` : `slack-form`;

    return (
      <Host class="slack-thread">
        <slot></slot>
        <form
          class={formClassName}
          onSubmit={e => {
            this.handleCreate(e);
            this.setContentToNull();
          }}
        >
          <cf-tiny uid={this.tinyMceId} />
          <button class="slack-form-button">submit</button>
        </form>
      </Host>
    );
  }
}

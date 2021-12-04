import { Component, Host, h, Prop, Watch, Event, EventEmitter, Listen, State } from '@stencil/core';
import { v4 } from 'uuid';
import { ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { idService, IdService } from '../../../core/id.service';
import { TinyUpdateEvent } from '../../cf-tiny/types';
import { CommonDiscussionChannel } from '../../tables/common/discussion-channels/types';
import { CommonPost, CommonPostsListRequest, CommonPostsListResponse } from '../../tables/common/posts/types';
import { CommonThread, CommonThreadsListRequest, CommonThreadsListResponse, CommonThreadsUpdateRequest, CommonThreadsUpdateResponse } from '../../tables/common/threads/types';

// will take discussion channels as a prop
@Component({
  tag: 'slack-thread',
  styleUrl: 'slack-page.css',
  shadow: true,
})
export class SlackThread {
  tinyMceId: string = idService.getNextId();
  @Prop() thread: CommonThread;
  @State() threadPostsResponse: CommonPostsListResponse = null;
  @State() threadPosts: CommonPost[];
  @State() showPosts: boolean = false;
  @State() showEditAndDeleteButtons: boolean = false;
  @State() updateMode: boolean = false;
  @State() threadContent: string = null;
  @Event({ eventName: 'threadDeleted' }) threadDeleted: EventEmitter<number>;

  componentWillLoad() {
    this.threadContent = this.thread.content;
  }

  @Listen('contentUpdate')
  handleContentUpdateChange(e: CustomEvent<TinyUpdateEvent>) {
    if (e.detail.id === this.tinyMceId) this.threadContent = e.detail.content;
  }

  @Watch('showPosts')
  async handleShowPostsChange(newValue: boolean, oldValue: boolean) {
    if (newValue === true && this.threadPostsResponse === null) {
      await this.getPosts();
    }
  }

  @Listen('postAdded')
  postAddedHandler(event: CustomEvent<CommonPost>) {
    console.log('event received', event);
    this.threadPosts = this.threadPosts.concat(event.detail);
  }

  async getPosts() {
    this.threadPostsResponse = await fetchAs<CommonPostsListRequest, CommonPostsListResponse>('common-posts/list', {
      token: globals.globalStore.state.token,
      threadId: this.thread.id,
    });
    if (this.threadPostsResponse.error === ErrorType.NoError) {
      this.threadPosts = this.threadPostsResponse.posts;
    }
  }

  mouseEnterAndLeaveHandler() {
    if (this.thread.owning_person === globals.globalStore.state.userId) this.showEditAndDeleteButtons = !this.showEditAndDeleteButtons;
  }

  render() {
    const editAndDeleteButtons = (
      <span class="slack-thread-buttons">
        {this.updateMode ? (
          <span>
            <span
              class="thread-update-confirm"
              onClick={async e => {
                e.stopPropagation();
                this.updateMode = false;
                const updateResponse = await fetchAs<CommonThreadsUpdateRequest, CommonThreadsUpdateResponse>('common-threads/update-read', {
                  token: globals.globalStore.state.token,
                  column: 'content',
                  id: this.thread.id,
                  value: this.threadContent !== '' ? this.threadContent : this.thread.content,
                });
                if (updateResponse.error === ErrorType.NoError) {
                  this.threadContent = updateResponse.thread.content;
                }
              }}
            >
              ✔️
            </span>
            <span
              class="thread-update-cancel"
              onClick={e => {
                e.stopPropagation();
                this.updateMode = false;
                this.threadContent = this.thread.content;
              }}
            >
              ❌
            </span>
          </span>
        ) : this.showEditAndDeleteButtons ? (
          <span>
            <span
              class="thread-update"
              onClick={e => {
                e.stopPropagation();
                this.updateMode = true;
              }}
            >
              ✎
            </span>
            <span
              class="thread-delete"
              onClick={e => {
                e.stopPropagation();
                this.threadDeleted.emit(this.thread.id);
              }}
            >
              ⛔
            </span>
          </span>
        ) : null}
      </span>
    );

    const jsx = (
      <div>
        <div
          onMouseEnter={() => {
            setTimeout(this.mouseEnterAndLeaveHandler.bind(this), 100);
          }}
          onMouseLeave={() => {
            setTimeout(this.mouseEnterAndLeaveHandler.bind(this), 100);
          }}
          class="slack-thread-content"
        >
          <span
            class="post-indicator"
            onClick={() => {
              this.showPosts = !this.showPosts;
            }}
          >
            {this.showPosts ? '👇' : '👉'}
          </span>
          {this.updateMode ? <cf-tiny initialHTMLContent={this.threadContent} uid={this.tinyMceId} /> : <span class="thread-content" innerHTML={this.threadContent}></span>}

          {editAndDeleteButtons}
        </div>
        <div class="thread-posts">{this.showPosts && this.threadPosts?.map(post => <div innerHTML={post.content} />)}</div>
      </div>
    );

    return (
      <Host class="slack-thread">
        <slot></slot>
        {jsx}
        {this.showPosts && <slack-form type="post" selectedThreadId={this.thread.id} />}
      </Host>
    );
  }
}

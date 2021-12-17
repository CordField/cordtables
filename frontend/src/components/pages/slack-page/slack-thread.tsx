import { Component, Host, h, Prop, Watch, Event, EventEmitter, Listen, State } from '@stencil/core';
import { v4 } from 'uuid';
import { ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { idService } from '../../../core/id.service';
import { TinyUpdateEvent } from '../../cf-tiny/types';
import { CommonPost, CommonPostsListRequest, CommonPostsListResponse, DeleteCommonPostsRequest, DeleteCommonPostsResponse } from '../../tables/common/posts/types';
import { CommonThread, CommonThreadsUpdateRequest, CommonThreadsUpdateResponse } from '../../tables/common/threads/types';

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
  @Listen('postDeleted')
  async handleThreadDeletedChange(event: CustomEvent<number>) {
    const deleteResponse = await fetchAs<DeleteCommonPostsRequest, DeleteCommonPostsResponse>('common-posts/delete', {
      token: globals.globalStore.state.token,
      id: event.detail,
    });
    if (deleteResponse.error === ErrorType.NoError) {
      this.threadPosts = this.threadPosts?.filter(thread => thread.id !== event.detail);
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'unable to delete thread', id: v4(), type: 'error' });
    }
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

  // mouseEnterAndLeaveHandler(e) {
  //   e.stopPropagation();
  //   console.log('mouse enter/leave', this.thread.owning_person, globals.globalStore.state.userId, this.showEditAndDeleteButtons, this.thread.id);
  //   if (this.thread.owning_person === globals.globalStore.state.userId) {
  //     this.showEditAndDeleteButtons = !this.showEditAndDeleteButtons;
  //   }
  // }

  render() {
    const slackThreadButtonsClass = this.updateMode === false ? 'thread-buttons' : 'thread-buttons thread-buttons-update';
    const editAndDeleteButtons = (
      <span class={slackThreadButtonsClass}>
        {this.updateMode ? (
          <span>
            <span
              class="update-confirm-icon"
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
              <ion-icon name="checkmark-circle-outline"></ion-icon>
            </span>
            <span
              class="update-cancel-icon thread-icon"
              onClick={e => {
                e.stopPropagation();
                this.updateMode = false;
                this.threadContent = this.thread.content;
              }}
            >
              <ion-icon name="close-circle-outline"></ion-icon>
            </span>
          </span>
        ) : globals.globalStore.state.editMode && this.thread.owning_person === globals.globalStore.state.userId ? (
          <span>
            <span
              class="update-icon thread-icon"
              onClick={e => {
                e.stopPropagation();
                this.updateMode = true;
              }}
            >
              <ion-icon name="create-outline"></ion-icon>
            </span>
            <span
              class="delete-icon thread-icon"
              onClick={e => {
                e.stopPropagation();
                this.threadDeleted.emit(this.thread.id);
              }}
            >
              <ion-icon name="trash-outline" class="delete-icon"></ion-icon>
            </span>
          </span>
        ) : null}
      </span>
    );
    const threadHeaderJSX = (
      <div
        // onMouseEnter={e => {
        //   e.stopPropagation();
        //   this.showEditAndDeleteButtons = !this.showEditAndDeleteButtons;
        //   // if (timer) clearTimeout(timer);
        //   // timer = setTimeout(this.mouseEnterAndLeaveHandler.bind(this), 50);
        // }}
        // onMouseLeave={e => {
        //   this.showEditAndDeleteButtons = !this.showEditAndDeleteButtons;
        //   e.stopPropagation();
        //   // if (timer) clearTimeout(timer);
        //   // timer = setTimeout(this.mouseEnterAndLeaveHandler.bind(this), 50);
        // }}
        class="thread-header"
      >
        <span
          class="post-indicator thread-icon"
          onClick={e => {
            e.stopPropagation();
            this.showPosts = !this.showPosts;
          }}
        >
          {this.showPosts ? <ion-icon name="arrow-down-circle-outline"></ion-icon> : <ion-icon name="arrow-forward-circle-outline"></ion-icon>}
        </span>
        {this.updateMode ? <cf-tiny initialHTMLContent={this.threadContent} uid={this.tinyMceId} /> : <span class="thread-content" innerHTML={this.threadContent}></span>}
        {editAndDeleteButtons}
      </div>
    );

    return (
      <Host>
        <slot></slot>
        {threadHeaderJSX}
        <div class="thread-posts">{this.showPosts && this.threadPosts?.map(post => <slack-post post={post} />)}</div>
        {this.showPosts && <slack-form type="post" selectedThreadId={this.thread.id} />}
      </Host>
    );
  }
}

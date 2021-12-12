import { Component, Host, h, Prop, Watch, Event, EventEmitter, Listen, State } from '@stencil/core';
import { v4 } from 'uuid';
import { ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { idService } from '../../../core/id.service';
import { TinyUpdateEvent } from '../../cf-tiny/types';
import { CommonPost, CommonPostsUpdateRequest, CommonPostsUpdateResponse } from '../../tables/common/posts/types';

// will take discussion channels as a prop
@Component({
  tag: 'slack-post',
  styleUrl: 'slack-page.css',
  shadow: true,
})
export class SlackThread {
  tinyMceId: string = idService.getNextId();
  @Prop() post: CommonPost;
  @State() showEditAndDeleteButtons: boolean = false;
  @State() updateMode: boolean = false;
  @State() postContent: string = null;
  @Event({ eventName: 'postDeleted' }) postDeleted: EventEmitter<number>;

  componentWillLoad() {
    this.postContent = this.post.content;
  }

  @Listen('contentUpdate')
  handleContentUpdateChange(e: CustomEvent<TinyUpdateEvent>) {
    if (e.detail.id === this.tinyMceId) this.postContent = e.detail.content;
  }

  mouseEnterAndLeaveHandler() {
    if (this.post.owning_person === globals.globalStore.state.userId) this.showEditAndDeleteButtons = !this.showEditAndDeleteButtons;
  }

  render() {
    const slackThreadButtonsClass = this.updateMode === false ? 'thread-buttons' : 'thread-buttons thread-buttons-update';
    const editAndDeleteButtons = (
      <span class={slackThreadButtonsClass}>
        {this.updateMode ? (
          <span>
            <span
              class="thread-update-confirm"
              onClick={async e => {
                e.stopPropagation();
                this.updateMode = false;
                const updateResponse = await fetchAs<CommonPostsUpdateRequest, CommonPostsUpdateResponse>('common-posts/update-read', {
                  token: globals.globalStore.state.token,
                  column: 'content',
                  id: this.post.id,
                  value: this.postContent !== '' ? this.postContent : this.post.content,
                });
                if (updateResponse.error === ErrorType.NoError) {
                  this.postContent = updateResponse.post.content;
                }
              }}
            >
              <ion-icon name="checkmark-circle-outline"></ion-icon>
            </span>
            <span
              class="thread-update-cancel"
              onClick={e => {
                e.stopPropagation();
                this.updateMode = false;
                this.postContent = this.post.content;
              }}
            >
              <ion-icon name="close-circle-outline"></ion-icon>
            </span>
          </span>
        ) : globals.globalStore.state.editMode ? (
          <span>
            <span
              class="thread-update"
              onClick={e => {
                e.stopPropagation();
                this.updateMode = true;
              }}
            >
              <ion-icon name="create-outline"></ion-icon>
            </span>
            <span
              class="thread-delete"
              onClick={e => {
                e.stopPropagation();
                this.postDeleted.emit(this.post.id);
              }}
            >
              <ion-icon name="trash-outline" class="delete-icon"></ion-icon>
            </span>
          </span>
        ) : null}
      </span>
    );

    const jsx = (
      <div
        // onMouseEnter={() => {
        //   setTimeout(this.mouseEnterAndLeaveHandler.bind(this), 100);
        // }}
        // onMouseLeave={() => {
        //   setTimeout(this.mouseEnterAndLeaveHandler.bind(this), 100);
        // }}
        class="thread-header"
      >
        {this.updateMode ? <cf-tiny initialHTMLContent={this.postContent} uid={this.tinyMceId} /> : <span class="slack-thread-content" innerHTML={this.postContent}></span>}
        {editAndDeleteButtons}
      </div>
    );

    return (
      <Host>
        <slot></slot>
        {jsx}
      </Host>
    );
  }
}

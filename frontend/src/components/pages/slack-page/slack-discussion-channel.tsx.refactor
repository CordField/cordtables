import { Component, Host, h, Prop, Watch, Event, EventEmitter, Listen, State } from '@stencil/core';
import { v4 } from 'uuid';
import { ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { idService } from '../../../core/id.service';
import { CommonDiscussionChannel, CommonDiscussionChannelUpdateRequest, CommonDiscussionChannelUpdateResponse } from '../../tables/common/discussion-channels/types';

// will take discussion channels as a prop
@Component({
  tag: 'slack-discussion-channel',
  styleUrl: 'slack-page.css',
  shadow: true,
})
export class SlackDiscussionChannel {
  @Prop() discussionChannel: CommonDiscussionChannel;
  @Prop() selectedDiscussionChannel: CommonDiscussionChannel;
  @State() showEditAndDeleteButtons: boolean = false;
  @State() channelName: string;
  @State() mode: 'none' | 'update' | 'delete' = 'none';
  @Event({ eventName: 'channelDeleted' }) channelDeleted: EventEmitter<string>;
  @Prop() discussionChannelClassName: string;

  @Event({ eventName: 'channelClicked' }) channelClicked: EventEmitter<string>;

  componentWillLoad() {
    console.log(this.discussionChannel);
    this.channelName = this.discussionChannel.name;
  }
  clickHandler() {
    console.log(this.selectedDiscussionChannel, this.discussionChannel);
    if (this.selectedDiscussionChannel.id !== this.discussionChannel.id) this.channelClicked.emit(this.discussionChannel.id);
  }

  render() {
    const slackThreadButtonsClass = this.mode === 'none' ? 'thread-buttons' : 'thread-buttons thread-buttons-update';
    const editAndDeleteButtons = (
      <span class={slackThreadButtonsClass}>
        {this.mode !== 'none' ? (
          <span>
            <span
              class="confirm-icon slack-icon"
              onClick={async e => {
                e.stopPropagation();
                if (this.mode === 'update') {
                  const updateResponse = await fetchAs<CommonDiscussionChannelUpdateRequest, CommonDiscussionChannelUpdateResponse>('common-discussion-channels/update-read', {
                    token: globals.globalStore.state.token,
                    column: 'name',
                    id: this.discussionChannel.id,
                    value: this.channelName !== '' ? this.channelName : this.discussionChannel.name,
                  });
                  if (updateResponse.error === ErrorType.NoError) {
                    console.log(updateResponse);
                    this.channelName = updateResponse.discussion_channel.name;
                  }
                } else {
                  this.channelDeleted.emit(this.discussionChannel.id);
                }
                this.mode = 'none';
              }}
            >
              <ion-icon name="checkmark-circle-outline"></ion-icon>
            </span>
            <span
              class="cancel-icon slack-icon"
              onClick={e => {
                e.stopPropagation();
                this.mode = 'none';
                this.channelName = this.discussionChannel.name;
              }}
            >
              <ion-icon name="close-circle-outline"></ion-icon>
            </span>
          </span>
        ) : globals.globalStore.state.editMode && this.discussionChannel.owning_person === globals.globalStore.state.userId ? (
          <span>
            <span
              class="thread-update"
              onClick={e => {
                e.stopPropagation();
                this.mode = 'update';
              }}
            >
              <ion-icon name="create-outline"></ion-icon>
            </span>
            <span
              class="thread-delete"
              onClick={e => {
                this.mode = 'delete';
              }}
            >
              <ion-icon name="trash-outline" class="delete-icon"></ion-icon>
            </span>
          </span>
        ) : null}
      </span>
    );

    return (
      <Host>
        <slot></slot>

        {this.mode === 'update' ? (
          <form>
            <input type="text" value={this.channelName} onChange={e => (this.channelName = (e.target as HTMLInputElement).value)} />
          </form>
        ) : (
          <div slot="name" class={this.discussionChannelClassName} onClick={this.clickHandler.bind(this)} key={this.discussionChannel.id}>
            {this.channelName}
          </div>
        )}
        {editAndDeleteButtons}
      </Host>
    );
  }
}

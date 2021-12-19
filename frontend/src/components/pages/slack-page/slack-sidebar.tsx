import { Component, Host, h, Prop, Watch, Event, EventEmitter, State, Listen } from '@stencil/core';
import { ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import {
  CommonDiscussionChannel,
  CommonDiscussionChannelListResponse,
  CreateCommonDiscussionChannelRequest,
  CreateCommonDiscussionChannelResponse,
} from '../../tables/common/discussion-channels/types';
import { v4 as uuidv4 } from 'uuid';

@Component({
  tag: 'slack-sidebar',
  styleUrl: 'slack-page.css',
  shadow: true,
})
export class SlackSidebar {
  @Prop({ mutable: true }) discussionChannels: CommonDiscussionChannelListResponse;
  @Event({ eventName: 'channelSelected' }) channelSelected: EventEmitter<CommonDiscussionChannel>;
  @State() selectedDiscussionChannel: CommonDiscussionChannel;
  @State() name: string = '';
  @State() showForm: boolean = false;

  componentWillLoad() {
    this.selectedDiscussionChannel = this.discussionChannels?.discussion_channels.length > 0 ? this.discussionChannels.discussion_channels[0] : null;
    this.channelSelected.emit(this.selectedDiscussionChannel);
  }
  @Listen('channelClicked')
  handleChannelClicked(e: CustomEvent<number>) {
    console.log(e, this.discussionChannels);
    this.selectedDiscussionChannel = this.discussionChannels.discussion_channels.find(discussion_channel => discussion_channel?.id === e.detail);
    this.channelSelected.emit(this.selectedDiscussionChannel);
    console.log(this.selectedDiscussionChannel);
  }
  handleNameChange(e) {
    this.name = e.target.value;
  }
  setNameToNull() {
    this.name = '';
  }
  async handleCreate(e) {
    e.preventDefault();
    if (this.name === '') {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'cannot create empty discussion channel', id: uuidv4(), type: 'error' });
      return;
    }

    const createResponse = await fetchAs<CreateCommonDiscussionChannelRequest, CreateCommonDiscussionChannelResponse>('common-discussion-channels/create-read', {
      token: globals.globalStore.state.token,
      discussion_channel: {
        name: this.name,
      },
    });
    if (createResponse.error === ErrorType.NoError) {
      this.discussionChannels = { ...this.discussionChannels, discussion_channels: this.discussionChannels?.discussion_channels.concat(createResponse.discussion_channel) };
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: createResponse.error, id: uuidv4(), type: 'error' });
    }
  }

  render() {
    const jsx = this.discussionChannels ? (
      this.discussionChannels.discussion_channels?.map(discussionChannel => {
        const discussionChannelClassName = `discussion-channel ${discussionChannel.id === this.selectedDiscussionChannel?.id ? 'selected-discussion-channel' : ''}`;
        return (
          <slack-discussion-channel
            selectedDiscussionChannel={this.selectedDiscussionChannel}
            discussionChannel={discussionChannel}
            key={discussionChannel.id}
            discussionChannelClassName={discussionChannelClassName}
          ></slack-discussion-channel>
        );
      })
    ) : (
      <div>Loading...</div>
    );
    const formJsx = (
      <form class="sidebar-form form">
        <input type="text" name="name" id="name" value={this.name} onChange={e => this.handleNameChange(e)} />
        <button
          onClick={e => {
            this.handleCreate(e);
            this.setNameToNull();
          }}
          class="form-button channel-button"
        >
          submit
        </button>
      </form>
    );
    return (
      <Host>
        <slot></slot>
        <span
          class="add-button"
          onClick={() => {
            this.showForm = !this.showForm;
          }}
        >
          {this.showForm ? <ion-icon name="remove-circle-outline"></ion-icon> : <ion-icon name="add-circle-outline"></ion-icon>}
        </span>
        {this.showForm && formJsx}
        {jsx}
      </Host>
    );
  }
}

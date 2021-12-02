import { Component, Host, h, Prop, Watch, Event, EventEmitter, State, Listen } from '@stencil/core';
import { ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { DiscussionChannels } from '../../tables/common/discussion-channels/discussion-channels';
import {
  CommonDiscussionChannel,
  CommonDiscussionChannelListResponse,
  CreateCommonDiscussionChannelRequest,
  CreateCommonDiscussionChannelResponse,
} from '../../tables/common/discussion-channels/types';
import { v4 as uuidv4 } from 'uuid';

// will take discussion channels as a prop
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
  // @Prop({ mutable: true }) selectedDiscussionChannel: CommonDiscussionChannel;
  @Listen('channelAdded')
  handleChannelAddedChange(event: CustomEvent<CommonDiscussionChannel>) {
    // if (this.discussionChannels.error === ErrorType.NoError) {
    //   this.channelThreads = this.channelThreads.concat(event.detail);
    // }
    if (this.discussionChannels.error === ErrorType.NoError) {
      this.discussionChannels = { error: ErrorType.NoError, discussion_channels: this.discussionChannels.discussion_channels.concat(event.detail) };
    }
  }
  componentWillLoad() {
    console.log('component did load', this.discussionChannels);
    this.selectedDiscussionChannel = this.discussionChannels?.discussion_channels.length > 0 ? this.discussionChannels.discussion_channels[0] : null;
    this.channelSelected.emit(this.selectedDiscussionChannel);
  }

  clickHandler(e) {
    this.selectedDiscussionChannel = this.discussionChannels.discussion_channels.find(discussion_channel => discussion_channel.name === e.target.innerText);
    console.log(this.selectedDiscussionChannel);
    this.channelSelected.emit(this.selectedDiscussionChannel);
  }
  handleNameChange(e) {
    this.name = e.target.value;
  }
  setNameToNull() {
    this.name = '';
  }
  async handleCreate(e) {
    e.preventDefault();
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
          <div class={discussionChannelClassName} onClick={e => this.clickHandler(e)} key={discussionChannel.id}>
            {discussionChannel.name}
          </div>
        );
      })
    ) : (
      <div>Loading...</div>
    );
    const formJsx = (
      <form class="slack-form-last">
        <input type="text" name="name" id="name" value={this.name} onChange={e => this.handleNameChange(e)} />
        <button
          onClick={e => {
            this.handleCreate(e);
            this.setNameToNull();
          }}
          class="slack-form-button"
        >
          submit
        </button>
      </form>
    );
    return (
      <Host>
        <slot></slot>
        {jsx}
        {/* {formJsx} */}
        <div
          class="add-button"
          onClick={() => {
            this.showForm = !this.showForm;
          }}
        >
          {this.showForm ? '\u2212' : '\u002B'}
        </div>
        {/* {this.showForm && <slack-form type="channel" />} */}
        {this.showForm && formJsx}
      </Host>
    );
  }
}

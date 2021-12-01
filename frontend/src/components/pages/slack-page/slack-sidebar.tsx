import { Component, Host, h, Prop, Watch, Event, EventEmitter, State } from '@stencil/core';
import { ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { DiscussionChannels } from '../../tables/common/discussion-channels/discussion-channels';
import {
  CommonDiscussionChannel,
  CommonDiscussionChannelListResponse,
  CreateDiscussionChannelRequest,
  CreateDiscussionChannelResponse,
} from '../../tables/common/discussion-channels/types';
import { v4 as uuidv4 } from 'uuid';

// will take discussion channels as a prop
@Component({
  tag: 'slack-sidebar',
  styleUrl: 'slack-page.css',
  shadow: true,
})
export class SlackSidebar {
  @Prop() discussionChannels: CommonDiscussionChannelListResponse;
  @Event({ eventName: 'channelSelected' }) channelSelected: EventEmitter<CommonDiscussionChannel>;
  @State() selectedDiscussionChannel: CommonDiscussionChannel;
  @State() name: string = '';
  // @Prop({ mutable: true }) selectedDiscussionChannel: CommonDiscussionChannel;

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
    const createResponse = await fetchAs<CreateDiscussionChannelRequest, CreateDiscussionChannelResponse>('common-discussion_channels/create-read', {
      token: globals.globalStore.state.token,
      discussion_channels: {
        name: this.name,
      },
    });
    if (createResponse.error === ErrorType.NoError) {
      this.discussionChannels = { ...this.discussionChannels, discussion_channels: this.discussionChannels?.discussion_channels.concat(createResponse.discussion_channels) };
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: createResponse.error, id: uuidv4(), type: 'error' });
    }
  }

  render() {
    const jsx = this.discussionChannels ? (
      this.discussionChannels.discussion_channels?.map(discussionChannel => {
        const discussionChannelClassName = `discussion-channel ${discussionChannel.id === this.selectedDiscussionChannel?.id ? 'selected-discussion-channel' : ''}`;
        return (
          <div class={discussionChannelClassName} onClick={e => this.clickHandler(e)}>
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
      </Host>
    );
  }
}

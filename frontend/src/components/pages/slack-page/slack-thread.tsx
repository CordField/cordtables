import { Component, Host, h, Prop, Watch, Event, EventEmitter, Listen, State } from '@stencil/core';
import { ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { CommonDiscussionChannel } from '../../tables/common/discussion-channels/types';
import { CommonPost, CommonPostsListRequest, CommonPostsListResponse } from '../../tables/common/posts/types';
import { CommonThread, CommonThreadsListRequest, CommonThreadsListResponse } from '../../tables/common/threads/types';

// will take discussion channels as a prop
@Component({
  tag: 'slack-thread',
  styleUrl: 'slack-page.css',
  shadow: true,
})
export class SlackThread {
  @Prop() thread: CommonThread;
  @State() threadPostsResponse: CommonPostsListResponse = null;
  @State() threadPosts: CommonPost[];
  @State() showPosts: boolean = false;
  @State() showButtons: boolean = false;
  @Watch('showPosts')
  async handleShowPostsChange(newValue: boolean, oldValue: boolean) {
    if (newValue === true && this.threadPostsResponse === null) {
      await this.getPosts();
    }
  }
  // componentDidLoad() {
  //   console.log('slackthread', this.thread);
  // }
  async getPosts() {
    this.threadPostsResponse = await fetchAs<CommonPostsListRequest, CommonPostsListResponse>('common-posts/list', {
      token: globals.globalStore.state.token,
      threadId: this.thread.id,
    });
    if (this.threadPostsResponse.error === ErrorType.NoError) {
      this.threadPosts = this.threadPostsResponse.posts;
    }
  }
  @Listen('postAdded')
  postAddedHandler(event: CustomEvent<CommonPost>) {
    console.log('event received', event);
    this.threadPosts = this.threadPosts.concat(event.detail);
  }
  mouseEnterAndLeaveHandler() {
    console.log(this);
    if (this.thread.owning_person === globals.globalStore.state.userId) this.showButtons = !this.showButtons;
  }

  render() {
    // figure out a way to make this cleaner code
    console.log('render', this);
    const jsx = (
      <div>
        <div
          onClick={() => {
            this.showPosts = !this.showPosts;
          }}
          onMouseEnter={() => {
            setTimeout(this.mouseEnterAndLeaveHandler.bind(this), 100);
          }}
          onMouseLeave={() => {
            setTimeout(this.mouseEnterAndLeaveHandler.bind(this), 100);
          }}
          class="slack-thread-content"
        >
          <span class="post-indicator">{this.showPosts ? <span>&#128071;</span> : <span>&#x261e;</span>}</span>
          {this.thread.content}
          {/* <span class="post-count">{this.threadPosts.length}</span> */}
          {this.showButtons && (
            <span class="slack-thread-buttons">
              <span class="slack-thread-update" onClick={e => {}}>
                &#9998;
              </span>
              <span class="slack-thread-delete">&#9940;</span>
            </span>
          )}
        </div>
        {/* <span class="form-indicator" onClick={() => (this.showForm = !this.showForm)}>
          {this.showForm === false ? <span>&#43;</span> : <span>&#8722;</span>}
        </span> */}
        {/* add button here to toggle visibility */}
        <div class="thread-posts">{this.showPosts && this.threadPosts?.map(post => <div>{post.content}</div>)}</div>
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

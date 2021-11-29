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
  @Prop({ mutable: true }) threadPosts: CommonPost[];
  @Prop() thread: CommonThread;
  @State() showPosts: Boolean = false;
  // @State() showForm: Boolean = false;
  componentDidLoad() {
    console.log('slackthread', this.thread);
  }
  @Listen('postAdded')
  postAddedHandler(event: CustomEvent<CommonPost>) {
    console.log('event received', event);
    this.threadPosts = this.threadPosts.concat(event.detail);
  }

  render() {
    // figure out a way to make this cleaner code
    const jsx = (
      <div>
        <div
          onClick={() => {
            this.showPosts = !this.showPosts;
          }}
          class="slack-thread-content"
        >
          <span class="post-indicator">{this.showPosts ? <span>&#128071;</span> : <span>&#x261e;</span>}</span>
          {this.thread.content} <span class="post-count">{this.threadPosts.length}</span>
        </div>
        {/* <span class="form-indicator" onClick={() => (this.showForm = !this.showForm)}>
          {this.showForm === false ? <span>&#43;</span> : <span>&#8722;</span>}
        </span> */}
        {/* add button here to toggle visibility */}
        <div class="thread-posts">{this.showPosts && this.threadPosts.map(post => <div>{post.content}</div>)}</div>
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

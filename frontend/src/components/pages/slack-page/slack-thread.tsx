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
  @Prop() threadPosts: CommonPost[];
  @Prop() thread: CommonThread;
  @State() showPosts: Boolean = false;
  componentDidLoad() {
    console.log('slackthread', this.thread);
  }
  // togglePostsVisibility() {
  //   console.log(this.showPosts);
  //   if (this.showPosts === true) this.showPosts = false;
  //   else this.showPosts = true;
  // }

  render() {
    const jsx = (
      <div>
        <div
          onClick={() => {
            this.showPosts = !this.showPosts;
          }}
        >
          {this.thread.content}
        </div>
        {/* add button here to toggle visibility */}
        <div class="thread-posts">{this.showPosts && this.threadPosts.map(post => <div>{post.content}</div>)}</div>
      </div>
    );

    return (
      <Host class="slack-thread">
        <slot></slot>
        {jsx}
      </Host>
    );
  }
}

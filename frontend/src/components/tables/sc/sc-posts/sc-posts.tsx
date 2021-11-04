import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-posts',
  styleUrl: 'sc-posts.css',
  shadow: true,
})
export class ScPosts {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

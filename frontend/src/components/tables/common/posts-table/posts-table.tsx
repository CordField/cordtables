import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'posts-table',
  styleUrl: 'posts-table.css',
  shadow: true,
})
export class PostsTable {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

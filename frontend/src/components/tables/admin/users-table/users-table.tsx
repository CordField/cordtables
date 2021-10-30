import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'users-table',
  styleUrl: 'users-table.css',
  shadow: true,
})
export class UsersTable {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

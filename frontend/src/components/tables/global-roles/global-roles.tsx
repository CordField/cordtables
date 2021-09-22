import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'global-roles',
  styleUrl: 'global-roles.css',
  shadow: true,
})
export class GlobalRoles {
  render() {
    return (
      <Host>
        <slot></slot>
        <h1>Global Roles</h1>
      </Host>
    );
  }
}

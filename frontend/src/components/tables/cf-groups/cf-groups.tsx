import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'cf-groups',
  styleUrl: 'cf-groups.css',
  shadow: true,
})
export class CfGroups {
  render() {
    return (
      <Host>
        <slot></slot>
        <h3>Groups</h3>
      </Host>
    );
  }
}

import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'stage-role-column-grants',
  styleUrl: 'stage-role-column-grants.css',
  shadow: true,
})
export class StageRoleColumnGrants {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

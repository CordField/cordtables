import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'organizations-page',
  styleUrl: 'organizations-page.css',
  shadow: true,
})
export class OrganizationsPage {
  render() {
    return (
      <Host>
        <slot></slot>
        Organizations Page
      </Host>
    );
  }
}

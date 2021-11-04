import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-organizations',
  styleUrl: 'sc-organizations.css',
  shadow: true,
})
export class ScOrganizations {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

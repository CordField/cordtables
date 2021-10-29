import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-partners',
  styleUrl: 'sc-partners.css',
  shadow: true,
})
export class ScPartners {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

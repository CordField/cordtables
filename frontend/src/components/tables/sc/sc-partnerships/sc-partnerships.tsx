import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-partnerships',
  styleUrl: 'sc-partnerships.css',
  shadow: true,
})
export class ScPartnerships {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

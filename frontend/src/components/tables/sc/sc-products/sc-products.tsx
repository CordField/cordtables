import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-products',
  styleUrl: 'sc-products.css',
  shadow: true,
})
export class ScProducts {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

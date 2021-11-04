import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-product-scripture-references',
  styleUrl: 'sc-product-scripture-references.css',
  shadow: true,
})
export class ScProductScriptureReferences {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

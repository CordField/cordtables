import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-languages',
  styleUrl: 'sc-languages.css',
  shadow: true,
})
export class ScLanguages {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sil-language-index',
  styleUrl: 'sil-language-index.css',
  shadow: true,
})
export class SilLanguageIndex {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

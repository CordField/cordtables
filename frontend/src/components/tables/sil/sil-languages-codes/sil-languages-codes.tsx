import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sil-languages-codes',
  styleUrl: 'sil-languages-codes.css',
  shadow: true,
})
export class SilLanguagesCodes {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sil-country-codes',
  styleUrl: 'sil-country-codes.css',
  shadow: true,
})
export class SilCountryCodes {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

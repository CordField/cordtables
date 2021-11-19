import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sil-table-of-languages',
  styleUrl: 'sil-table-of-languages.css',
  shadow: true,
})
export class ScEthnologue {
  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }
}

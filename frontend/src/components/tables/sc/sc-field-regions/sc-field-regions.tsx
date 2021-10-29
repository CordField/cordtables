import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-field-regions',
  styleUrl: 'sc-field-regions.css',
  shadow: true,
})
export class ScFieldRegions {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

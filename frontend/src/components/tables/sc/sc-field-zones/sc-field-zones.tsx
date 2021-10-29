import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-field-zones',
  styleUrl: 'sc-field-zones.css',
  shadow: true,
})
export class ScFieldZones {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

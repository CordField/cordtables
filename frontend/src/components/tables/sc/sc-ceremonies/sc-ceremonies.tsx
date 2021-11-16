import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-ceremonies',
  styleUrl: 'sc-ceremonies.css',
  shadow: true,
})
export class ScCeremonies {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

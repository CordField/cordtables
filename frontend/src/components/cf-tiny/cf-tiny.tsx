import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'cf-tiny',
  styleUrl: 'cf-tiny.css',
  shadow: true,
})
export class CfTiny {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

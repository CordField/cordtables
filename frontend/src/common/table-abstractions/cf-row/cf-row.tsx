import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'cf-row',
  styleUrl: 'cf-row.css',
  shadow: true,
})
export class CfRow {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'cf-table',
  styleUrl: 'cf-table.css',
  shadow: true,
})
export class CfTable {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

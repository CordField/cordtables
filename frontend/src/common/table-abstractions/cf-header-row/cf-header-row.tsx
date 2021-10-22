import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'cf-header-row',
  styleUrl: 'cf-header-row.css',
  shadow: true,
})
export class CfHeaderRow {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

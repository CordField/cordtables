import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'cf-header-cell',
  styleUrl: 'cf-header-cell.css',
  shadow: true,
})
export class CfHeaderCell {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

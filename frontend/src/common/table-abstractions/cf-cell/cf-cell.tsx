import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'cf-cell2',
  styleUrl: 'cf-cell.css',
  shadow: true,
})
export class CfCell {
  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }
}

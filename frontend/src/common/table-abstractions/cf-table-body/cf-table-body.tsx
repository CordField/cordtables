import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'cf-table-body',
  styleUrl: 'cf-table-body.css',
  shadow: true,
})
export class CfTableBody {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

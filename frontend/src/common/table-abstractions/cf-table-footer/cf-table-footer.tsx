import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'cf-table-footer',
  styleUrl: 'cf-table-footer.css',
  shadow: true,
})
export class CfTableFooter {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

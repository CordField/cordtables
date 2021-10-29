import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'stages-table',
  styleUrl: 'stages-table.css',
  shadow: true,
})
export class StagesTable {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

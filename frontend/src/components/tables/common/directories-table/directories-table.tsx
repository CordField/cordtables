import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'directories-table',
  styleUrl: 'directories-table.css',
  shadow: true,
})
export class DirectoriesTable {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

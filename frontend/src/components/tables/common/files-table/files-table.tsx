import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'files-table',
  styleUrl: 'files-table.css',
  shadow: true,
})
export class FilesTable {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'organizations-table',
  styleUrl: 'organizations-table.css',
  shadow: true,
})
export class OrganizationsTable {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

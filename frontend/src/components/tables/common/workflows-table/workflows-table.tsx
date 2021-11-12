import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'workflows-table',
  styleUrl: 'workflows-table.css',
  shadow: true,
})
export class WorkflowsTable {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

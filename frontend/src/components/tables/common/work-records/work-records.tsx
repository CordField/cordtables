import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'work-records',
  styleUrl: 'work-records.css',
  shadow: true,
})
export class WorkRecords {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

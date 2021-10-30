import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-budget-records',
  styleUrl: 'sc-budget-records.css',
  shadow: true,
})
export class ScBudgetRecords {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

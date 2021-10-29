import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-budgets',
  styleUrl: 'sc-budgets.css',
  shadow: true,
})
export class ScBudgets {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

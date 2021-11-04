import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-funding-accounts',
  styleUrl: 'sc-funding-accounts.css',
  shadow: true,
})
export class ScFundingAccounts {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-change-to-plan',
  styleUrl: 'sc-change-to-plan.css',
  shadow: true,
})
export class ScChangeToPlan {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

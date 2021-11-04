import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'work-estimates',
  styleUrl: 'work-estimates.css',
  shadow: true,
})
export class WorkEstimates {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

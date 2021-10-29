import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'stage-graph',
  styleUrl: 'stage-graph.css',
  shadow: true,
})
export class StageGraph {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

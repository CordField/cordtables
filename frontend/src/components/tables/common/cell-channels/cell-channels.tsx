import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'cell-channels',
  styleUrl: 'cell-channels.css',
  shadow: true,
})
export class CellChannels {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

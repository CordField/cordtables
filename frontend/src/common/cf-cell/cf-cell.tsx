import { Component, Host, h, Prop } from '@stencil/core';

@Component({
  tag: 'cf-cell',
  styleUrl: 'cf-cell.css',
  shadow: true,
})
export class CfCell {
  @Prop() propKey: keyof any;
  @Prop() value: any;

  connectedCallback() {}

  render() {
    return (
      <Host>
        <slot></slot>
        {this.value}
      </Host>
    );
  }
}

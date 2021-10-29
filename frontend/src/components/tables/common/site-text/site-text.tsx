import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'site-text',
  styleUrl: 'site-text.css',
  shadow: true,
})
export class SiteText {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

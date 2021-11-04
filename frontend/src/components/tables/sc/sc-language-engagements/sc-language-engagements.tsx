import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-language-engagements',
  styleUrl: 'sc-language-engagements.css',
  shadow: true,
})
export class ScLanguageEngagements {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

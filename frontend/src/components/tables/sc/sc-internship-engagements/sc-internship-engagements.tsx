import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-internship-engagements',
  styleUrl: 'sc-internship-engagements.css',
  shadow: true,
})
export class ScInternshipEngagements {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-person-unavailabilities',
  styleUrl: 'sc-person-unavailabilities.css',
  shadow: true,
})
export class ScPersonUnavailabilities {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

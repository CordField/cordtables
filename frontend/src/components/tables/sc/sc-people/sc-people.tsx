import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-people',
  styleUrl: 'sc-people.css',
  shadow: true,
})
export class ScPeople {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

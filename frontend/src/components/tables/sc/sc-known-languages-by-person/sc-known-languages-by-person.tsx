import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-known-languages-by-person',
  styleUrl: 'sc-known-languages-by-person.css',
  shadow: true,
})
export class ScKnownLanguagesByPerson {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

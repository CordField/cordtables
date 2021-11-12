import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-project-locations',
  styleUrl: 'sc-project-locations.css',
  shadow: true,
})
export class ScProjectLocations {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

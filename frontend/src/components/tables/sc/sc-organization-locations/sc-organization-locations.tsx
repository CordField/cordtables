import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-organization-locations',
  styleUrl: 'sc-organization-locations.css',
  shadow: true,
})
export class ScOrganizationLocations {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

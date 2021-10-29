import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'people-to-org-relationships',
  styleUrl: 'people-to-org-relationships.css',
  shadow: true,
})
export class PeopleToOrgRelationships {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'groups-page',
  styleUrl: 'groups-page.css',
  shadow: true,
})
export class GroupsPage {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-pinned-projects',
  styleUrl: 'sc-pinned-projects.css',
  shadow: true,
})
export class ScPinnedProjects {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

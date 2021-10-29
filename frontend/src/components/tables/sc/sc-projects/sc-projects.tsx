import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'sc-projects',
  styleUrl: 'sc-projects.css',
  shadow: true,
})
export class ScProjects {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

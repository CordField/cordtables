import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'education-entries',
  styleUrl: 'education-entries.css',
  shadow: true,
})
export class EducationEntries {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

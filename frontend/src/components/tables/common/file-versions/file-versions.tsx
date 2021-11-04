import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'file-versions',
  styleUrl: 'file-versions.css',
  shadow: true,
})
export class FileVersions {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

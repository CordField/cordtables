import { Component, Host, h } from '@stencil/core';

@Component({
  tag: 'education-by-person',
  styleUrl: 'education-by-person.css',
  shadow: true,
})
export class EducationByPerson {

  render() {
    return (
      <Host>
        <slot></slot>
      </Host>
    );
  }

}

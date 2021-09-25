import { Component, Host, h } from '@stencil/core';


const tableColumns = [
  {
    name: "firstColumn"
  },
  {
    address: "secondColumn"
  }
];

const tableValues = [
  {
    name1: "firstValue",
    address1: "address"
  },
  {
    name2: "secondValue",
    address2: "address2"
  }
];


@Component({
  tag: 'global-role-column-grants',
  styleUrl: 'global-role-column-grants.css',
  shadow: true,
})

export class GlobalRoleColumnGrants {
  render() {
    return (
      <Host>
        <slot></slot>
        <generic-table name="Test" columns={tableColumns} values={tableValues}></generic-table>
      </Host>
    );
  }
}

import { Component, Host, h, Prop } from '@stencil/core';
import { ColumnDescription } from '../types';

@Component({
  tag: 'cf-header-row',
  styleUrl: 'cf-header-row.css',
  shadow: true,
})
export class CfHeaderRow {
  @Prop() columnData: ColumnDescription[];

  render() {
    return (
      <Host>
        <slot></slot>
        <div id="header-row">{this.columnData && this.columnData.map(cell => <cf-cell2 value={cell.displayName} columnDescription={cell} isHeader={true}></cf-cell2>)}</div>
      </Host>
    );
  }
}

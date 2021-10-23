import { Component, Host, h, Prop } from '@stencil/core';
import { ColumnDescription } from '../cf-table/types';

@Component({
  tag: 'cf-row',
  styleUrl: 'cf-row.css',
  shadow: true,
})
export class CfRow {
  @Prop() row: any;
  @Prop() columnData: ColumnDescription[];

  render() {
    return (
      <Host>
        <slot></slot>
        <div id="data-row">
          {this.columnData &&
            this.row &&
            this.columnData.map(columnDescription => <cf-cell2 value={this.row[columnDescription.field]} columnDescription={columnDescription}></cf-cell2>)}
        </div>
      </Host>
    );
  }
}

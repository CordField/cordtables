import { Component, Host, h, State, Prop } from '@stencil/core';
import { ColumnDescription } from '../types';

@Component({
  tag: 'cf-table',
  styleUrl: 'cf-table.css',
  shadow: true,
})
export class CfTable {
  @Prop() columnData: ColumnDescription[];
  @Prop() rowData: any[];

  render() {
    return (
      <Host>
        <slot></slot>
        <div style={{ width: this.columnData.map(col => col.width).reduce((p, c) => p + c) + 1000 + 'px' }}>
          {/* <cf-header-row columnData={this.columnData}></cf-header-row> */}
          <cf-row columnData={this.columnData} row={null}></cf-row>
          <cf-table-body rowData={this.rowData} columnData={this.columnData}></cf-table-body>
          <cf-table-footer></cf-table-footer>
        </div>
      </Host>
    );
  }
}

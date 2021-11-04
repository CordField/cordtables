import { Component, Host, h, State, Prop } from '@stencil/core';
import { globals } from '../../../core/global.store';
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
        <div style={{ width: this.columnData.map(col => col.width).reduce((p, c) => p + c + 19 + globals.globalStore.state.editModeWidth) + 120 + 'px' }}>
          <cf-row columnData={this.columnData} row={null}></cf-row>
          <cf-table-body rowData={this.rowData} columnData={this.columnData}></cf-table-body>
          <cf-table-footer></cf-table-footer>
        </div>
      </Host>
    );
  }
}

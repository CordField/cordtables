import { Component, Host, h, Prop } from '@stencil/core';
import { ColumnDescription } from '../types';

@Component({
  tag: 'cf-table-body',
  styleUrl: 'cf-table-body.css',
  shadow: true,
})
export class CfTableBody {
  @Prop() columnData: ColumnDescription[];
  @Prop() rowData: any[];

  render() {
    return (
      <Host>
        <slot></slot>
        {this.rowData && this.rowData.map(row => <cf-row row={row} columnData={this.columnData}></cf-row>)}
      </Host>
    );
  }
}

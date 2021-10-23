import { Component, Host, h, Prop, State } from '@stencil/core';
import { globals } from '../../../core/global.store';
import { ColumnDescription } from '../types';

@Component({
  tag: 'cf-row',
  styleUrl: 'cf-row.css',
  shadow: true,
})
export class CfRow {
  @Prop() row: any;
  @Prop() columnData: ColumnDescription[];

  @State() processedColumnData: ColumnDescription[];

  render() {
    return (
      <Host>
        <slot></slot>
        {(this.row === null || this.row === undefined) && (
          <div id="header-row">
            {this.columnData &&
              this.columnData.map(columnDescription => <cf-cell2 rowId={0} value={columnDescription.displayName} columnDescription={columnDescription} isHeader={true}></cf-cell2>)}
          </div>
        )}
        <div id="data-row">
          {this.columnData &&
            this.row &&
            this.columnData.map(columnDescription => <cf-cell2 rowId={this.row.id} value={this.row[columnDescription.field]} columnDescription={columnDescription}></cf-cell2>)}
        </div>
      </Host>
    );
  }
}

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

  actionColumn: ColumnDescription = {
    field: 'Actions',
    displayName: 'Actions',
    editable: false,
    width: 50,
  };

  render() {
    return (
      <Host>
        <slot></slot>
        {(this.row === null || this.row === undefined) && (
          <div id="header-row">
            {/* {globals.globalStore.state.editMode === true && <cf-cell2 rowId={0} value={'Actions'} columnDescription={this.actionColumn} cellType={'header'}></cf-cell2>} */}

            {this.columnData &&
              this.columnData.map(columnDescription => (
                <cf-cell2 rowId={''} value={columnDescription.displayName} columnDescription={columnDescription} cellType={'header'}></cf-cell2>
              ))}
          </div>
        )}
        <div id="data-row">
          {/* {globals.globalStore.state.editMode === true && this.row && this.row.id && (
            <cf-cell2 rowId={this.row.id} columnDescription={this.actionColumn} cellType={'action'}></cf-cell2>
          )} */}

          {this.columnData &&
            this.row &&
            this.columnData.map(columnDescription => {
              // console.group();
              // console.log(typeof this.row[columnDescription.field]);
              console.log(columnDescription.field);
              console.log(this.row[columnDescription.field]);
              // console.log(columnDescription.field +' : '+ this.row[columnDescription.field]?.value);
              // console.log(this.row[columnDescription.field]?.displayValue);
              // console.groupEnd();
              if (typeof this.row[columnDescription.field] === 'string') {
                return <cf-cell2 rowId={this.row.id} value={this.row[columnDescription.field]} columnDescription={columnDescription} displayValue={''}></cf-cell2>;
              } else {
                // if (this.row[columnDescription.field] !== null && this.row[columnDescription.field] !== undefined) {
                return (
                  <cf-cell2
                    rowId={this.row.id}
                    value={this.row[columnDescription.field]?.value}
                    displayValue={this.row[columnDescription.field]?.displayValue}
                    columnDescription={columnDescription}
                  ></cf-cell2>
                );
                // }
                // console.log('case3');
                // return <cf-cell2 rowId={this.row.id} value={null} displayValue={null} columnDescription={columnDescription} />;
              }
            })}
        </div>
      </Host>
    );
  }
}

import { Component, Host, h, Prop } from '@stencil/core';
import { ColumnDescription } from '../cf-table/types';

@Component({
  tag: 'cf-cell2',
  styleUrl: 'cf-cell.css',
  shadow: true,
})
export class CfCell {
  @Prop() value: any;
  @Prop() columnDescription: ColumnDescription;
  @Prop() isHeader = false;

  render() {
    return (
      <Host>
        <slot></slot>
        <div class={this.isHeader ? 'header both' : 'data both'} style={{ width: this.columnDescription.width + 'px' }}>
          {this.value && this.value.toString()}
        </div>
      </Host>
    );
  }
}

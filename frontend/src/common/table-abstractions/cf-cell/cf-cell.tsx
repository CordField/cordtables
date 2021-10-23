import { Component, Host, h, Prop, State } from '@stencil/core';
import { globals } from '../../../core/global.store';
import { ColumnDescription } from '../types';

@Component({
  tag: 'cf-cell2',
  styleUrl: 'cf-cell.css',
  shadow: true,
})
export class CfCell {
  @Prop() value: any;
  @Prop() columnDescription: ColumnDescription;
  @Prop() isHeader = false;

  @State() showEdit = false;

  clickEdit = () => {
    if (this.columnDescription.editable) this.showEdit = !this.showEdit;
  };

  render() {
    return (
      <Host>
        <slot></slot>
        <span id="value-view" class={this.isHeader ? 'header both' : 'data both'} style={{ width: this.columnDescription.width + 'px' }}>
          {!this.showEdit && (
            <span>
              <span>{this.value && this.value.toString()}</span>
            </span>
          )}
          {!this.isHeader && globals.globalStore.state.editMode && this.columnDescription.editable && !this.showEdit && (
            <span class="edit-buttons" onClick={this.clickEdit}>
              <ion-icon name="create-outline"></ion-icon>
            </span>
          )}
          {this.showEdit && <span></span>}
        </span>
      </Host>
    );
  }
}

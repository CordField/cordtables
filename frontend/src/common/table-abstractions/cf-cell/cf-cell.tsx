import { Component, Host, h, Prop, State } from '@stencil/core';
import { globals } from '../../../core/global.store';
import { ColumnDescription } from '../types';

@Component({
  tag: 'cf-cell2',
  styleUrl: 'cf-cell.css',
  shadow: true,
})
export class CfCell {
  @Prop() rowId: number;
  @Prop() value: any;
  @Prop() columnDescription: ColumnDescription;
  @Prop() isHeader = false;

  @State() showEdit = false;
  @State() newValue: any;

  @State() width: number;

  connectedCallback() {
    this.newValue = this.value;
  }

  clickEdit = () => {
    if (this.columnDescription.editable) this.showEdit = !this.showEdit;
  };

  updateValue = async event => {
    this.newValue = event.target.value;
  };

  handleSelect(event) {
    this.newValue = event.target.value;
  }

  submit = async () => {
    if (this.newValue === undefined) return;
    const result = await this.columnDescription.updateFn(this.rowId, this.columnDescription.field, this.newValue);
    if (result) {
      this.value = this.newValue;
      this.showEdit = false;
    } else {
    }
  };

  render() {
    return (
      <Host>
        <slot></slot>
        <span id="value-view" class={this.isHeader ? 'header both' : 'data both'} style={{ width: this.columnDescription.width + globals.globalStore.state.editModeWidth + 'px' }}>
          {!this.showEdit && (
            <span>
              <span>
                {this.value && typeof this.value != 'boolean' && this.value.toString()}
                {typeof this.value == 'boolean' && this.value.toString()} &nbsp;
              </span>
            </span>
          )}
          {!this.isHeader && globals.globalStore.state.editMode && this.columnDescription.editable && !this.showEdit && (
            <span class="edit-buttons" onClick={this.clickEdit}>
              <ion-icon name="create-outline"></ion-icon>
            </span>
          )}
          {this.showEdit && (
            <span id="value-edit">
              <span>
                {this.columnDescription.selectOptions != null || this.columnDescription.selectOptions != undefined ? (
                  <select onInput={event => this.handleSelect(event)}>
                    {this.columnDescription.selectOptions &&
                      this.columnDescription.selectOptions.length > 0 &&
                      this.columnDescription.selectOptions.map(option => (
                        <option value={option.value} selected={this.value === option.value}>
                          {option.display}
                        </option>
                      ))}
                  </select>
                ) : (
                  <input type="text" id="value-input" name="value-input" defaultValue={this.value} onInput={event => this.updateValue(event)}>
                    {this.value}
                  </input>
                )}
              </span>
              <span>
                <span id="save-icon" class="edit-buttons" onClick={this.submit}>
                  <ion-icon name="checkmark-outline"></ion-icon>
                </span>
                <span id="cancel-icon" class="edit-buttons" onClick={this.clickEdit}>
                  <ion-icon name="close-outline"></ion-icon>
                </span>
              </span>
            </span>
          )}
        </span>
      </Host>
    );
  }
}

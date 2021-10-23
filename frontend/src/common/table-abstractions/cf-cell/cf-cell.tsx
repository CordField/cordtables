import { Component, Host, h, Prop, State } from '@stencil/core';
import { globals } from '../../../core/global.store';
import { CellType, ColumnDescription } from '../types';

@Component({
  tag: 'cf-cell2',
  styleUrl: 'cf-cell.css',
  shadow: true,
})
export class CfCell {
  @Prop() rowId: number;
  @Prop() value: any;
  @Prop() columnDescription: ColumnDescription;
  @Prop() cellType: CellType = 'data';

  @State() showEdit = false;
  @State() showDelete = false;
  @State() newValue: any;

  @State() width: number;

  connectedCallback() {
    this.newValue = this.value;
  }

  clickEdit = () => {
    if (this.columnDescription.editable) this.showEdit = !this.showEdit;
  };

  showDeleteConfirm = () => {
    this.showDelete = !this.showDelete;
  };

  updateValue = async event => {
    this.newValue = event.target.value;
  };

  handleSelect(event) {
    this.newValue = event.target.value;
  }

  handleDelete = event => {
    this.columnDescription.deleteFn(this.rowId);
  };

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
        {/* holds entire cell - width is calculated based on base value and edit mode */}
        <span
          id="value-view"
          class={this.cellType === 'header' ? 'header both' : 'data both'}
          style={{ width: this.columnDescription.width + globals.globalStore.state.editModeWidth + 'px' }}
        >
          {/* need to display base on type of value, some types like actions don't like toString() */}
          {!this.showEdit && (this.cellType === 'data' || this.cellType === 'header') && (
            <span id="value-span">
              <span>
                {typeof this.value === 'boolean' && <span>{this.value.toString()}</span>}
                {typeof this.value === 'number' && <span>{this.value.toString()}</span>}
                {typeof this.value === 'string' && <span>{this.value}</span>}
                {typeof this.value === 'object' && <span>{this.value}</span>}
              </span>

              {/* if this is the ID field and edit mode is true, show the delete button */}
              {this.columnDescription && this.columnDescription.field === 'id' && this.cellType === 'data' && globals.globalStore.state.editMode === true && (
                <span>
                  {this.showDelete === false && (
                    <span id="delete-span" onClick={this.showDeleteConfirm}>
                      Delete
                    </span>
                  )}
                  {this.showDelete === true && (
                    <span>
                      <span id="save-icon" class="edit-buttons" onClick={this.handleDelete}>
                        <ion-icon name="checkmark-outline"></ion-icon>
                      </span>
                      <span id="cancel-icon" class="edit-buttons" onClick={this.showDeleteConfirm}>
                        <ion-icon name="close-outline"></ion-icon>
                      </span>
                    </span>
                  )}
                </span>
              )}
            </span>
          )}

          {/* show's the edit icon */}
          {!(this.cellType === 'header') && globals.globalStore.state.editMode && this.columnDescription.editable && !this.showEdit && (
            <span class="edit-buttons" onClick={this.clickEdit}>
              <ion-icon name="create-outline"></ion-icon>
            </span>
          )}

          {/* holds the form and buttons for editing the field */}
          {this.showEdit && (
            <span id="value-edit">
              <span>
                {/* might be a select menu or a text input */}
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

              {/* confirm and cancel buttons */}
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

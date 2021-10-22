import { Component, Host, h, Prop, State } from '@stencil/core';
import { globals } from '../../core/global.store';

@Component({
  tag: 'cf-cell',
  styleUrl: 'cf-cell.css',
  shadow: true,
})
export class CfCell {
  @Prop() rowId: number;
  @Prop() propKey: keyof any;
  @Prop() value: any;
  @Prop() isEditable: boolean;
  @Prop() type?: string;
  @Prop() options?: Array<string>;
  @Prop() updateFn: (id: number, columnName: any, value: any) => Promise<boolean>;

  @State() showEdit = false;
  @State() newValue: any;

  connectedCallback() {}

  clickEdit = () => {
    if (this.isEditable) this.showEdit = !this.showEdit;
  };

  updateValue = async event => {
    this.newValue = event.target.value;
  };

  submit = async () => {
    if (this.newValue === undefined) return;
    const result = await this.updateFn(this.rowId, this.propKey, this.newValue);
    if (result) {
      this.showEdit = false;
    } else {
    }
  };

  handleSelect(event) {
    this.newValue = event.target.value;
  }

  render() {
    return (
      <Host>
        <slot></slot>
        <span class={this.isEditable ? 'editable' : ''}>
          {!this.showEdit && (
            <span id="value-view">
              <span class="value">{this.value}</span>
              {this.isEditable && globals.globalStore.state.editMode && (
                <span class="edit-buttons" onClick={this.clickEdit}>
                  <ion-icon name="create-outline"></ion-icon>
                </span>
              )}
            </span>
          )}
          {this.showEdit && (
            <span id="value-edit">
              {this.type === 'select' ? (
                <select onInput={event => this.handleSelect(event)}>
                  {this.options &&
                    this.options.length > 0 &&
                    this.options.map(option => (
                      <option value={option} selected={this.value === option}>
                        {option}
                      </option>
                    ))}
                </select>
              ) : (
                <input type="text" id="value-input" name="value-input" defaultValue={this.value} onInput={event => this.updateValue(event)}>
                  {this.value}
                </input>
              )}
              <span id="save-icon" class="edit-buttons" onClick={this.submit}>
                <ion-icon name="checkmark-outline"></ion-icon>
              </span>
              <span id="cancel-icon" class="edit-buttons" onClick={this.clickEdit}>
                <ion-icon name="close-outline"></ion-icon>
              </span>
            </span>
          )}
        </span>
      </Host>
    );
  }
}

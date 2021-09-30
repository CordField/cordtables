import { Component, Host, h, Prop, State } from '@stencil/core';

@Component({
  tag: 'cf-cell',
  styleUrl: 'cf-cell.css',
  shadow: true,
})
export class CfCell {
  @Prop() propKey: keyof any;
  @Prop() value: any;
  @Prop() isEditable: boolean;
  @Prop() updateFn: (value: any) => void;
  @State() showEdit = false;
  @State() newValue: any;

  connectedCallback() {}

  clickEdit = () => {
    if (this.isEditable) this.showEdit = !this.showEdit;
  };

  updateValue = event => {
    this.updateFn(event.target.value);
  };

  render() {
    return (
      <Host>
        <slot></slot>
        <span class={this.isEditable ? 'editable' : ''}>
          {!this.showEdit && (
            <span>
              <span>{this.value}</span>
              {this.isEditable && <button onClick={this.clickEdit}>edit</button>}
            </span>
          )}
          {this.showEdit && (
            <span>
              <input type="text" id="value-input" name="value-input" defaultValue={this.value} onInput={event => this.updateValue(event)}>
                {this.value}
              </input>
              <button>Save</button>
              <button onClick={this.clickEdit}>Cancel</button>
            </span>
          )}
        </span>
      </Host>
    );
  }
}

import { Component, Host, h, Prop, State } from '@stencil/core';
import { ActionType } from '../types';

@Component({
  tag: 'cf-action',
  styleUrl: 'cf-action.css',
  shadow: true,
})
export class CfAction {
  @Prop() actionType: ActionType;
  @Prop() value: any;
  @Prop() text: any;
  @Prop() actionFn: (value: any) => Promise<boolean>;
  @State() showConfirm = false;

  toggleConfirm = () => {
    this.showConfirm = !this.showConfirm;
  };

  click = async () => {
    const result = await this.actionFn(this.value);
  };

  render() {
    return (
      <Host>
        <slot></slot>
        {!this.showConfirm && (
          <span id="action-start" onClick={this.toggleConfirm}>
            {this.text}
          </span>
        )}
        {this.showConfirm && (
          <div>
            <span id="cancel-button" class="button" onClick={this.toggleConfirm}>
              Cancel
            </span>
            <span id="confirm-button" class="button" onClick={this.click}>
              Confirm
            </span>
          </div>
        )}
      </Host>
    );
  }
}

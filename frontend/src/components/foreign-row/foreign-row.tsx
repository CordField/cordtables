import { Component, Host, Listen, State, h } from '@stencil/core';
import { foreignKeyClickedObject, GenericResponse, GenericRequest, ErrorType } from '../../common/types';
import { fetchAs } from '../../common/utility';
import { globals } from '../../core/global.store';

@Component({
  tag: 'foreign-row',
  styleUrl: 'foreign-row.css',
  shadow: true,
})
export class ForeignRow {
  @State() foreignRow: any = {};
  @Listen('foreignKeyClicked', { target: 'window' })
  async showForeignRow(event: CustomEvent<foreignKeyClickedObject>) {
    console.log(event);
    const foreignRowObject = await fetchAs<GenericRequest, GenericResponse>(`${event.detail.tableUrl}/read`, {
      token: globals.globalStore.state.token,
      id: event.detail.id,
    });
    if (foreignRowObject.error === ErrorType.NoError) {
      for (const key in foreignRowObject) {
        if (foreignRowObject.hasOwnProperty(key) && key !== 'error') {
          this.foreignRow = foreignRowObject[key];
        }
      }
    }
    console.log(foreignRowObject);
    console.log(this.foreignRow);
  }
  render() {
    return (
      <Host>
        <slot></slot>
        <div class="card">
          {Object.keys(this.foreignRow).map(key => (
            <div class="card__row">
              <span class="card__key">{key}</span> : <span class="card__value">{this.foreignRow[key]}</span>
            </div>
          ))}
        </div>
      </Host>
    );
  }
}

import { Component, Host, h, Prop, State, Event, EventEmitter } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { MatchResults } from '@stencil/router';


@Component({
    tag: 'partner-list-item',
    styleUrl: 'partner-list-item.css',
    shadow: true,
})

export class PartnerListItem{
    @State() isSelected: boolean = false;
    @Prop() columnData: any[];
    @Prop() rowData: PartnerDetail;
    @Prop() selected: string;

    @Event({ bubbles: true, composed: true }) partnerItemClicked: EventEmitter<string>;

    itemClicked(partner) {
      this.isSelected = !this.isSelected;
      this.partnerItemClicked.emit(partner)
    }

    render() {
        return (
            <Host>
                <slot></slot>
                <a class={ this.selected==this.rowData.id?"selected":"not-selected" } onClick={p => this.itemClicked(this.rowData.id)}>
                  <div class="partner-list-item">
                      <div>{ this.rowData.name }</div>
                  </div>
                </a>
            </Host>
        )
    }

    
}
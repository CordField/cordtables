import { Component, Host, h, Prop, State } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { MatchResults } from '@stencil/router';


class ScPartner {
  id?: string | undefined;

  organization?: string | undefined; 
  active?: boolean | undefined; 
  financial_reporting_types?: string | undefined;
  is_innovations_client?: boolean | undefined;
  pmc_entity_code?: string | undefined;
  point_of_contact?: string | undefined;
  types?: string | undefined;

  created_at?: string | undefined;
  created_by?: string | undefined;
  modified_at?: string | undefined;
  modified_by?: string | undefined;
  owning_person?: string | undefined;
  owning_group?: string | undefined;
}

@Component({
    tag: 'partner-list-item',
    styleUrl: 'partner-list-item.css',
    shadow: true,
})

export class PartnerListItem{
    @Prop() columnData: any[];
    @Prop() rowData: ScPartner;



    render() {
        return (
            <Host>
                <slot></slot>
                <div>
                    <div>{ this.rowData.id }</div>
                </div>
            </Host>
        )
    }

    
}
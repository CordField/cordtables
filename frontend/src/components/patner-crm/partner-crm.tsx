import { Component, Host, h, Prop, State } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { ErrorType, GenericResponse } from '../../common/types';
import { fetchAs } from '../../common/utility';
import { globals } from '../../core/global.store';
import { MatchResults } from '@stencil/router';

class ScPartnerListRequest {
    token: string;
}

class ScPartnerListResponse {
    error: ErrorType;
    partners: ScPartner[];
}

@Component({
    tag: 'partner-crm',
    styleUrl: 'partner-crm.css',
    shadow: true,
})

export class PartnerCRM{
    @State() partnersResponse: ScPartnerListResponse;

    async getPartnersList(){
        this.partnersResponse = await fetchAs<ScPartnerListRequest, ScPartnerListResponse>('sc/partners/list', {
            token: globals.globalStore.state.token,
        });
    } 

    async componentWillLoad() {
        await this.getPartnersList();
        // await this.getFilesList();
    }


    render() {
        return (
            <Host>
                <div class="wrapper">
                    <div class="row">
                        <div class="col-1">
                            <search-input></search-input>
                        </div>
                        <div class="col-1">
                            <filter-menu></filter-menu>
                        </div>
                    </div>

                    <div class="row">
                        <div class="column">
                            {this.partnersResponse && this.partnersResponse.partners.map(row => 
                                <div>
                                    <partner-list-item rowData={row}></partner-list-item>
                                </div>
                            )}
                        </div>
                    </div>
                    
                </div>
            </Host>
        )
    }

}
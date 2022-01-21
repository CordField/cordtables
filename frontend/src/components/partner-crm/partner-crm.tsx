import { Component, Host, h, Prop, State, Listen } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { ErrorType, GenericResponse } from '../../common/types';
import { fetchAs } from '../../common/utility';
import { globals } from '../../core/global.store';
import { MatchResults } from '@stencil/router';



class ScPartnerListRequest {
    token: string;
    page: number;
    resultsPerPage: number;
    search?: string;
}

class ScPartnerListResponse {
    error: ErrorType;
    partners: PartnerDetail[];
    size: number;
}

class ScPartnerDetailsRequest{
    token: string;
    id: string;
}

class ScPartnerDetailsResponse{
    error: ErrorType;
    partner: PartnerDetail;
}

class AdminGroupMembershipUpdateRequest{

}
class AdminGroupMembershipUpdateResponse{

}

@Component({
    tag: 'partner-crm',
    styleUrl: 'partner-crm.css',
    shadow: true,
})

export class PartnerCRM{
    @State() partnersResponse: ScPartnerListResponse;
    @State() partnerDetailsResponse: ScPartnerDetailsResponse;
    @State() selectedPartner: string;
    @State() currentPage: number = 1;
    @State() search: string = "";
    partnersData = []

    @Listen('partnerItemClicked', { target: 'body' })
    async getChangedValue(event: CustomEvent) {
        this.selectedPartner = event.detail
        await this.loadPartnerDetails(this.selectedPartner);
    }

    async loadPartnerDetails(partner){
        this.partnerDetailsResponse = await fetchAs<ScPartnerDetailsRequest, ScPartnerDetailsResponse>('sc/partners-crm/read', {
            token: globals.globalStore.state.token,
            id: partner
        });
    }

    @Listen('pageChanged', { target: 'body' })
    async getChangedPage(event: CustomEvent) {
        this.currentPage = event.detail;
        await this.getPartnersList(this.currentPage, this.search);
    }

    @Listen('reloadPartners', { target: 'body' })
    async reloadPartners(event: CustomEvent) {
        console.log("asdf");
        this.currentPage = event.detail;
        await this.loadPartnerDetails(this.selectedPartner);
        // await this.getPartnersList(this.currentPage, this.search);
    }

    @Listen('doSearch', { target: 'body' })
    async searchPartners(event: CustomEvent) {
        this.search = event.detail;
        this.currentPage = 1;
        await this.getPartnersList(this.currentPage, this.search);
    }

    async getPartnersList(page, search){
        this.partnersResponse = await fetchAs<ScPartnerListRequest, ScPartnerListResponse>('sc/partners-crm/list', {
            token: globals.globalStore.state.token,
            page: page,
            resultsPerPage: 25,
            search: search
        });
        if(this.partnersResponse.error == ErrorType.NoError && this.partnersResponse.partners.length > 0){
            for(var i in this.partnersResponse.partners){
                this.partnersData[this.partnersResponse.partners[i].id] = this.partnersResponse.partners[i];
            }
        }
    } 

    async componentWillLoad() {
        await this.getPartnersList(this.currentPage, this.search);
        // await this.getFilesList();
    }

    

    render() {
        return (
            <Host>
                <div class="page-wrap">
                    <div class="partners-list-wrapper">
                        <div class="partner-container">
                            <div class="row">
                                <div class="col-1">
                                    <search-input></search-input>
                                </div>
                                <div class="col-1">
                                    <filter-menu></filter-menu>
                                </div>
                            </div>
                            <div class="row-test">
                                <div class="column-fullwidth">
                                    {this.partnersResponse && this.partnersResponse.partners.map(row => 
                                        <div>
                                            <partner-list-item rowData={row} selected={this.selectedPartner}></partner-list-item>
                                        </div>
                                    )}
                                </div>
                                <div class="pagination-box">
                                    <cf-pagination currentPage={this.currentPage} total-rows={this.partnersResponse.size} results-per-page="25" page-url="partner-crm"></cf-pagination>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="partner-details-wrapper">
                        <div class="partner-details-container">
                            <div class="row">
                                {this.partnerDetailsResponse && 
                                    <partner-details partnerDetail={this.partnerDetailsResponse.partner} ></partner-details>
                                }
                            </div>
                        </div>
                    </div>
                </div>
            </Host>
        )
    }

}
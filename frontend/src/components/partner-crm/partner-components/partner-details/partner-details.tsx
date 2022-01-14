import { Component, Host, h, Prop, State, Listen } from '@stencil/core';

@Component({
    tag: 'partner-details',
    styleUrl: 'partner-details.css',
    shadow: true,
})

export class PartnerDetails{
    @Prop() partnerDetail: PartnerDetail;

    render(){
        return (
            <Host>
               <div class="partner-details-page">
                    <div class="partner-details">
                        <div class="row">
                            <div class="col box-title"><h5>{this.partnerDetail.name}</h5></div>
                        </div>
                        <div class="partner-details-box">
                            <div class="row"> 
                                <div class="column col-4">Address</div>
                                <div class="column col-8">{this.partnerDetail.address}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Sensitivity</div>
                                <div class="column col-8">{this.partnerDetail.partner_sensitivity}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Files</div>
                                <div class="column col-8"> Not Avaialble </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Financial Reporting Types</div>
                                <div class="column col-8">{this.partnerDetail.financial_reporting_types}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Innovation Client</div>
                                <div class="column col-8">{this.partnerDetail.is_innovations_client}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">PMC Entity Code</div>
                                <div class="column col-8">{this.partnerDetail.pmc_entity_code}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Point of Contact</div>
                                <div class="column col-8">{this.partnerDetail.point_of_contact}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Partner Types</div>
                                <div class="column col-8">{this.partnerDetail.partner}</div>
                            </div>
                        </div>
                    </div>
                    <div class="partner-details">
                        <div class="row">
                            <div class="col box-title"><h5>Maturity Level</h5></div>
                        </div>
                        <div class="partner-details-box">
                            <div class="row">
                                <div class="column col-4">Governance</div>
                                <div class="column col-8">{this.partnerDetail.governance_trans}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Director</div>
                                <div class="column col-8">{this.partnerDetail.director_trans}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Identity</div>
                                <div class="column col-8">{this.partnerDetail.identity_trans}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Growth</div>
                                <div class="column col-8">{this.partnerDetail.growth_trans}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Communication Support</div>
                                <div class="column col-8">{this.partnerDetail.comm_support_trans}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Systems</div>
                                <div class="column col-8">{this.partnerDetail.systems_trans}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Financial Management</div>
                                <div class="column col-8">{this.partnerDetail.fin_management_trans}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Human Resources</div>
                                <div class="column col-8">{this.partnerDetail.hr_trans}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Information Technology</div>
                                <div class="column col-8">{this.partnerDetail.it_trans}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Program Design</div>
                                <div class="column col-8">{this.partnerDetail.program_design_trans}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Translation Technology</div>
                                <div class="column col-8">{this.partnerDetail.tech_translation_trans}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Directory Opportunities</div>
                                <div class="column col-8">{this.partnerDetail.director_opp}</div>
                            </div>
                        </div>
                    </div>
                    <div class="partner-details">
                        <div class="row">
                            <div class="col box-title"><h5>Performance</h5></div>
                        </div>
                        <div class="partner-details-box">
                            <div class="row">
                                <div class="column col-4">Reporting</div>
                                <div class="column col-8">{this.partnerDetail.reporting_performance}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Financial</div>
                                <div class="column col-8">{this.partnerDetail.financial_performance}</div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Translation</div>
                                <div class="column col-8">{this.partnerDetail.translation_performance}</div>
                            </div>
                        </div>
                    </div>

                    {this.partnerDetail.engagements && this.partnerDetail.engagements.map(row => 
                        <div class="partner-details">
                            <div class="row">
                                <div class="col box-title"><h5>Engagement: {row.id}</h5></div>
                            </div>
                            <div class="partner-details-box">
                                <div class="row">
                                    <div class="column col-4">Type</div>
                                    <div class="column col-8">{row.type}</div>
                                </div>
                                <div class="row">
                                    <div class="column col-4">MOU Start</div>
                                    <div class="column col-8">{row.mou_start}</div>
                                </div>
                                <div class="row">
                                    <div class="column col-4">MOU End</div>
                                    <div class="column col-8">{row.mou_end}</div>
                                </div>
                                <div class="row">
                                    <div class="column col-4">Roles</div>
                                    <div class="column col-8">{row.sc_roles}</div>
                                </div>
                                <div class="row">
                                    <div class="column col-4">Partner Roles</div>
                                    <div class="column col-8">{row.partner_roles}</div>
                                </div>
                            </div>
                        </div>
                    )}

                </div>
            </Host>
        )
    }
}
import { Component, Host, h, Prop, State, Listen } from '@stencil/core';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class PartnersUpdateRequest {
    token: string;
    column: string;
    value: any;
    id: string;
}
  
class PartnersUpdateResponse {
    error: ErrorType;
    partner: ScPartner | null = null;
}

@Component({
    tag: 'partner-details',
    styleUrl: 'partner-details.css',
    shadow: true,
})

export class PartnerDetails{
    @Prop() partnerDetail: PartnerDetail;
    @State() doEdit: boolean = false;
    

    clickEdit = () => {
        this.doEdit = !this.doEdit;
    }

    handleUpdate = async (id: string, columnName: string, value: string, endpoint: string): Promise<boolean> => {
        const updateResponse = await fetchAs<PartnersUpdateRequest, PartnersUpdateResponse>(endpoint, {
            token: globals.globalStore.state.token,
            column: columnName,
            id: id,
            value: value !== '' ? value : null,
        });
        if (updateResponse.error == ErrorType.NoError) {
            globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
            return true;
        } else {
            globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
            return false;
        }
    };

    render(){
        return (
            <Host>
               <div class="partner-details-page">
                    <div class="partner-details">
                        <div class="row">
                            <div class="col box-title">
                                <h5>{this.partnerDetail.name}</h5>
                                <div class="edit-button"><button onClick={this.clickEdit} >Edit</button></div>
                            </div>
                        </div>
                        <div class="partner-details-box">
                            <div class="row"> 
                                <div class="column col-4">Address</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        enableEdit={this.doEdit}
                                        field="address"
                                        endPoint="sc/partners/update"
                                        updateFn={this.handleUpdate}
                                        rowId={this.partnerDetail.id}
                                        value={this.partnerDetail.address}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Sensitivity</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        enableEdit={this.doEdit}
                                        field="sensitivity"
                                        endPoint="sc/partners/update"
                                        updateFn={this.handleUpdate}
                                        rowId={this.partnerDetail.id}
                                        value={this.partnerDetail.partner_sensitivity}
                                        selectOptions={[
                                            {display: "Low", value: "Low" },
                                            {display: "Medium", value: "Medium" },
                                            {display: "High", value: "High" },
                                        ]}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Files</div>
                                <div class="column col-8"> Not Avaialble </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Financial Reporting Types</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        enableEdit={this.doEdit}
                                        field="financial_reporting_types"
                                        endPoint="sc/partners/update"
                                        multiSelect={true}
                                        updateFn={this.handleUpdate}
                                        rowId={this.partnerDetail.id}
                                        value={this.partnerDetail.financial_reporting_types}
                                        selectOptions={[
                                            {display:"Funded", value:"Funded"},
                                            {display:"FieldEngaged", value:"FieldEngaged"},
                                        ]}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Innovation Client</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        enableEdit={this.doEdit}
                                        field="is_innovations_client"
                                        endPoint="sc/partners/update"
                                        updateFn={this.handleUpdate}
                                        rowId={this.partnerDetail.id}
                                        value={this.partnerDetail.is_innovations_client}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">PMC Entity Code</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.pmc_entity_code}
                                        field="pmc_entity_code"
                                        rowId={this.partnerDetail.id}
                                        endPoint="sc/partners/update"
                                        updateFn={this.handleUpdate}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Point of Contact</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.id} 
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.point_of_contact}
                                        field="point_of_contact"
                                        endPoint="sc/partners/update"
                                        updateFn={this.handleUpdate}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Partner Types</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.id} 
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.partner}
                                        field="partner"
                                        endPoint="sc/partners/update"
                                        updateFn={this.handleUpdate}
                                    ></cf-editable>
                                </div>
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
                                <div class="column col-8">
                                    <cf-editable
                                        enableEdit={this.doEdit}
                                        field="governance_trans"
                                        endPoint="sc/global-partner-assessments/update"
                                        updateFn={this.handleUpdate}
                                        rowId={this.partnerDetail.gpaid}
                                        value={this.partnerDetail.governance_trans}
                                        selectOptions={[
                                            {display:"Level 1", value:"Level 1"},
                                            {display:"Level 2", value:"Level 2"},
                                            {display:"Level 3", value:"Level 3"},
                                            {display:"Level 4", value:"Level 4"},
                                        ]}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Director</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.gpaid}
                                        field="director_trans"
                                        endPoint="sc/global-partner-assessments/update"
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.director_trans}
                                        updateFn={this.handleUpdate}
                                        selectOptions={[
                                            {display:"Level 1", value:"Level 1"},
                                            {display:"Level 2", value:"Level 2"},
                                            {display:"Level 3", value:"Level 3"},
                                            {display:"Level 4", value:"Level 4"},
                                        ]}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Identity</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.gpaid}
                                        field="identity_trans"
                                        endPoint="sc/global-partner-assessments/update"
                                        enableEdit={this.doEdit}
                                        value={this.partnerDetail.identity_trans}
                                        updateFn={this.handleUpdate}
                                        selectOptions={[
                                            {display:"Level 1", value:"Level 1"},
                                            {display:"Level 2", value:"Level 2"},
                                            {display:"Level 3", value:"Level 3"},
                                            {display:"Level 4", value:"Level 4"},
                                        ]}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Growth</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.gpaid}
                                        field="growth_trans"
                                        endPoint="sc/global-partner-assessments/update"
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.growth_trans}
                                        updateFn={this.handleUpdate}
                                        selectOptions={[
                                            {display:"Level 1", value:"Level 1"},
                                            {display:"Level 2", value:"Level 2"},
                                            {display:"Level 3", value:"Level 3"},
                                            {display:"Level 4", value:"Level 4"},
                                        ]}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Communication Support</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.gpaid}
                                        field="comm_support_trans"
                                        endPoint="sc/global-partner-assessments/update"
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.comm_support_trans}
                                        updateFn={this.handleUpdate}
                                        selectOptions={[
                                            {display:"Level 1", value:"Level 1"},
                                            {display:"Level 2", value:"Level 2"},
                                            {display:"Level 3", value:"Level 3"},
                                            {display:"Level 4", value:"Level 4"},
                                        ]}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Systems</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.gpaid}
                                        field="systems_trans"
                                        endPoint="sc/global-partner-assessments/update"
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.systems_trans}
                                        updateFn={this.handleUpdate}
                                        selectOptions={[
                                            {display:"Level 1", value:"Level 1"},
                                            {display:"Level 2", value:"Level 2"},
                                            {display:"Level 3", value:"Level 3"},
                                            {display:"Level 4", value:"Level 4"},
                                        ]}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Financial Management</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.gpaid}
                                        field="fin_management_trans"
                                        endPoint="sc/global-partner-assessments/update"
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.fin_management_trans}
                                        updateFn={this.handleUpdate}
                                        selectOptions={[
                                            {display:"Level 1", value:"Level 1"},
                                            {display:"Level 2", value:"Level 2"},
                                            {display:"Level 3", value:"Level 3"},
                                            {display:"Level 4", value:"Level 4"},
                                        ]}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Human Resources</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.gpaid}
                                        field="hr_trans"
                                        endPoint="sc/global-partner-assessments/update"
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.hr_trans}
                                        updateFn={this.handleUpdate}
                                        selectOptions={[
                                            {display:"Level 1", value:"Level 1"},
                                            {display:"Level 2", value:"Level 2"},
                                            {display:"Level 3", value:"Level 3"},
                                            {display:"Level 4", value:"Level 4"},
                                        ]}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Information Technology</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.gpaid}
                                        field="it_trans"
                                        endPoint="sc/global-partner-assessments/update"
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.it_trans}
                                        updateFn={this.handleUpdate}
                                        selectOptions={[
                                            {display:"Level 1", value:"Level 1"},
                                            {display:"Level 2", value:"Level 2"},
                                            {display:"Level 3", value:"Level 3"},
                                            {display:"Level 4", value:"Level 4"},
                                        ]}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Program Design</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.gpaid}
                                        field="program_design_trans"
                                        endPoint="sc/global-partner-assessments/update"
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.program_design_trans}
                                        updateFn={this.handleUpdate}
                                        selectOptions={[
                                            {display:"Level 1", value:"Level 1"},
                                            {display:"Level 2", value:"Level 2"},
                                            {display:"Level 3", value:"Level 3"},
                                            {display:"Level 4", value:"Level 4"},
                                        ]}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Translation Technology</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.gpaid}
                                        field="tech_translation_trans"
                                        endPoint="sc/global-partner-assessments/update"
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.tech_translation_trans}
                                        updateFn={this.handleUpdate}
                                        selectOptions={[
                                            {display:"Level 1", value:"Level 1"},
                                            {display:"Level 2", value:"Level 2"},
                                            {display:"Level 3", value:"Level 3"},
                                            {display:"Level 4", value:"Level 4"},
                                        ]}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Directory Opportunities</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.gpaid}
                                        field="director_opp"
                                        endPoint="sc/global-partner-assessments/update"
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.director_opp}
                                        updateFn={this.handleUpdate}
                                        selectOptions={[
                                            {display:"Level 1", value:"Level 1"},
                                            {display:"Level 2", value:"Level 2"},
                                            {display:"Level 3", value:"Level 3"},
                                            {display:"Level 4", value:"Level 4"},
                                        ]}
                                    ></cf-editable>
                                </div>
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
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.gppid}
                                        field="reporting_performance"
                                        endPoint="sc/global-partner-performance/update"
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.reporting_performance}
                                        updateFn={this.handleUpdate}
                                        selectOptions={[
                                            {display:"1", value:"1"},
                                            {display:"2", value:"2"},
                                            {display:"3", value:"3"},
                                            {display:"4", value:"4"},
                                        ]}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Financial</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.gppid}
                                        field="financial_performance"
                                        endPoint="sc/global-partner-performance/update"
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.financial_performance}
                                        updateFn={this.handleUpdate}
                                        selectOptions={[
                                            {display:"1", value:"1"},
                                            {display:"2", value:"2"},
                                            {display:"3", value:"3"},
                                            {display:"4", value:"4"},
                                        ]}
                                    ></cf-editable>
                                </div>
                            </div>
                            <div class="row">
                                <div class="column col-4">Translation</div>
                                <div class="column col-8">
                                    <cf-editable 
                                        rowId={this.partnerDetail.gppid}
                                        field="translation_performance"
                                        endPoint="sc/global-partner-performance/update"
                                        enableEdit={this.doEdit} 
                                        value={this.partnerDetail.translation_performance}
                                        updateFn={this.handleUpdate}
                                        selectOptions={[
                                            {display:"1", value:"1"},
                                            {display:"2", value:"2"},
                                            {display:"3", value:"3"},
                                            {display:"4", value:"4"},
                                        ]}
                                    ></cf-editable>
                                </div>
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
                                    <div class="column col-8">
                                        <cf-editable 
                                            rowId={row.id}
                                            field="type"
                                            endPoint="sc/global-partner-engagements/update"
                                            enableEdit={this.doEdit} 
                                            value={row.type}
                                            updateFn={this.handleUpdate}
                                            selectOptions={[
                                                {display:"CIT", value:"CIT"},
                                                {display:"Engagements", value:"Engagements"}
                                            ]}
                                        ></cf-editable>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="column col-4">MOU Start</div>
                                    <div class="column col-8">
                                        <cf-editable 
                                            rowId={row.id}
                                            field="mou_start"
                                            endPoint="sc/global-partner-engagements/update"
                                            enableEdit={this.doEdit} 
                                            value={row.mou_start}
                                            updateFn={this.handleUpdate}
                                        ></cf-editable>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="column col-4">MOU End</div>
                                    <div class="column col-8">
                                        <cf-editable 
                                            rowId={row.id}
                                            field="mou_end"
                                            endPoint="sc/global-partner-engagements/update"
                                            enableEdit={this.doEdit} 
                                            value={row.mou_end}
                                            updateFn={this.handleUpdate}
                                        ></cf-editable>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="column col-4">Roles</div>
                                    <div class="column col-8">
                                        <cf-editable 
                                            rowId={row.id}
                                            field="sc_roles"
                                            endPoint="sc/global-partner-engagements/update"
                                            enableEdit={this.doEdit} 
                                            value={row.sc_roles}
                                            updateFn={this.handleUpdate}
                                            multiSelect={true}
                                            selectOptions={[
                                                {display: "A", value: "A"},
                                                {display: "B", value: "B"}
                                            ]}
                                        ></cf-editable>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="column col-4">Partner Roles</div>
                                    <div class="column col-8">
                                        <cf-editable 
                                            rowId={row.id}
                                            field="partner_roles"
                                            endPoint="sc/global-partner-engagements/update"
                                            enableEdit={this.doEdit} 
                                            value={row.partner_roles}
                                            updateFn={this.handleUpdate}
                                            multiSelect={true}
                                            selectOptions={[
                                                {display: "A", value: "A"},
                                                {display: "B", value: "B"}
                                            ]}
                                        ></cf-editable>
                                    </div>
                                </div>
                            </div>
                        </div>
                    )}
                </div>
            </Host>
        )
    }
}
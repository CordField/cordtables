import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

    

class CreateGlobalPartnerAssessmentExRequest {
  token: string;
  globalPartnerAssessment: {
    partner: number;
    governance_trans: string;
    director_trans: string;
    identity_trans: string;
    growth_trans: string;
    comm_support_trans: string;
    systems_trans: string;
    fin_management_trans: string;
    hr_trans: string;
    it_trans: string;
    program_design_trans: string;
    tech_translation_trans: string;
    director_opp: string;
    financial_management_opp: string;
    program_design_opp: string;
    tech_translation_opp: string;
  };
}
class CreateGlobalPartnerAssessmentExResponse extends GenericResponse {
  globalPartnerAssessment: ScGlobalPartnerAssessment;
}

class ScGlobalPartnerAssessmentListRequest {
  token: string;
}

class ScGlobalPartnerAssessmentListResponse {
  error: ErrorType;
  globalPartnerAssessments: ScGlobalPartnerAssessment[];
}


class ScGlobalPartnerAssessmentUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class ScGlobalPartnerAssessmentUpdateResponse {
  error: ErrorType;
  globalPartnerAssessment: ScGlobalPartnerAssessment | null = null;
}

class DeleteGlobalPartnerAssessmentExRequest {
  id: number;
  token: string;
}

class DeleteGlobalPartnerAssessmentExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'sc-global-partner-assessments',
  styleUrl: 'sc-global-partner-assessments.css',
  shadow: true,
})
export class ScGlobalPartnerAssessments {

  @State() globalPartnerAssessmentsResponse: ScGlobalPartnerAssessmentListResponse;

  newPartner: number;
  newGovernance_trans: string;
  newDirector_trans: string;
  newIdentity_trans: string;
  newGrowth_trans: string;
  newComm_support_trans: string;
  newSystems_trans: string;
  newFin_management_trans: string;
  newHr_trans: string;
  newIt_trans: string;
  newProgram_design_trans: string;
  newTech_translation_trans: string;
  newDirector_opp: string;
  newFinancial_management_opp: string;
  newProgram_design_opp: string;
  newTech_translation_opp: string;
  
  
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScGlobalPartnerAssessmentUpdateRequest, ScGlobalPartnerAssessmentUpdateResponse>('sc-global-partner-assessments/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.globalPartnerAssessmentsResponse = { error: ErrorType.NoError, globalPartnerAssessments: this.globalPartnerAssessmentsResponse.globalPartnerAssessments.map(globalPartnerAssessment => (globalPartnerAssessment.id === id ? updateResponse.globalPartnerAssessment : globalPartnerAssessment)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteGlobalPartnerAssessmentExRequest, DeleteGlobalPartnerAssessmentExResponse>('sc-global-partner-assessments/delete', {
      id,
      token: globals.globalStore.state.token,
    });
    if (deleteResponse.error === ErrorType.NoError) {
      this.getList();
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item deleted successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: deleteResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  async getList() {
    this.globalPartnerAssessmentsResponse = await fetchAs<ScGlobalPartnerAssessmentListRequest, ScGlobalPartnerAssessmentListResponse>('sc-global-partner-assessments/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }

  partnerChange(event) {
    this.newPartner = event.target.value;
  }
  governance_transChange(event) {
    this.newGovernance_trans = event.target.value;
  }
  director_transChange(event) {
    this.newDirector_trans = event.target.value;
  }
  identity_transChange(event) {
    this.newIdentity_trans = event.target.value;
  }
  growth_transChange(event) {
    this.newGrowth_trans = event.target.value;
  }
  comm_support_transChange(event) {
    this.newComm_support_trans = event.target.value;
  }
  systems_transChange(event) {
    this.newSystems_trans = event.target.value;
  }
  fin_management_transChange(event) {
    this.newFin_management_trans = event.target.value;
  }
  hr_transChange(event) {
    this.newHr_trans = event.target.value;
  }
  it_transChange(event) {
    this.newIt_trans = event.target.value;
  }
  program_design_transChange(event) {
    this.newProgram_design_trans = event.target.value;
  }
  tech_translation_transChange(event) {
    this.newTech_translation_trans = event.target.value;
  }
  director_oppChange(event) {
    this.newDirector_opp = event.target.value;
  }
  financial_management_oppChange(event) {
    this.newFinancial_management_opp = event.target.value;
  }
  program_design_oppChange(event) {
    this.newProgram_design_opp = event.target.value;
  }
  tech_translation_oppChange(event) {
    this.newTech_translation_opp = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateGlobalPartnerAssessmentExRequest, CreateGlobalPartnerAssessmentExResponse>('sc-global-partner-assessments/create-read', {
      token: globals.globalStore.state.token,
      globalPartnerAssessment: {
        partner: this.newPartner,
        governance_trans: this.newGovernance_trans,
        director_trans: this.newDirector_trans,
        identity_trans: this.newIdentity_trans,
        growth_trans: this.newGrowth_trans,
        comm_support_trans: this.newComm_support_trans,
        systems_trans: this.newSystems_trans,
        fin_management_trans: this.newFin_management_trans,
        hr_trans: this.newHr_trans,
        it_trans: this.newIt_trans,
        program_design_trans: this.newProgram_design_trans,
        tech_translation_trans: this.newTech_translation_trans,
        director_opp: this.newDirector_opp,
        financial_management_opp: this.newFinancial_management_opp,
        program_design_opp: this.newProgram_design_opp,
        tech_translation_opp: this.newTech_translation_opp,
      },
    });

    if (createResponse.error === ErrorType.NoError) {
      globals.globalStore.state.editMode = false;
      this.getList();
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item inserted successfully', id: uuidv4(), type: 'success' });
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: createResponse.error, id: uuidv4(), type: 'error' });
    }
  };


  columnData: ColumnDescription[] = [
    {
      field: 'id',
      displayName: 'ID',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },


    {
      field: 'partner',
      displayName: 'Partner',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'governance_trans',
      displayName: 'Governance Trans',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'director_trans',
      displayName: 'Director Trans',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'identity_trans',
      displayName: 'Identity Trans',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'growth_trans',
      displayName: 'Growth Trans',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'comm_support_trans',
      displayName: 'Comm Support Trans',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'systems_trans',
      displayName: 'Systems Trans',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'fin_management_trans',
      displayName: 'Fin Management Trans',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'hr_trans',
      displayName: 'Hr Trans',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'it_trans',
      displayName: 'It Trans',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'program_design_trans',
      displayName: 'Program Design Trans',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'tech_translation_trans',
      displayName: 'Tech Translation Trans',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'director_opp',
      displayName: 'Director Opp',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'financial_management_opp',
      displayName: 'Financial Management Opp',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'program_design_opp',
      displayName: 'Program Design Opp',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'tech_translation_opp',
      displayName: 'Tech Translation Opp',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'created_at',
      displayName: 'Created At',
      width: 250,
      editable: false,
    },
    {
      field: 'created_by',
      displayName: 'Created By',
      width: 100,
      editable: false,
    },
    {
      field: 'modified_at',
      displayName: 'Last Modified',
      width: 250,
      editable: false,
    },
    {
      field: 'modified_by',
      displayName: 'Last Modified By',
      width: 100,
      editable: false,
    },
    {
      field: 'owning_person',
      displayName: 'Owning Person ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'owning_group',
      displayName: 'Owning Group ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
  ];

  async componentWillLoad() {
    await this.getList();
    // await this.getFilesList();
  }


  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.globalPartnerAssessmentsResponse && <cf-table rowData={this.globalPartnerAssessmentsResponse.globalPartnerAssessments} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">

            <div id="partner-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="partner">Partner</label>
              </span>
              <span class="form-thing">
                <input type="text" id="partner" name="partner" onInput={event => this.partnerChange(event)} />
              </span>
            </div>








            <div id="governance_trans-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="governance_transtner">Governance Trans</label>
              </span>
              <span class="form-thing">
                <select id="governance_transtner" name="governance_trans" onInput={event => this.governance_transChange(event)}>
                  <option value="">Select Level</option>
                    <option value="Level 1" selected={this.newGovernance_trans === "Level 1"}>Level 1</option>
                    <option value="Level 2" selected={this.newGovernance_trans === "Level 2"}>Level 2</option>
                    <option value="Level 3" selected={this.newGovernance_trans === "Level 3"}>Level 3</option>
                    <option value="Level 4" selected={this.newGovernance_trans === "Level 4"}>Level 4</option>
                  </select>
              </span>
            </div>

            <div id="director_trans-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="director_trans">Director Trans</label>
              </span>
              <span class="form-thing">
                <select id="director_trans" name="director_trans" onInput={event => this.director_transChange(event)}>
                <option value="">Select Level</option>
                    <option value="Level 1" selected={this.newDirector_trans === "Level 1"}>Level 1</option>
                    <option value="Level 2" selected={this.newDirector_trans === "Level 2"}>Level 2</option>
                    <option value="Level 3" selected={this.newDirector_trans === "Level 3"}>Level 3</option>
                    <option value="Level 4" selected={this.newDirector_trans === "Level 4"}>Level 4</option>
                  </select>
              </span>
            </div>

            <div id="identity_trans-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="identity_trans">Identity Trans</label>
              </span>
              <span class="form-thing">
                <select id="identity_trans" name="identity_trans" onInput={event => this.identity_transChange(event)}>
                <option value="">Select Level</option>
                    <option value="Level 1" selected={this.newIdentity_trans === "Level 1"}>Level 1</option>
                    <option value="Level 2" selected={this.newIdentity_trans === "Level 2"}>Level 2</option>
                    <option value="Level 3" selected={this.newIdentity_trans === "Level 3"}>Level 3</option>
                    <option value="Level 4" selected={this.newIdentity_trans === "Level 4"}>Level 4</option>
                  </select>
              </span>
            </div>

            <div id="growth_trans-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="growth_trans">Growth Trans</label>
              </span>
              <span class="form-thing">
                <select id="growth_trans" name="growth_trans" onInput={event => this.growth_transChange(event)}>
                <option value="">Select Level</option>
                    <option value="Level 1" selected={this.newGrowth_trans === "Level 1"}>Level 1</option>
                    <option value="Level 2" selected={this.newGrowth_trans === "Level 2"}>Level 2</option>
                    <option value="Level 3" selected={this.newGrowth_trans === "Level 3"}>Level 3</option>
                    <option value="Level 4" selected={this.newGrowth_trans === "Level 4"}>Level 4</option>
                  </select>
              </span>
            </div>

            <div id="comm_support_trans-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="comm_support_trans">Comm Support Trans</label>
              </span>
              <span class="form-thing">
                <select id="comm_support_trans" name="comm_support_trans" onInput={event => this.comm_support_transChange(event)}>
                <option value="">Select Level</option>
                    <option value="Level 1" selected={this.newComm_support_trans === "Level 1"}>Level 1</option>
                    <option value="Level 2" selected={this.newComm_support_trans === "Level 2"}>Level 2</option>
                    <option value="Level 3" selected={this.newComm_support_trans === "Level 3"}>Level 3</option>
                    <option value="Level 4" selected={this.newComm_support_trans === "Level 4"}>Level 4</option>
                  </select>
              </span>
            </div>

            <div id="systems_trans-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="systems_trans">Systems Trans</label>
              </span>
              <span class="form-thing">
                <select id="systems_trans" name="systems_trans" onInput={event => this.systems_transChange(event)}>
                <option value="">Select Level</option>
                    <option value="Level 1" selected={this.newSystems_trans === "Level 1"}>Level 1</option>
                    <option value="Level 2" selected={this.newSystems_trans === "Level 2"}>Level 2</option>
                    <option value="Level 3" selected={this.newSystems_trans === "Level 3"}>Level 3</option>
                    <option value="Level 4" selected={this.newSystems_trans === "Level 4"}>Level 4</option>
                  </select>
              </span>
            </div>

            <div id="fin_management_trans-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="fin_management_trans">Fin Management Trans</label>
              </span>
              <span class="form-thing">
                <select id="fin_management_trans" name="fin_management_trans" onInput={event => this.fin_management_transChange(event)}>
                <option value="">Select Level</option>
                    <option value="Level 1" selected={this.newFin_management_trans === "Level 1"}>Level 1</option>
                    <option value="Level 2" selected={this.newFin_management_trans === "Level 2"}>Level 2</option>
                    <option value="Level 3" selected={this.newFin_management_trans === "Level 3"}>Level 3</option>
                    <option value="Level 4" selected={this.newFin_management_trans === "Level 4"}>Level 4</option>
                  </select>
              </span>
            </div>

            <div id="hr_trans-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="hr_trans">HR Trans</label>
              </span>
              <span class="form-thing">
                <select id="hr_trans" name="hr_trans" onInput={event => this.hr_transChange(event)}>
                <option value="">Select Level</option>
                    <option value="Level 1" selected={this.newHr_trans === "Level 1"}>Level 1</option>
                    <option value="Level 2" selected={this.newHr_trans === "Level 2"}>Level 2</option>
                    <option value="Level 3" selected={this.newHr_trans === "Level 3"}>Level 3</option>
                    <option value="Level 4" selected={this.newHr_trans === "Level 4"}>Level 4</option>
                  </select>
              </span>
            </div>

            <div id="it_trans-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="it_trans">IT Trans</label>
              </span>
              <span class="form-thing">
                <select id="it_trans" name="it_trans" onInput={event => this.it_transChange(event)}>
                <option value="">Select Level</option>
                    <option value="Level 1" selected={this.newIt_trans === "Level 1"}>Level 1</option>
                    <option value="Level 2" selected={this.newIt_trans === "Level 2"}>Level 2</option>
                    <option value="Level 3" selected={this.newIt_trans === "Level 3"}>Level 3</option>
                    <option value="Level 4" selected={this.newIt_trans === "Level 4"}>Level 4</option>
                  </select>
              </span>
            </div>

            <div id="program_design_trans-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="program_design_trans">Program Design Trans</label>
              </span>
              <span class="form-thing">
                <select id="program_design_trans" name="program_design_trans" onInput={event => this.program_design_transChange(event)}>
                <option value="">Select Level</option>
                    <option value="Level 1" selected={this.newProgram_design_trans === "Level 1"}>Level 1</option>
                    <option value="Level 2" selected={this.newProgram_design_trans === "Level 2"}>Level 2</option>
                    <option value="Level 3" selected={this.newProgram_design_trans === "Level 3"}>Level 3</option>
                    <option value="Level 4" selected={this.newProgram_design_trans === "Level 4"}>Level 4</option>
                  </select>
              </span>
            </div>

            <div id="tech_translation_trans-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="tech_translation_trans">Tech Translation Trans</label>
              </span>
              <span class="form-thing">
                <select id="tech_translation_trans" name="tech_translation_trans" onInput={event => this.tech_translation_transChange(event)}>
                <option value="">Select Level</option>
                    <option value="Level 1" selected={this.newTech_translation_trans === "Level 1"}>Level 1</option>
                    <option value="Level 2" selected={this.newTech_translation_trans === "Level 2"}>Level 2</option>
                    <option value="Level 3" selected={this.newTech_translation_trans === "Level 3"}>Level 3</option>
                    <option value="Level 4" selected={this.newTech_translation_trans === "Level 4"}>Level 4</option>
                  </select>
              </span>
            </div>

            <div id="director_opp-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="director_opp">Director Opp</label>
              </span>
              <span class="form-thing">
                <select id="director_opp" name="director_opp" onInput={event => this.director_oppChange(event)}>
                <option value="">Select Level</option>
                    <option value="Level 1" selected={this.newDirector_opp === "Level 1"}>Level 1</option>
                    <option value="Level 2" selected={this.newDirector_opp === "Level 2"}>Level 2</option>
                    <option value="Level 3" selected={this.newDirector_opp === "Level 3"}>Level 3</option>
                    <option value="Level 4" selected={this.newDirector_opp === "Level 4"}>Level 4</option>
                  </select>
              </span>
            </div>

            <div id="financial_management_opp-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="financial_management_opp">Financial Management Opp</label>
              </span>
              <span class="form-thing">
                <select id="financial_management_opp" name="financial_management_opp" onInput={event => this.financial_management_oppChange(event)}>
                <option value="">Select Level</option>
                    <option value="Level 1" selected={this.newFinancial_management_opp === "Level 1"}>Level 1</option>
                    <option value="Level 2" selected={this.newFinancial_management_opp === "Level 2"}>Level 2</option>
                    <option value="Level 3" selected={this.newFinancial_management_opp === "Level 3"}>Level 3</option>
                    <option value="Level 4" selected={this.newFinancial_management_opp === "Level 4"}>Level 4</option>
                  </select>
              </span>
            </div>

            <div id="program_design_opp-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="program_design_opp">Program Design Opp</label>
              </span>
              <span class="form-thing">
                <select id="program_design_opp" name="program_design_opp" onInput={event => this.program_design_oppChange(event)}>
                <option value="">Select Level</option>
                    <option value="Level 1" selected={this.newProgram_design_opp === "Level 1"}>Level 1</option>
                    <option value="Level 2" selected={this.newProgram_design_opp === "Level 2"}>Level 2</option>
                    <option value="Level 3" selected={this.newProgram_design_opp === "Level 3"}>Level 3</option>
                    <option value="Level 4" selected={this.newProgram_design_opp === "Level 4"}>Level 4</option>
                  </select>
              </span>
            </div>

            <div id="tech_translation_opp-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="tech_translation_opp">Tech Translation Opp</label>
              </span>
              <span class="form-thing">
                <select id="tech_translation_opp" name="tech_translation_opp" onInput={event => this.tech_translation_oppChange(event)}>
                    <option value="">Select Level</option>
                    <option value="Level 1" selected={this.newTech_translation_opp === "Level 1"}>Level 1</option>
                    <option value="Level 2" selected={this.newTech_translation_opp === "Level 2"}>Level 2</option>
                    <option value="Level 3" selected={this.newTech_translation_opp === "Level 3"}>Level 3</option>
                    <option value="Level 4" selected={this.newTech_translation_opp === "Level 4"}>Level 4</option>
                </select>
              </span>
            </div>
            
            
   
            

            <span class="form-thing">
              <input id="create-button" type="submit" value="Create" onClick={this.handleInsert} />
            </span>
          </form>
        )}
      </Host>
    );
  }

}

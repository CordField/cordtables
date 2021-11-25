import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateInternshipEngagementExRequest {
  token: string;
  internshipEngagement: {
    project: number;
    ethnologue: number;
    change_to_plan: number;
    active: boolean;
    communications_complete: string;
    complete_date: string;
    country_of_origin: number;
    disbursement_complete_date: string;
    end_date: string;
    end_date_override: string;
    growth_plan: number;
    initial_end_date: string;
    intern: number;
    last_reactivated_at: string;
    mentor: number;
    methodology: string;
    paratext_registry: string;
    periodic_reports_directory: number;
    position: string;
    start_date: string;
    start_date_override: string;
    status: string;
  };
}
class CreateInternshipEngagementExResponse extends GenericResponse {
  internshipEngagement: ScInternshipEngagement;
}

class ScInternshipEngagementListRequest {
  token: string;
}

class ScInternshipEngagementListResponse {
  error: ErrorType;
  internshipEngagements: ScInternshipEngagement[];
}


class ScInternshipEngagementUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class ScInternshipEngagementUpdateResponse {
  error: ErrorType;
  internshipEngagement: ScInternshipEngagement | null = null;
}

class DeleteInternshipEngagementExRequest {
  id: number;
  token: string;
}

class DeleteInternshipEngagementExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'sc-internship-engagements',
  styleUrl: 'sc-internship-engagements.css',
  shadow: true,
})
export class ScInternshipEngagements {

  @State() internshipEngagementsResponse: ScInternshipEngagementListResponse;

  newProject: number;
  newEthnologue: number;
  newChange_to_plan: number;
  newActive: boolean;
  newCommunications_complete: string;
  newComplete_date: string;
  newCountry_of_origin: number;
  newDisbursement_complete_date: string;
  newEnd_date: string;
  newEnd_date_override: string;
  newGrowth_plan: number;
  newInitial_end_date: string;
  newIntern: number;
  newLast_reactivated_at: string;
  newMentor: number;
  newMethodology: string;
  newParatext_registry: string;
  newPeriodic_reports_directory: number;
  newPosition: string;
  newStart_date: string;
  newStart_date_override: string;
  newStatus: string;
  
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScInternshipEngagementUpdateRequest, ScInternshipEngagementUpdateResponse>('sc-internship-engagements/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.internshipEngagementsResponse = { error: ErrorType.NoError, internshipEngagements: this.internshipEngagementsResponse.internshipEngagements.map(internshipEngagement => (internshipEngagement.id === id ? updateResponse.internshipEngagement : internshipEngagement)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteInternshipEngagementExRequest, DeleteInternshipEngagementExResponse>('sc-internship-engagements/delete', {
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
    this.internshipEngagementsResponse = await fetchAs<ScInternshipEngagementListRequest, ScInternshipEngagementListResponse>('sc-internship-engagements/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }

  
  projectChange(event) {
    this.newProject = event.target.value;
  }

  ethnologueChange(event) {
    this.newEthnologue = event.target.value;
  }

  change_to_planChange(event) {
    this.newChange_to_plan = event.target.value;
  }

  activeChange(event) {
    this.newActive = event.target.value;
  }

  communications_completeChange(event) {
    this.newCommunications_complete = event.target.value;
  }

  complete_dateChange(event) {
    this.newComplete_date = event.target.value;
  }

  country_of_originChange(event) {
    this.newCountry_of_origin = event.target.value;
  }

  disbursement_complete_dateChange(event) {
    this.newDisbursement_complete_date = event.target.value;
  }

  end_dateChange(event) {
    this.newEnd_date = event.target.value;
  }

  


  end_date_overrideChange(event) {
    this.newEnd_date_override = event.target.value;
  }

  growth_planChange(event) {
    this.newGrowth_plan = event.target.value;
  }

  initial_end_dateChange(event) {
    this.newInitial_end_date = event.target.value;
  }

  internChange(event) {
    this.newIntern = event.target.value;
  }

  last_reactivated_atChange(event) {
    this.newLast_reactivated_at = event.target.value;
  }

  mentorChange(event) {
    this.newMentor = event.target.value;
  }

  methodologyChange(event) {
    this.newMethodology = event.target.value;
  }

  paratext_registryChange(event) {
    this.newParatext_registry = event.target.value;
  }

  periodic_reports_directoryChange(event) {
    this.newPeriodic_reports_directory = event.target.value;
  }

  positionChange(event) {
    this.newPosition = event.target.value;
  }

  start_dateChange(event) {
    this.newStart_date = event.target.value;
  }

  start_date_overrideChange(event) {
    this.newStart_date_override = event.target.value;
  }

  statusChange(event) {
    this.newStatus = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateInternshipEngagementExRequest, CreateInternshipEngagementExResponse>('sc-internship-engagements/create-read', {
      token: globals.globalStore.state.token,
      internshipEngagement: {
        project: this.newProject,
        ethnologue: this.newEthnologue,
        change_to_plan: this.newChange_to_plan,
        active: this.newActive,
        communications_complete: this.newCommunications_complete,
        complete_date: this.newComplete_date,
        country_of_origin: this.newCountry_of_origin,
        disbursement_complete_date: this.newDisbursement_complete_date,
        end_date: this.newEnd_date,
        end_date_override: this.newEnd_date_override,
        growth_plan: this.newGrowth_plan,
        initial_end_date: this.newInitial_end_date,
        intern: this.newIntern,
        last_reactivated_at: this.newLast_reactivated_at,
        mentor: this.newMentor,
        methodology: this.newMethodology,
        paratext_registry: this.newParatext_registry,
        periodic_reports_directory: this.newPeriodic_reports_directory,
        position: this.newPosition,
        start_date: this.newStart_date,
        start_date_override: this.newStart_date_override,
        status: this.newStatus,
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
      field: 'project',
      displayName: 'Project',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'ethnologue',
      displayName: 'Ethnologue',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'change_to_plan',
      displayName: 'Change To Plan',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'active',
      displayName: 'Active',
      width: 200,
      editable: true,
      selectOptions: [
        {display: "True", value: "true"},
        {display: "False", value: "false"},
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'communications_complete',
      displayName: 'Communications Complete',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'complete_date',
      displayName: 'Complete Date',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'country_of_origin',
      displayName: 'Country Of Origin',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'disbursement_complete_date',
      displayName: 'Disbursement Complete Date',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'end_date',
      displayName: 'End Date',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'end_date_override',
      displayName: 'End Date Override',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'growth_plan',
      displayName: 'Growth Plan',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'initial_end_date',
      displayName: 'Initial End Date',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'intern',
      displayName: 'Intern',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'last_reactivated_at',
      displayName: 'Last Reactivated At',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'mentor',
      displayName: 'Mentor',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'methodology',
      displayName: 'Methodology',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'paratext_registry',
      displayName: 'Paratext Registry',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'periodic_reports_directory',
      displayName: 'Periodic Reports Directory',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'position',
      displayName: 'Position',
      width: 200,
      editable: true,
      selectOptions: [
        {display: "A", value: "A"},
        {display: "B", value: "B"},
        {display: "C", value: "C"},
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'start_date',
      displayName: 'Start Date',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'start_date_override',
      displayName: 'Start Date Override',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'status',
      displayName: 'Status',
      width: 200,
      editable: true,
      selectOptions: [
        {display: "A", value: "A"},
        {display: "B", value: "B"},
        {display: "C", value: "C"},
      ],
      updateFn: this.handleUpdate,
    },

    {
      field: 'created_at',
      displayName: 'Created At',
      width: 100,
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
        {this.internshipEngagementsResponse && <cf-table rowData={this.internshipEngagementsResponse.internshipEngagements} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="project-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="project">Project</label>
              </span>
              <span class="form-thing">
                <input type="number" id="project" name="project" onInput={event => this.projectChange(event)} />
              </span>
            </div>

            <div id="ethnologue-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="ethnologue">Ethnologue</label>
              </span>
              <span class="form-thing">
                <input type="number" id="ethnologue" name="ethnologue" onInput={event => this.ethnologueChange(event)} />
              </span>
            </div> 

            <div id="change_to_plan-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="change_to_plan">Change To Plan</label>
              </span>
              <span class="form-thing">
                <input type="number" id="change_to_plan" name="change_to_plan" onInput={event => this.change_to_planChange(event)} />
              </span>
            </div>

            <div id="active-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="active">Active</label>
              </span>
              <span class="form-thing">
                <select id="active" name="active" onInput={event => this.activeChange(event)}>
                  <option value="">Select Active</option>
                  <option value="true" selected={this.newActive === true}>True</option>
                   <option value="false" selected={this.newActive === false}>False</option>
                </select>
              </span>
            </div> 

            <div id="communications_complete-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="communications_complete">Communications Complete Date</label>
              </span>
              <span class="form-thing">
                <input type="text" id="communications_complete" name="communications_complete" onInput={event => this.communications_completeChange(event)} />
              </span>
            </div>

            <div id="complete_date-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="complete_date">Complete Date</label>
              </span>
              <span class="form-thing">
                <input type="text" id="complete_date" name="complete_date" onInput={event => this.complete_dateChange(event)} />
              </span>
            </div> 

            <div id="country_of_origin-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="country_of_origin">Country Of Origin</label>
              </span>
              <span class="form-thing">
                <input type="number" id="country_of_origin" name="country_of_origin" onInput={event => this.country_of_originChange(event)} />
              </span>
            </div>

            <div id="disbursement_complete_date-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="disbursement_complete_date">Disbursement Complete Date</label>
              </span>
              <span class="form-thing">
                <input type="text" id="disbursement_complete_date" name="disbursement_complete_date" onInput={event => this.disbursement_complete_dateChange(event)} />
              </span>
            </div> 

            <div id="end_date-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="end_date">End Date</label>
              </span>
              <span class="form-thing">
                <input type="text" id="end_date" name="end_date" onInput={event => this.end_dateChange(event)} />
              </span>
            </div>

            <div id="end_date_override-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="end_date_override">End Date Override</label>
              </span>
              <span class="form-thing">
                <input type="text" id="end_date_override" name="end_date_override" onInput={event => this.end_date_overrideChange(event)} />
              </span>
            </div> 

            <div id="growth_plan-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="growth_plan">Growth Plan</label>
              </span>
              <span class="form-thing">
                <input type="number" id="growth_plan" name="growth_plan" onInput={event => this.growth_planChange(event)} />
              </span>
            </div>

            <div id="initial_end_date-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="initial_end_date">Initial End Date</label>
              </span>
              <span class="form-thing">
                <input type="text" id="initial_end_date" name="initial_end_date" onInput={event => this.initial_end_dateChange(event)} />
              </span>
            </div> 

            <div id="intern-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="role">Intern</label>
              </span>
              <span class="form-thing">
                <input type="number" id="intern" name="intern" onInput={event => this.internChange(event)} />
              </span>
            </div>

            <div id="last_reactivated_at-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="last_reactivated_at">Last Reactivated At</label>
              </span>
              <span class="form-thing">
                <input type="text" id="last_reactivated_at" name="last_reactivated_at" onInput={event => this.last_reactivated_atChange(event)} />
              </span>
            </div> 

            <div id="mentor-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="mentor">Mentor</label>
              </span>
              <span class="form-thing">
                <input type="number" id="mentor" name="mentor" onInput={event => this.mentorChange(event)} />
              </span>
            </div>

            <div id="methodology-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="methodology">Methodology</label>
              </span>
              <span class="form-thing">
                <select id="methodology" name="methodology" onInput={event => this.methodologyChange(event)}>
                  <option value="">Select Methodology</option>
                  <option value="A" selected={this.newMethodology === "A"}>A</option>
                  <option value="B" selected={this.newMethodology === "B"}>B</option>
                  <option value="C" selected={this.newMethodology === "C"}>C</option>
                </select>

              </span>
            </div> 

            <div id="paratext_registry-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="paratext_registry">Paratext Registry</label>
              </span>
              <span class="form-thing">
                <input type="text" id="paratext_registry" name="paratext_registry" onInput={event => this.paratext_registryChange(event)} />
              </span>
            </div>

            <div id="periodic_reports_directory-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="periodic_reports_directory">Periodic Reports Directory</label>
              </span>
              <span class="form-thing">
                <input type="number" id="periodic_reports_directory" name="periodic_reports_directory" onInput={event => this.periodic_reports_directoryChange(event)} />
              </span>
            </div> 

            <div id="position-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="position">Position</label>
              </span>
              <span class="form-thing">
                <select id="position" name="position" onInput={event => this.positionChange(event)}>
                  <option value="">Select Position</option>
                  <option value="A" selected={this.newPosition === "A"}>A</option>
                  <option value="B" selected={this.newPosition === "B"}>B</option>
                  <option value="C" selected={this.newPosition === "C"}>C</option>
                </select>
              </span>
            </div>

            <div id="start_date-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="start_date">Start Date</label>
              </span>
              <span class="form-thing">
                <input type="text" id="start_date" name="start_date" onInput={event => this.start_dateChange(event)} />
              </span>
            </div> 

            <div id="start_date_override-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="start_date_override">Start Date Override</label>
              </span>
              <span class="form-thing">
                <input type="text" id="start_date_override" name="start_date_override" onInput={event => this.start_date_overrideChange(event)} />
              </span>
            </div>

            <div id="status-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="status">Status</label>
              </span>
              <span class="form-thing">
                <select id="status" name="status" onInput={event => this.statusChange(event)}>
                  <option value="">Select Status</option>
                  <option value="A" selected={this.newStatus === "A"}>A</option>
                  <option value="B" selected={this.newStatus === "B"}>B</option>
                  <option value="C" selected={this.newStatus === "C"}>C</option>
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


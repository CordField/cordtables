import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateInternshipEngagementExRequest {
  token: string;
  internshipEngagement: {
    project: string;
    change_to_plan: string;
    active: boolean;
    ceremony: string;
    communications_complete_date: string;
    complete_date: string;
    country_of_origin: string;
    disbursement_complete_date: string;
    end_date: string;
    end_date_override: string;
    growth_plan: string;
    initial_end_date: string;
    intern: string;
    last_reactivated_at: string;
    mentor: string;
    methodologies: string;
    paratext_registry: string;
    periodic_reports_directory: string;
    position: string;
    sensitivity: string;
    start_date: string;
    start_date_override: string;
    status: string;
    status_modified_at: string;
    last_suspended_at: string;
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
  id: string;
}

class ScInternshipEngagementUpdateResponse {
  error: ErrorType;
  internshipEngagement: ScInternshipEngagement | null = null;
}

class DeleteInternshipEngagementExRequest {
  id: string;
  token: string;
}

class DeleteInternshipEngagementExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-internship-engagements',
  styleUrl: 'sc-internship-engagements.css',
  shadow: true,
})
export class ScInternshipEngagements {
  @State() internshipEngagementsResponse: ScInternshipEngagementListResponse;

  newProject: string;
  newChange_to_plan: string;
  newActive: boolean;
  newCeremony: string;
  newCommunications_complete_date: string;
  newComplete_date: string;
  newCountry_of_origin: string;
  newDisbursement_complete_date: string;
  newEnd_date: string;
  newEnd_date_override: string;
  newGrowth_plan: string;
  newInitial_end_date: string;
  newIntern: string;
  newLast_reactivated_at: string;
  newMentor: string;
  newMethodologies: string;
  newParatext_registry: string;
  newPeriodic_reports_directory: string;
  newPosition: string;
  newSensitivity: string;
  newStart_date: string;
  newStart_date_override: string;
  newStatus: string;
  newStatus_modified_at: string;
  newLast_suspended_at: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScInternshipEngagementUpdateRequest, ScInternshipEngagementUpdateResponse>('sc/internship-engagements/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.internshipEngagementsResponse = {
        error: ErrorType.NoError,
        internshipEngagements: this.internshipEngagementsResponse.internshipEngagements.map(internshipEngagement =>
          internshipEngagement.id === id ? updateResponse.internshipEngagement : internshipEngagement,
        ),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteInternshipEngagementExRequest, DeleteInternshipEngagementExResponse>('sc/internship-engagements/delete', {
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
    this.internshipEngagementsResponse = await fetchAs<ScInternshipEngagementListRequest, ScInternshipEngagementListResponse>('sc/internship-engagements/list', {
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

  change_to_planChange(event) {
    this.newChange_to_plan = event.target.value;
  }

  activeChange(event) {
    this.newActive = event.target.value;
  }

  ceremonyChange(event) {
    this.newCeremony = event.target.value;
  }

  communications_complete_dateChange(event) {
    this.newCommunications_complete_date = event.target.value;
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

  methodologiesChange(event) {
    this.newMethodologies = event.target.value;
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

  sensitivityChange(event) {
    this.newSensitivity = event.target.value;
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

  status_modified_atChange(event) {
    this.newStatus_modified_at = event.target.value;
  }

  last_suspended_atChange(event) {
    this.newLast_suspended_at = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateInternshipEngagementExRequest, CreateInternshipEngagementExResponse>('sc/internship-engagements/create-read', {
      token: globals.globalStore.state.token,
      internshipEngagement: {
        project: this.newProject,
        change_to_plan: this.newChange_to_plan,
        active: this.newActive,
        ceremony: this.newCeremony,
        communications_complete_date: this.newCommunications_complete_date,
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
        methodologies: this.newMethodologies,
        paratext_registry: this.newParatext_registry,
        periodic_reports_directory: this.newPeriodic_reports_directory,
        position: this.newPosition,
        sensitivity: this.newSensitivity,
        start_date: this.newStart_date,
        start_date_override: this.newStart_date_override,
        status: this.newStatus,
        status_modified_at: this.newStatus_modified_at,
        last_suspended_at: this.newLast_suspended_at,
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
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'project',
      displayName: 'Project',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'change_to_plan',
      displayName: 'Change To Plan',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'active',
      displayName: 'Active',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'True', value: 'true' },
        { display: 'False', value: 'false' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'ceremony',
      displayName: 'Ceremony',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'communications_complete_date',
      displayName: 'Communications Complete Date',
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
      field: 'methodologies',
      displayName: 'Methodologies',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'Paratext', value: 'Paratext' },
        { display: 'OtherWritten', value: 'OtherWritten' },
        { display: 'Render', value: 'Render' },
        { display: 'Audacity', value: 'Audacity' },
        { display: 'AdobeAudition', value: 'AdobeAudition' },
        { display: 'OtherOralTranslation', value: 'OtherOralTranslation' },
        { display: 'StoryTogether', value: 'StoryTogether' },
        { display: 'SeedCompanyMethod', value: 'SeedCompanyMethod' },
        { display: 'OneStory', value: 'OneStory' },
        { display: 'Craft2Tell', value: 'Craft2Tell' },
        { display: 'OtherOralStories', value: 'OtherOralStories' },
        { display: 'Film', value: 'Film' },
        { display: 'SignLanguage', value: 'SignLanguage' },
        { display: 'OtherVisual', value: 'OtherVisual' },
      ],
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
        { display: 'ConsultantInTraining', value: 'ConsultantInTraining' },
        { display: 'ExegeticalFacilitator', value: 'ExegeticalFacilitator' },
        { display: 'LeadershipDevelopment', value: 'LeadershipDevelopment' },
        { display: 'Mobilization', value: 'Mobilization' },
        { display: 'Personnel', value: 'Personnel' },
        { display: 'Communication', value: 'Communication' },
        { display: 'Administration', value: 'Administration' },
        { display: 'Technology', value: 'Technology' },
        { display: 'Finance', value: 'Finance' },
        { display: 'LanguageProgramManager', value: 'LanguageProgramManager' },
        { display: 'Literacy', value: 'Literacy' },
        { display: 'TranslationFacilitator', value: 'TranslationFacilitator' },
        { display: 'OralityFacilitator', value: 'OralityFacilitator' },
        { display: 'ScriptureEngagement', value: 'ScriptureEngagement' },
        { display: 'OtherAttached', value: 'OtherAttached' },
        { display: 'OtherTranslationCapacity', value: 'OtherTranslationCapacity' },
        { display: 'OtherPartnershipCapacity', value: 'OtherPartnershipCapacity' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'sensitivity',
      displayName: 'Sensitivity',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'Low', value: 'Low' },
        { display: 'Medium', value: 'Medium' },
        { display: 'High', value: 'High' },
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
        { display: 'InDevelopment', value: 'InDevelopment' },
        { display: 'DidNotDevelop', value: 'DidNotDevelop' },
        { display: 'Active', value: 'Active' },
        { display: 'DiscussingTermination', value: 'DiscussingTermination' },
        { display: 'DiscussingReactivation', value: 'DiscussingReactivation' },
        { display: 'DiscussingChangeToPlan', value: 'DiscussingChangeToPlan' },
        { display: 'DiscussingSuspension', value: 'DiscussingSuspension' },
        { display: 'FinalizingCompletion', value: 'FinalizingCompletion' },
        { display: 'ActiveChangedPlan', value: 'ActiveChangedPlan' },
        { display: 'Suspended', value: 'Suspended' },
        { display: 'Terminated', value: 'Terminated' },
        { display: 'Completed', value: 'Completed' },
        { display: 'Converted', value: 'Converted' },
        { display: 'Unapproved', value: 'Unapproved' },
        { display: 'Transferred', value: 'Transferred' },
        { display: 'NotRenewed', value: 'NotRenewed' },
        { display: 'Rejected', value: 'Rejected' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'status_modified_at',
      displayName: 'Status Modified At',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'last_suspended_at',
      displayName: 'Last Suspended At',
      width: 200,
      editable: true,
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
                <input type="text" id="project" name="project" onInput={event => this.projectChange(event)} />
              </span>
            </div>

            <div id="change_to_plan-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="change_to_plan">Change To Plan</label>
              </span>
              <span class="form-thing">
                <input type="text" id="change_to_plan" name="change_to_plan" onInput={event => this.change_to_planChange(event)} />
              </span>
            </div>

            <div id="active-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="active">Active</label>
              </span>
              <span class="form-thing">
                <select id="active" name="active" onInput={event => this.activeChange(event)}>
                  <option value="">Select Active</option>
                  <option value="true" selected={this.newActive === true}>
                    True
                  </option>
                  <option value="false" selected={this.newActive === false}>
                    False
                  </option>
                </select>
              </span>
            </div>

            <div id="ceremony-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="ceremony">Ceremony</label>
              </span>
              <span class="form-thing">
                <input type="text" id="ceremony" name="ceremony" onInput={event => this.ceremonyChange(event)} />
              </span>
            </div>

            <div id="communications_complete_date-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="communications_complete_date">Communications Complete Date</label>
              </span>
              <span class="form-thing">
                <input type="text" id="communications_complete_date" name="communications_complete_date" onInput={event => this.communications_complete_dateChange(event)} />
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
                <input type="text" id="country_of_origin" name="country_of_origin" onInput={event => this.country_of_originChange(event)} />
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
                <input type="text" id="growth_plan" name="growth_plan" onInput={event => this.growth_planChange(event)} />
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
                <input type="text" id="intern" name="intern" onInput={event => this.internChange(event)} />
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
                <input type="text" id="mentor" name="mentor" onInput={event => this.mentorChange(event)} />
              </span>
            </div>

            <div id="methodologies-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="methodologies">Methodologies</label>
              </span>
              <span class="form-thing">
                <select id="methodologies" name="methodologies" onInput={event => this.methodologiesChange(event)}>
                  <option value="">Select Methodology</option>
                  <option value="Paratext" selected={this.newMethodologies === 'Paratext'}>
                    Paratext
                  </option>
                  <option value="OtherWritten" selected={this.newMethodologies === 'OtherWritten'}>
                    OtherWritten
                  </option>
                  <option value="Render" selected={this.newMethodologies === 'Render'}>
                    Render
                  </option>
                  <option value="Audacity" selected={this.newMethodologies === 'Audacity'}>
                    Audacity
                  </option>
                  <option value="AdobeAudition" selected={this.newMethodologies === 'AdobeAudition'}>
                    AdobeAudition
                  </option>
                  <option value="OtherOralTranslation" selected={this.newMethodologies === 'OtherOralTranslation'}>
                    OtherOralTranslation
                  </option>
                  <option value="StoryTogether" selected={this.newMethodologies === 'StoryTogether'}>
                    StoryTogether
                  </option>
                  <option value="SeedCompanyMethod" selected={this.newMethodologies === 'SeedCompanyMethod'}>
                    SeedCompanyMethod
                  </option>
                  <option value="OneStory" selected={this.newMethodologies === 'OneStory'}>
                    OneStory
                  </option>
                  <option value="Craft2Tell" selected={this.newMethodologies === 'Craft2Tell'}>
                    Craft2Tell
                  </option>
                  <option value="OtherOralStories" selected={this.newMethodologies === 'OtherOralStories'}>
                    OtherOralStories
                  </option>
                  <option value="Film" selected={this.newMethodologies === 'Film'}>
                    Film
                  </option>
                  <option value="SignLanguage" selected={this.newMethodologies === 'SignLanguage'}>
                    SignLanguage
                  </option>
                  <option value="OtherVisual" selected={this.newMethodologies === 'OtherVisual'}>
                    OtherVisual
                  </option>
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
                <input type="text" id="periodic_reports_directory" name="periodic_reports_directory" onInput={event => this.periodic_reports_directoryChange(event)} />
              </span>
            </div>

            <div id="position-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="position">Position</label>
              </span>
              <span class="form-thing">
                <select id="position" name="position" onInput={event => this.positionChange(event)}>
                  <option value="">Select Position</option>
                  <option value="ConsultantInTraining" selected={this.newPosition === 'ConsultantInTraining'}>
                    ConsultantInTraining
                  </option>
                  <option value="ExegeticalFacilitator" selected={this.newPosition === 'ExegeticalFacilitator'}>
                    ExegeticalFacilitator
                  </option>
                  <option value="LeadershipDevelopment" selected={this.newPosition === 'LeadershipDevelopment'}>
                    LeadershipDevelopment
                  </option>
                  <option value="Mobilization" selected={this.newPosition === 'Mobilization'}>
                    Mobilization
                  </option>
                  <option value="Personnel" selected={this.newPosition === 'Personnel'}>
                    Personnel
                  </option>
                  <option value="Communication" selected={this.newPosition === 'Communication'}>
                    Communication
                  </option>
                  <option value="Administration" selected={this.newPosition === 'Administration'}>
                    Administration
                  </option>
                  <option value="Technology" selected={this.newPosition === 'Technology'}>
                    Technology
                  </option>
                  <option value="Finance" selected={this.newPosition === 'Finance'}>
                    Finance
                  </option>
                  <option value="LanguageProgramManager" selected={this.newPosition === 'LanguageProgramManager'}>
                    LanguageProgramManager
                  </option>
                  <option value="Literacy" selected={this.newPosition === 'Literacy'}>
                    Literacy
                  </option>
                  <option value="TranslationFacilitator" selected={this.newPosition === 'TranslationFacilitator'}>
                    TranslationFacilitator
                  </option>
                  <option value="OralityFacilitator" selected={this.newPosition === 'OralityFacilitator'}>
                    OralityFacilitator
                  </option>
                  <option value="ScriptureEngagement" selected={this.newPosition === 'ScriptureEngagement'}>
                    ScriptureEngagement
                  </option>
                  <option value="OtherAttached" selected={this.newPosition === 'OtherAttached'}>
                    OtherAttached
                  </option>
                  <option value="OtherTranslationCapacity" selected={this.newPosition === 'OtherTranslationCapacity'}>
                    OtherTranslationCapacity
                  </option>
                  <option value="OtherPartnershipCapacity" selected={this.newPosition === 'OtherPartnershipCapacity'}>
                    OtherPartnershipCapacity
                  </option>
                </select>
              </span>
            </div>

            <div id="sensitivity-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="sensitivity">Sensitivity</label>
              </span>
              <span class="form-thing">
                <select id="sensitivity" name="sensitivity" onInput={event => this.sensitivityChange(event)}>
                  <option value="">Select Sensitivity</option>
                  <option value="Low" selected={this.newSensitivity === 'Low'}>
                    Low
                  </option>
                  <option value="Medium" selected={this.newSensitivity === 'Medium'}>
                    Medium
                  </option>
                  <option value="High" selected={this.newSensitivity === 'High'}>
                    High
                  </option>
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
                  <option value="InDevelopment" selected={this.newStatus === 'InDevelopment'}>
                    InDevelopment
                  </option>
                  <option value="DidNotDevelop" selected={this.newStatus === 'DidNotDevelop'}>
                    DidNotDevelop
                  </option>
                  <option value="Active" selected={this.newStatus === 'Active'}>
                    Active
                  </option>
                  <option value="DiscussingTermination" selected={this.newStatus === 'DiscussingTermination'}>
                    DiscussingTermination
                  </option>
                  <option value="DiscussingReactivation" selected={this.newStatus === 'DiscussingReactivation'}>
                    DiscussingReactivation
                  </option>
                  <option value="DiscussingChangeToPlan" selected={this.newStatus === 'DiscussingChangeToPlan'}>
                    DiscussingChangeToPlan
                  </option>
                  <option value="DiscussingSuspension" selected={this.newStatus === 'DiscussingSuspension'}>
                    DiscussingSuspension
                  </option>
                  <option value="FinalizingCompletion" selected={this.newStatus === 'FinalizingCompletion'}>
                    FinalizingCompletion
                  </option>
                  <option value="ActiveChangedPlan" selected={this.newStatus === 'ActiveChangedPlan'}>
                    ActiveChangedPlan
                  </option>
                  <option value="Suspended" selected={this.newStatus === 'Suspended'}>
                    Suspended
                  </option>
                  <option value="Terminated" selected={this.newStatus === 'Terminated'}>
                    Terminated
                  </option>
                  <option value="Completed" selected={this.newStatus === 'Completed'}>
                    Completed
                  </option>
                  <option value="Converted" selected={this.newStatus === 'Converted'}>
                    Converted
                  </option>
                  <option value="Unapproved" selected={this.newStatus === 'Unapproved'}>
                    Unapproved
                  </option>
                  <option value="Transferred" selected={this.newStatus === 'Transferred'}>
                    Transferred
                  </option>
                  <option value="NotRenewed" selected={this.newStatus === 'NotRenewed'}>
                    NotRenewed
                  </option>
                  <option value="Rejected" selected={this.newStatus === 'Rejected'}>
                    Rejected
                  </option>
                </select>
              </span>
            </div>

            <div id="status_modified_at-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="status_modified_at">Status Modified At</label>
              </span>
              <span class="form-thing">
                <input type="text" id="status_modified_at" name="status_modified_at" onInput={event => this.status_modified_atChange(event)} />
              </span>
            </div>

            <div id="last_suspended_at-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="last_suspended_at">Last Suspended At</label>
              </span>
              <span class="form-thing">
                <input type="text" id="last_suspended_at" name="last_suspended_at" onInput={event => this.last_suspended_atChange(event)} />
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

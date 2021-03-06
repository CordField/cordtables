import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateLanguageEngagementExRequest {
  token: string;
  languageEngagement: {
    project: string;
    ethnologue: string;
    change_to_plan: string;
    active: boolean;
    ceremony: string;
    is_open_to_investor_visit: boolean;
    communications_complete_date: string;
    complete_date: string;
    disbursement_complete_date: string;
    end_date: string;
    end_date_override: string;
    initial_end_date: string;
    is_first_scripture: boolean;
    is_luke_partnership: boolean;
    is_sent_printing: boolean;
    last_suspended_at: string;
    last_reactivated_at: string;
    paratext_registry: string;
    periodic_reports_directory: string;
    pnp: string;
    pnp_file: string;
    product_engagement_tag: string;
    start_date: string;
    start_date_override: string;
    status: string;
    status_modified_at: string;
    historic_goal: string;
  };
}
class CreateLanguageEngagementExResponse extends GenericResponse {
  languageEngagement: ScLanguageEngagement;
}

class ScLanguageEngagementListRequest {
  token: string;
}

class ScLanguageEngagementListResponse {
  error: ErrorType;
  languageEngagements: ScLanguageEngagement[];
}

class ScLanguageEngagementUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScLanguageEngagementUpdateResponse {
  error: ErrorType;
  languageEngagement: ScLanguageEngagement | null = null;
}

class DeleteLanguageEngagementExRequest {
  id: string;
  token: string;
}

class DeleteLanguageEngagementExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-language-engagements',
  styleUrl: 'sc-language-engagements.css',
  shadow: true,
})
export class ScLanguageEngagements {
  @State() languageEngagementsResponse: ScLanguageEngagementListResponse;

  newProject: string;
  newEthnologue: string;
  newChange_to_plan: string;
  newActive: boolean;
  newCeremony: string;
  newIs_open_to_investor_visit: boolean;
  newCommunications_complete_date: string;
  newComplete_date: string;
  newDisbursement_complete_date: string;
  newEnd_date: string;
  newEnd_date_override: string;
  newInitial_end_date: string;
  newIs_first_scripture: boolean;
  newIs_luke_partnership: boolean;
  newIs_sent_printing: boolean;
  newLast_suspended_at: string;
  newLast_reactivated_at: string;
  newParatext_registry: string;
  newPeriodic_reports_directory: string;
  newPnp: string;
  newPnp_file: string;
  newProduct_engagement_tag: string;
  newStart_date: string;
  newStart_date_override: string;
  newStatus: string;
  newStatus_modified_at: string;
  newHistoric_goal: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScLanguageEngagementUpdateRequest, ScLanguageEngagementUpdateResponse>('sc/language-engagements/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.languageEngagementsResponse = {
        error: ErrorType.NoError,
        languageEngagements: this.languageEngagementsResponse.languageEngagements.map(languageEngagement =>
          languageEngagement.id === id ? updateResponse.languageEngagement : languageEngagement,
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
    const deleteResponse = await fetchAs<DeleteLanguageEngagementExRequest, DeleteLanguageEngagementExResponse>('sc/language-engagements/delete', {
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
    this.languageEngagementsResponse = await fetchAs<ScLanguageEngagementListRequest, ScLanguageEngagementListResponse>('sc/language-engagements/list', {
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

  ceremonyChange(event) {
    this.newCeremony = event.target.value;
  }

  is_open_to_investor_visitChange(event) {
    this.newIs_open_to_investor_visit = event.target.value;
  }

  communications_complete_dateChange(event) {
    this.newCommunications_complete_date = event.target.value;
  }

  complete_dateChange(event) {
    this.newComplete_date = event.target.value;
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

  initial_end_dateChange(event) {
    this.newInitial_end_date = event.target.value;
  }

  is_first_scriptureChange(event) {
    this.newIs_first_scripture = event.target.value;
  }

  is_luke_partnershipChange(event) {
    this.newIs_luke_partnership = event.target.value;
  }

  is_sent_printingChange(event) {
    this.newIs_sent_printing = event.target.value;
  }

  last_suspended_atChange(event) {
    this.newLast_suspended_at = event.target.value;
  }

  last_reactivated_atChange(event) {
    this.newLast_reactivated_at = event.target.value;
  }

  paratext_registryChange(event) {
    this.newParatext_registry = event.target.value;
  }

  periodic_reports_directoryChange(event) {
    this.newPeriodic_reports_directory = event.target.value;
  }

  pnpChange(event) {
    this.newPnp = event.target.value;
  }

  pnp_fileChange(event) {
    this.newPnp_file = event.target.value;
  }

  product_engagement_tagChange(event) {
    this.newProduct_engagement_tag = event.target.value;
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

  historic_goalChange(event) {
    this.newHistoric_goal = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateLanguageEngagementExRequest, CreateLanguageEngagementExResponse>('sc/language-engagements/create-read', {
      token: globals.globalStore.state.token,
      languageEngagement: {
        project: this.newProject,
        ethnologue: this.newEthnologue,
        change_to_plan: this.newChange_to_plan,
        active: this.newActive,
        ceremony: this.newCeremony,
        is_open_to_investor_visit: this.newIs_open_to_investor_visit,
        communications_complete_date: this.newCommunications_complete_date,
        complete_date: this.newComplete_date,
        disbursement_complete_date: this.newDisbursement_complete_date,
        end_date: this.newEnd_date,
        end_date_override: this.newEnd_date_override,
        initial_end_date: this.newInitial_end_date,
        is_first_scripture: this.newIs_first_scripture,
        is_luke_partnership: this.newIs_luke_partnership,
        is_sent_printing: this.newIs_sent_printing,
        last_suspended_at: this.newLast_reactivated_at,
        last_reactivated_at: this.newLast_reactivated_at,
        paratext_registry: this.newParatext_registry,
        periodic_reports_directory: this.newPeriodic_reports_directory,
        pnp: this.newPnp,
        pnp_file: this.newPnp_file,
        product_engagement_tag: this.newProduct_engagement_tag,
        start_date: this.newStart_date,
        start_date_override: this.newStart_date_override,
        status: this.newStatus,
        status_modified_at: this.newStatus_modified_at,
        historic_goal: this.newHistoric_goal,
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
      field: 'ethnologue',
      displayName: 'Ethnologue',
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
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'is_open_to_investor_visit',
      displayName: 'Is Open to Investor Visit',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'True', value: 'true' },
        { display: 'False', value: 'false' },
      ],
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
      field: 'initial_end_date',
      displayName: 'Initial End Date',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'is_first_scripture',
      displayName: 'Is First Scripture',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'True', value: 'true' },
        { display: 'False', value: 'false' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'is_luke_partnership',
      displayName: 'Is Luke Partnership',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'True', value: 'true' },
        { display: 'False', value: 'false' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'is_sent_printing',
      displayName: 'Is Sent Printing',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'True', value: 'true' },
        { display: 'False', value: 'false' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'last_suspended_at',
      displayName: 'last_suspended_at',
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
      field: 'paratext_registry',
      displayName: 'Paratext Registry',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'periodic_reports_directory',
      displayName: 'Periodic Reports Directory',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'pnp',
      displayName: 'PNP',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'pnp_file',
      displayName: 'PNP File',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'product_engagement_tag',
      displayName: 'Product Engagement Tag',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'A', value: 'A' },
        { display: 'B', value: 'B' },
        { display: 'C', value: 'C' },
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
      field: 'historic_goal',
      displayName: 'Historic Goal',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
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
        {this.languageEngagementsResponse && <cf-table rowData={this.languageEngagementsResponse.languageEngagements} columnData={this.columnData}></cf-table>}

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

            <div id="ethnologue-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="ethnologue">Ethnologue</label>
              </span>
              <span class="form-thing">
                <input type="text" id="ethnologue" name="ethnologue" onInput={event => this.ethnologueChange(event)} />
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

            <div id="is_open_to_investor_visit-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="is_open_to_investor_visit">Is open to investor visit</label>
              </span>
              <span class="form-thing">
                <select id="is_open_to_investor_visit" name="is_open_to_investor_visit" onInput={event => this.is_open_to_investor_visitChange(event)}>
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

            <div id="initial_end_date-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="initial_end_date">Initial End Date</label>
              </span>
              <span class="form-thing">
                <input type="text" id="initial_end_date" name="initial_end_date" onInput={event => this.initial_end_dateChange(event)} />
              </span>
            </div>

            <div id="is_first_scripture-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="is_first_scripture">Is First Scripture</label>
              </span>
              <span class="form-thing">
                <select id="is_first_scripture" name="is_first_scripture" onInput={event => this.is_first_scriptureChange(event)}>
                  <option value="">Select Active</option>
                  <option value="true" selected={this.newIs_first_scripture === true}>
                    True
                  </option>
                  <option value="false" selected={this.newIs_first_scripture === false}>
                    False
                  </option>
                </select>
              </span>
            </div>

            <div id="is_luke_partnership-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="is_luke_partnership">Is Luke Partnership</label>
              </span>
              <span class="form-thing">
                <select id="is_luke_partnership" name="is_luke_partnership" onInput={event => this.is_luke_partnershipChange(event)}>
                  <option value="">Select Is Luke Partnership</option>
                  <option value="true" selected={this.newIs_luke_partnership === true}>
                    True
                  </option>
                  <option value="false" selected={this.newIs_luke_partnership === false}>
                    False
                  </option>
                </select>
              </span>
            </div>

            <div id="is_sent_printing-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="is_sent_printing">Is Sent Printing</label>
              </span>
              <span class="form-thing">
                <select id="is_sent_printing" name="is_sent_printing" onInput={event => this.is_sent_printingChange(event)}>
                  <option value="">Select Is Sent Printing</option>
                  <option value="true" selected={this.newIs_sent_printing === true}>
                    True
                  </option>
                  <option value="false" selected={this.newIs_sent_printing === false}>
                    False
                  </option>
                </select>
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

            <div id="last_reactivated_at-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="last_reactivated_at">Last Reactivated At</label>
              </span>
              <span class="form-thing">
                <input type="text" id="last_reactivated_at" name="last_reactivated_at" onInput={event => this.last_reactivated_atChange(event)} />
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

            <div id="pnp-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="pnp">PNP</label>
              </span>
              <span class="form-thing">
                <input type="text" id="pnp" name="pnp" onInput={event => this.pnpChange(event)} />
              </span>
            </div>

            <div id="pnp_file-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="pnp_file">PNP File</label>
              </span>
              <span class="form-thing">
                <input type="text" id="pnp_file" name="pnp_file" onInput={event => this.pnp_fileChange(event)} />
              </span>
            </div>

            <div id="product_engagement_tag-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="product_engagement_tag">Product Engagement Tag</label>
              </span>
              <span class="form-thing">
                <select id="product_engagement_tag" name="product_engagement_tag" onInput={event => this.product_engagement_tagChange(event)}>
                  <option value="">Select Product Engagement Tag</option>
                  <option value="A" selected={this.newProduct_engagement_tag === 'A'}>
                    A
                  </option>
                  <option value="B" selected={this.newProduct_engagement_tag === 'B'}>
                    B
                  </option>
                  <option value="C" selected={this.newProduct_engagement_tag === 'C'}>
                    C
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

            <div id="historic_goal-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="historic_goal">Historic Goal</label>
              </span>
              <span class="form-thing">
                <input type="text" id="historic_goal" name="historic_goal" onInput={event => this.historic_goalChange(event)} />
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

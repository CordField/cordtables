import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateProjectExRequest {
  token: string;
  project: {
    neo4j_id: string;
    name: string;
    change_to_plan: number;
    active: boolean;
    department: string;
    estimated_submission: string;
    field_region: number;
    initial_mou_end: string;
    marketing_location: number;
    mou_start: string;
    mou_end: string;
    owning_organization: number;
    periodic_reports_directory: number;
    posts_directory: number;
    primary_location: number;
    root_directory: number;
    status: string;
    status_changed_at: string;
    step: string;
  };
}
class CreateProjectExResponse extends GenericResponse {
  project: ScProject;
}

class ScProjectListRequest {
  token: string;
}

class ScProjectListResponse {
  error: ErrorType;
  projects: ScProject[];
}


class ScProjectUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class ScProjectUpdateResponse {
  error: ErrorType;
  project: ScProject | null = null;
}

class DeleteProjectExRequest {
  id: number;
  token: string;
}

class DeleteProjectExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'sc-projects',
  styleUrl: 'sc-projects.css',
  shadow: true,
})
export class ScProjects {

  @State() projectsResponse: ScProjectListResponse;

  newNeo4j_id: string;
  newName: string;
  newChange_to_plan: number;
  newActive: boolean;
  newDepartment: string;
  newEstimated_submission: string;
  newField_region: number;
  newInitial_mou_end: string;
  newMarketing_location: number;
  newMou_start: string;
  newMou_end: string;
  newOwning_organization: number;
  newPeriodic_reports_directory: number;
  newPosts_directory: number;
  newPrimary_location: number;
  newRoot_directory: number;
  newStatus: string;
  newStatus_changed_at: string;
  newStep: string;
  
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScProjectUpdateRequest, ScProjectUpdateResponse>('sc-projects/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.projectsResponse = { error: ErrorType.NoError, projects: this.projectsResponse.projects.map(project => (project.id === id ? updateResponse.project : project)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteProjectExRequest, DeleteProjectExResponse>('sc-projects/delete', {
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
    this.projectsResponse = await fetchAs<ScProjectListRequest, ScProjectListResponse>('sc-projects/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  neo4j_idChange(event) {
    this.newNeo4j_id = event.target.value;
  }

  nameChange(event) {
    this.newName = event.target.value;
  }

  change_to_planChange(event) {
    this.newChange_to_plan = event.target.value;
  }

  activeChange(event) {
    this.newActive = event.target.value;
  }

  departmentChange(event) {
    this.newDepartment = event.target.value;
  }

  estimated_submissionChange(event) {
    this.newEstimated_submission = event.target.value;
  }

  field_regionChange(event) {
    this.newField_region = event.target.value;
  }

  initial_mou_endChange(event) {
    this.newInitial_mou_end = event.target.value;
  }

  marketing_locationChange(event) {
    this.newMarketing_location = event.target.value;
  }

  mou_startChange(event) {
    this.newMou_start = event.target.value;
  }

  mou_endChange(event) {
    this.newMou_end = event.target.value;
  }

  owning_organizationChange(event) {
    this.newOwning_organization = event.target.value;
  }

  periodic_reports_directoryChange(event) {
    this.newPeriodic_reports_directory = event.target.value;
  }

  posts_directoryChange(event) {
    this.newPosts_directory = event.target.value;
  }

  primary_locationChange(event) {
    this.newPrimary_location = event.target.value;
  }

  root_directoryChange(event) {
    this.newRoot_directory = event.target.value;
  }

  statusChange(event) {
    this.newStatus = event.target.value;
  }

  status_changed_atChange(event) {
    this.newStatus_changed_at = event.target.value;
  }

  stepChange(event) {
    this.newStep = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateProjectExRequest, CreateProjectExResponse>('sc-projects/create-read', {
      token: globals.globalStore.state.token,
      project: {
        neo4j_id: this.newNeo4j_id,
        name: this.newName,
        change_to_plan: this.newChange_to_plan,
        active: this.newActive,
        department: this.newDepartment,
        estimated_submission: this.newEstimated_submission,
        field_region: this.newField_region,
        initial_mou_end: this.newInitial_mou_end,
        marketing_location: this.newMarketing_location,
        mou_start: this.newMou_start,
        mou_end: this.newMou_end,
        owning_organization: this.newOwning_organization,
        periodic_reports_directory: this.newPeriodic_reports_directory,
        posts_directory: this.newPosts_directory,
        primary_location: this.newPrimary_location,
        root_directory: this.newRoot_directory,
        status: this.newStatus,
        status_changed_at: this.newStatus_changed_at,
        step: this.newStep,
        
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
      field: 'neo4j_id',
      displayName: 'neo4j_id',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'name',
      displayName: 'Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'change_to_plan',
      displayName: 'change_to_plan',
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
      field: 'department',
      displayName: 'Department',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'estimated_submission',
      displayName: 'Estimated Submission',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'field_region',
      displayName: 'Field Region',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'initial_mou_end',
      displayName: 'Initial MOU End',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'marketing_location',
      displayName: 'Marketing Location',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'mou_start',
      displayName: 'MOU Start',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'mou_end',
      displayName: 'MOU End',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'owning_organization',
      displayName: 'Owning Organization',
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
      field: 'posts_directory',
      displayName: 'Posts Directory',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'primary_location',
      displayName: 'Primary Location',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'root_directory',
      displayName: 'Root Directory',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'status',
      displayName: 'Status',
      width: 200,
      editable: true,
      isMulti: true,
      selectOptions: [
        {display: "A", value: "A"},
        {display: "B", value: "B"},
        {display: "C", value: "C"},
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'status_changed_at',
      displayName: 'Status Changed At',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'step',
      displayName: 'Step',
      width: 250,
      editable: true,
      isMulti: true,
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
        {this.projectsResponse && <cf-table rowData={this.projectsResponse.projects} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">



            <div id="neo4j_id-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="neo4j_id">neo4j_id</label>
              </span>
              <span class="form-thing">
                <input type="text" id="neo4j_id" name="neo4j_id" onInput={event => this.neo4j_idChange(event)} />
              </span>
            </div>

            <div id="name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="name">Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="name" name="name" onInput={event => this.nameChange(event)} />
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

            <div id="department-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="department">Department</label>
              </span>
              <span class="form-thing">
                <input type="text" id="department" name="department" onInput={event => this.departmentChange(event)} />
              </span>
            </div>

            <div id="estimated_submission-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="estimated_submission">Estimated Submission</label>
              </span>
              <span class="form-thing">
                <input type="text" id="estimated_submission" name="estimated_submission" onInput={event => this.estimated_submissionChange(event)} />
              </span>
            </div>    

            <div id="field_region-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="field_region">Field Region</label>
              </span>
              <span class="form-thing">
                <input type="number" id="field_region" name="field_region" onInput={event => this.field_regionChange(event)} />
              </span>
            </div>

            <div id="initial_mou_end-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="initial_mou_end">Initial MOU End</label>
              </span>
              <span class="form-thing">
                <input type="text" id="initial_mou_end" name="initial_mou_end" onInput={event => this.initial_mou_endChange(event)} />
              </span>
            </div>    

            <div id="marketing_location-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="marketing_location">Marketing Location</label>
              </span>
              <span class="form-thing">
                <input type="number" id="marketing_location" name="marketing_location" onInput={event => this.marketing_locationChange(event)} />
              </span>
            </div>

            <div id="mou_start-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="mou_start">MOU Start</label>
              </span>
              <span class="form-thing">
                <input type="text" id="persmou_starton" name="mou_start" onInput={event => this.mou_startChange(event)} />
              </span>
            </div>    

            <div id="mou_end-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="mou_end">MOU End</label>
              </span>
              <span class="form-thing">
                <input type="text" id="mou_end" name="mou_end" onInput={event => this.mou_endChange(event)} />
              </span>
            </div>

            <div id="owning_organization-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="owning_organization">Owning Organization</label>
              </span>
              <span class="form-thing">
                <input type="text" id="owning_organization" name="owning_organization" onInput={event => this.owning_organizationChange(event)} />
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

            <div id="posts_directory-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="posts_directory">Posts Directory</label>
              </span>
              <span class="form-thing">
                <input type="number" id="posts_directory" name="posts_directory" onInput={event => this.posts_directoryChange(event)} />
              </span>
            </div>    

            <div id="primary_location-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="primary_location">Primary Location</label>
              </span>
              <span class="form-thing">
                <input type="number" id="primary_location" name="primary_location" onInput={event => this.primary_locationChange(event)} />
              </span>
            </div>

            <div id="root_directory-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="root_directory">Root Directory</label>
              </span>
              <span class="form-thing">
                <input type="text" id="root_directory" name="root_directory" onInput={event => this.root_directoryChange(event)} />
              </span>
            </div>    

            <div id="status-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="status">Status</label>
              </span>
              <span class="form-thing">
              <select id="status" name="status" multiple onInput={event => this.statusChange(event)}>
                  <option value="">Select Status</option>
                  <option value="A" selected={this.newStatus === "A"}>A</option>
                  <option value="B" selected={this.newStatus === "B"}>B</option>
                  <option value="C" selected={this.newStatus === "C"}>C</option>
                </select>
              </span>
            </div>

            <div id="status_changed_at-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="status_changed_at">Status Changed At</label>
              </span>
              <span class="form-thing">
                <input type="text" id="status_changed_at" name="status_changed_at" onInput={event => this.status_changed_atChange(event)} />
              </span>
            </div>    

            <div id="step-holder" class="form-input-item form-thing">
              <span class="form-step">
                <label htmlFor="step">Step</label>
              </span>
              <span class="form-thing">
              <select id="step" name="step" multiple onInput={event => this.stepChange(event)}>
                  <option value="">Select Step</option>
                  <option value="A" selected={this.newStep === "A"}>A</option>
                  <option value="B" selected={this.newStep === "B"}>B</option>
                  <option value="C" selected={this.newStep === "C"}>C</option>
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

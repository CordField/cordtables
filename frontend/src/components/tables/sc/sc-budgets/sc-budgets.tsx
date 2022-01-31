import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateBudgetExRequest {
  token: string;
  budget: {
    change_to_plan: string;
    project: string;
    status: string;
    universal_template: string;
    universal_template_file_url: string;
    sensitivity: string;
    total: number;
  };
}
class CreateBudgetExResponse extends GenericResponse {
  budget: ScBudget;
}

class ScBudgetListRequest {
  token: string;
}

class ScBudgetListResponse {
  error: ErrorType;
  budgets: ScBudget[];
}

class ScBudgetUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScBudgetUpdateResponse {
  error: ErrorType;
  budget: ScBudget | null = null;
}

class DeleteBudgetExRequest {
  id: string;
  token: string;
}

class DeleteBudgetExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-budgets',
  styleUrl: 'sc-budgets.css',
  shadow: true,
})
export class ScBudgets {
  @State() budgetsResponse: ScBudgetListResponse;

  newChange_to_plan: string;
  newProject: string;
  newStatus: string;
  newUniversal_template: string;
  newUniversal_template_file_url: string;
  newSensitivity: string;
  newTotal: number;
  

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScBudgetUpdateRequest, ScBudgetUpdateResponse>('sc/budgets/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.budgetsResponse = {
        error: ErrorType.NoError,
        budgets: this.budgetsResponse.budgets.map(budget => (budget.id === id ? updateResponse.budget : budget)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteBudgetExRequest, DeleteBudgetExResponse>('sc/budgets/delete', {
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
    this.budgetsResponse = await fetchAs<ScBudgetListRequest, ScBudgetListResponse>('sc/budgets/list', {
      token: globals.globalStore.state.token,
    });
  }

  change_to_planChange(event) {
    this.newChange_to_plan = event.target.value;
  }

  projectChange(event) {
    this.newProject = event.target.value;
  }

  statusChange(event) {
    this.newStatus = event.target.value;
  }

  universal_templateChange(event) {
    this.newUniversal_template = event.target.value;
  }

  universal_template_file_urlChange(event) {
    this.newUniversal_template_file_url = event.target.value;
  }

  sensitivityChange(event) {
    this.newSensitivity = event.target.value;
  }
  
  totalChange(event) {
    this.newTotal = event.target.value;
  }


  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateBudgetExRequest, CreateBudgetExResponse>('sc/budgets/create-read', {
      token: globals.globalStore.state.token,
      budget: {
        change_to_plan: this.newChange_to_plan,
        project: this.newProject,
        status: this.newStatus,
        universal_template: this.newUniversal_template,
        universal_template_file_url: this.newUniversal_template_file_url,
        sensitivity: this.newSensitivity,
        total: this.newTotal,
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

  change_to_plan: string;
    project: string;
    status: string;
    universal_template: string;
    universal_template_file_url: string;
    sensitivity: string;
    total: number;

  columnData: ColumnDescription[] = [
    {
      field: 'id',
      displayName: 'ID',
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'change_to_plan',
      displayName: 'Change To Plan',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'project',
      displayName: 'Project',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'status',
      displayName: 'Status',
      width: 250,
      editable: true,
      selectOptions: [
        {display:'Pending', value:'Pending'},
        {display:'Current', value:'Current'},
        {display:'Superseded', value:'Superseded'},
        {display:'Rejected', value:'Rejected'}
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'universal_template',
      displayName: 'Universal Template',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'universal_template_file_url',
      displayName: 'Universal Template File Url',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'sensitivity',
      displayName: 'Sensitivity',
      width: 250,
      editable: true,
      selectOptions: [
        { display: 'Low', value: 'Low' },
        { display: 'Medium', value: 'Medium' },
        { display: 'High', value: 'High' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'total',
      displayName: 'Total',
      width: 250,
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
        {this.budgetsResponse && <cf-table rowData={this.budgetsResponse.budgets} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">




            <div id="change_to_plan-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="change_to_plan">Change_To Plan</label>
              </span>
              <span class="form-thing">
                <input type="text" id="change_to_plan" name="change_to_plan" onInput={event => this.change_to_planChange(event)} />
              </span>
            </div>

            <div id="project-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="project">Project</label>
              </span>
              <span class="form-thing">
                <input type="text" id="project" name="project" onInput={event => this.projectChange(event)} />
              </span>
            </div>


            <div id="status-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="status">Status</label>
              </span>
              <span class="form-thing">
                <select id="status" name="status" onInput={event => this.statusChange(event)}>
                  <option value="">Select Status</option>
                  <option value="Pending" selected={this.newSensitivity === 'Pending'}>Pending</option>
                  <option value="Current" selected={this.newSensitivity === 'Current'}>Current</option>
                  <option value="Superseded" selected={this.newSensitivity === 'Superseded'}>Superseded</option>
                  <option value="Rejected" selected={this.newSensitivity === 'Rejected'}>Rejected</option>
                </select>
              </span>
            </div>

            <div id="universal_template-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="universal_template">Universal Template</label>
              </span>
              <span class="form-thing">
                <input type="text" id="universal_template" name="universal_template" onInput={event => this.universal_templateChange(event)} />
              </span>
            </div>

            <div id="universal_template_file_url-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="universal_template_file_url">Universal Template File URL</label>
              </span>
              <span class="form-thing">
                <input type="text" id="universal_template_file_url" name="universal_template_file_url" onInput={event => this.universal_template_file_urlChange(event)} />
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


            <div id="total-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="total">Total</label>
              </span>
              <span class="form-thing">
                <input type="text" id="total" name="total" onInput={event => this.totalChange(event)} />
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

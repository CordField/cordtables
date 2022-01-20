import { Component, Host, State, h } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { ScBudgetRecord } from './types';

class CreateBudgetRecordRequest {
  token: string;
  budget_record: ScBudgetRecord;
}
class CreateBudgetRecordResponse extends GenericResponse {
  budget_record: ScBudgetRecord;
}

class ScBudgetRecordsListRequest {
  token: string;
}

class ScBudgetRecordsListResponse {
  error: ErrorType;
  budget_records: ScBudgetRecord[];
}

class ScBudgetRecordsUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScBudgetRecordUpdateResponse {
  error: ErrorType;
  budget_record: ScBudgetRecord | null = null;
}

class DeleteBudgetRecordRequest {
  id: string;
  token: string;
}

class DeleteBudgetRecordResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-budget-records',
  styleUrl: 'sc-budget-records.css',
  shadow: true,
})
export class ScBudgetRecords {
  @State() budgetrecordsResponse: ScBudgetRecordsListResponse;

  newBudget: string;
  newChange_to_plan: string;
  newActive: boolean;
  newAmount: number;
  newFiscal_year: number;
  newOrganization: string;
  newSensitivity: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScBudgetRecordsUpdateRequest, ScBudgetRecordUpdateResponse>('sc/budget-records/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      return true;
    } else {
      alert(updateResponse.error);
      return false;
    }
  };

  handleDelete = async id => {
    const result = await fetchAs<DeleteBudgetRecordRequest, DeleteBudgetRecordResponse>('sc/budget-records/delete', {
      id,
      token: globals.globalStore.state.token,
    });
    if (result.error === ErrorType.NoError) {
      this.getList();
      return true;
    } else {
      return false;
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
      field: 'budget',
      displayName: 'Budget ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'change_to_plan',
      displayName: 'Change to Plan',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'active',
      displayName: 'Active',
      width: 50,
      editable: true,
      selectOptions: [
        { display: 'True', value: 'true' },
        { display: 'False', value: 'false' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'amount',
      displayName: 'Amount',
      width: 80,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'fiscal_year',
      displayName: 'Fiscal Year',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'organization',
      displayName: 'organization',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'sensitivity',
      displayName: 'sensitivity',
      width: 100,
      editable: true,
      selectOptions: [
        { display: 'Low', value: 'Low' },
        { display: 'Medium', value: 'Medium' },
        { display: 'High', value: 'High' },
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
  }

  async getList() {
    this.budgetrecordsResponse = await fetchAs<ScBudgetRecordsListRequest, ScBudgetRecordsListResponse>('sc/budget-records/list', {
      token: globals.globalStore.state.token,
    });
  }


  budgetChange(event) {
    this.newBudget = event.target.value;
  }

  change_to_planChange(event) {
    this.newChange_to_plan = event.target.value;
  }

  activeChange(event) {
    this.newActive = event.target.value;
  }

  amountChange(event) {
    this.newAmount = event.target.value;
  }

  fiscal_yearChange(event) {
    this.newFiscal_year = event.target.value;
  }

  organizationChange(event) {
    this.newOrganization = event.target.value;
  }

  sensitivityChange(event) {
    this.newSensitivity = event.target.value;
  }



  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const result = await fetchAs<CreateBudgetRecordRequest, CreateBudgetRecordResponse>('sc/budget-records/create-read', {
      token: globals.globalStore.state.token,
      budget_record: {
        budget: this.newBudget,
        change_to_plan: this.newChange_to_plan,
        active: this.newActive,
        amount: this.newAmount,
        fiscal_year: this.newFiscal_year,
        organization: this.newOrganization,
        sensitivity: this.newSensitivity,
      },
    });

    if (result.error === ErrorType.NoError) {
      // globals.globalStore.state.editMode = false;
      this.getList();
    }
  };
  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.budgetrecordsResponse && <cf-table rowData={this.budgetrecordsResponse.budget_records} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">

            <div id="budget-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="budget">Budget ID</label>
              </span>
              <span class="form-thing">
                <input id="budget" name="budget" onInput={event => this.budgetChange(event)} />
              </span>
            </div>

            <div id="change_to_plan-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="change_to_plan">Change To Plan</label>
              </span>
              <span class="form-thing">
                <input id="change_to_plan" name="change_to_plan" onInput={event => this.change_to_planChange(event)} />
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

            <div id="amount-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="amount">Amount</label>
              </span>
              <span class="form-thing">
                <input id="amount" name="amount" onInput={event => this.amountChange(event)} />
              </span>
            </div>

            <div id="fiscal_year-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="fiscal_year">Fiscal Year</label>
              </span>
              <span class="form-thing">
                <input id="fiscal_year" name="fiscal_year" onInput={event => this.fiscal_yearChange(event)} />
              </span>
            </div>

            <div id="organization-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="budget">Organization</label>
              </span>
              <span class="form-thing">
                <input id="organization" name="organization" onInput={event => this.organizationChange(event)} />
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
      

            <span class="form-thing">
              <input id="create-button" type="submit" value="Create" onClick={this.handleInsert} />
            </span>
          </form>
        )}
      </Host>
    );
  }
}

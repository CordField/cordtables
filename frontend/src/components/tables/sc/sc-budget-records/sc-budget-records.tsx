import { Component, Host, State, h } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { ScBudgetRecord } from './types';

class CreateBudgetRecordRequest {
  token: string;
  budgetrecord: ScBudgetRecord;
}
class CreateBudgetRecordResponse extends GenericResponse {
  budgetrecord: ScBudgetRecord;
}

class ScBudgetRecordsListRequest {
  token: string;
}

class ScBudgetRecordsListResponse {
  error: ErrorType;
  budgetrecords: ScBudgetRecord[];
}

class ScBudgetRecordsUpdateRequest {
  token: string;
  budgetrecord: ScBudgetRecord;
}

class ScBudgetRecordUpdateResponse {
  error: ErrorType;
  budgetrecord: ScBudgetRecord | null = null;
}

class DeleteBudgetRecordRequest {
  id: number;
  token: string;
}

class DeleteBudgetRecordResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'sc-budgetrecords',
  styleUrl: 'sc-budget-records.css',
  shadow: true,
})
export class ScBudgetRecords {
  @State() budgetrecordsResponse: ScBudgetRecordsListResponse;
  newBudgetRecordName: string;
  newBudgetRecordType: string = 'City';

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScBudgetRecordsUpdateRequest, ScBudgetRecordUpdateResponse>('sc-budgetrecords/update-read', {
      token: globals.globalStore.state.token,
      budgetrecord: {
        id: id,
        [columnName]: value,
      },
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
    const result = await fetchAs<DeleteBudgetRecordRequest, DeleteBudgetRecordResponse>('sc-budgetrecords/delete', {
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
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'budget',
      displayName: 'Budget',
      width: 10,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'change_to_plan',
      displayName: 'Change to Plan',
      width: 10,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'active',
      displayName: 'Active',
      width: 5,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'amount',
      displayName: 'Amount',
      width: 50,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'fiscal_year',
      displayName: 'Fiscal Year',
      width: 10,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'partnership',
      displayName: 'Partnership',
      width: 10,
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
  }

  async getList() {
    this.budgetrecordsResponse = await fetchAs<ScBudgetRecordsListRequest, ScBudgetRecordsListResponse>('sc-budgetrecords/list', {
      token: globals.globalStore.state.token,
    });
  }

  budgetrecordBudgetChange(event) {
    this.newBudgetRecordName = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    // const result = await fetchAs<CreateBudgetRecordRequest, CreateBudgetRecordResponse>('sc-budgetrecords/create-read', {
    //   token: globals.globalStore.state.token,
    //   budgetrecord: {
    //     name: this.newBudgetRecordName,
    //     type: this.newBudgetRecordType
    //   },
    // });

    // if (result.error === ErrorType.NoError) {
    //   // globals.globalStore.state.editMode = false;
    //   this.getList();
    // }
  };
  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.budgetrecordsResponse && <cf-table rowData={this.budgetrecordsResponse.budgetrecords} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="budgetrecord-budget-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="budgetrecord-budget">New BudgetRecord Budget</label>
              </span>
              {/* <span class="form-thing">
                <input id="budgetrecord-name" budget="budgetrecord-budget" onInput={event => this.budgetrecordBudgetChange(event)} />
              </span> */}
            </div>
            <div id="budgetrecord-change_to_plan-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="budgetrecord-change_to_plan">BudgetRecord Type</label>
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

import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateBudgetRecordsPartnershipExRequest {
  token: string;
  budgetRecordsPartnership: {
    budget_record: string;
    partnership: string;
  };
}
class CreateBudgetRecordsPartnershipExResponse extends GenericResponse {
  budgetRecordsPartnership: ScBudgetRecordsPartnership;
}

class ScBudgetRecordsPartnershipListRequest {
  token: string;
}

class ScBudgetRecordsPartnershipListResponse {
  error: ErrorType;
  budgetRecordsPartnerships: ScBudgetRecordsPartnership[];
}

class ScBudgetRecordsPartnershipUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScBudgetRecordsPartnershipUpdateResponse {
  error: ErrorType;
  budgetRecordsPartnership: ScBudgetRecordsPartnership | null = null;
}

class DeleteBudgetRecordsPartnershipExRequest {
  id: string;
  token: string;
}

class DeleteBudgetRecordsPartnershipExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-budget-records-partnerships',
  styleUrl: 'sc-budget-records-partnerships.css',
  shadow: true,
})
export class ScBudgetRecordsPartnerships {
  @State() budgetRecordsPartnershipsResponse: ScBudgetRecordsPartnershipListResponse;

  newBudget_record: string;
  newPartnership: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScBudgetRecordsPartnershipUpdateRequest, ScBudgetRecordsPartnershipUpdateResponse>('sc/budget-records-partnerships/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.budgetRecordsPartnershipsResponse = {
        error: ErrorType.NoError,
        budgetRecordsPartnerships: this.budgetRecordsPartnershipsResponse.budgetRecordsPartnerships.map(budgetRecordsPartnership => (budgetRecordsPartnership.id === id ? updateResponse.budgetRecordsPartnership : budgetRecordsPartnership)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteBudgetRecordsPartnershipExRequest, DeleteBudgetRecordsPartnershipExResponse>('sc/budget-records-partnerships/delete', {
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
    this.budgetRecordsPartnershipsResponse = await fetchAs<ScBudgetRecordsPartnershipListRequest, ScBudgetRecordsPartnershipListResponse>('sc/budget-records-partnerships/list', {
      token: globals.globalStore.state.token,
    });
  }

  budget_recordChange(event) {
    this.newBudget_record = event.target.value;
  }

  partnershipChange(event) {
    this.newPartnership = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateBudgetRecordsPartnershipExRequest, CreateBudgetRecordsPartnershipExResponse>('sc/budget-records-partnerships/create-read', {
      token: globals.globalStore.state.token,
      budgetRecordsPartnership: {
        budget_record: this.newBudget_record,
        partnership: this.newPartnership,
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
      field: 'budget_record',
      displayName: 'Budget Record',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'partnership',
      displayName: 'partnership',
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
        {this.budgetRecordsPartnershipsResponse && <cf-table rowData={this.budgetRecordsPartnershipsResponse.budgetRecordsPartnerships} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="budget_record-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="budget_record">Budget Record</label>
              </span>
              <span class="form-thing">
                <input type="text" id="director" name="director" onInput={event => this.budget_recordChange(event)} />
              </span>
            </div>

            <div id="partnership-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="partnership">Partnership</label>
              </span>
              <span class="form-thing">
                <input type="text" id="partnership" name="partnership" onInput={event => this.partnershipChange(event)} />
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

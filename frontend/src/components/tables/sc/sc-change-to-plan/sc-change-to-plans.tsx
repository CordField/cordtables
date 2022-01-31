import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateChangeToPlanExRequest {
  token: string;
  changeToPlan: {
    status: string;
    summary: string;
    type: string;
  };
}
class CreateChangeToPlanExResponse extends GenericResponse {
  changeToPlan: ScChangeToPlan;
}

class ScChangeToPlanListRequest {
  token: string;
}

class ScChangeToPlanListResponse {
  error: ErrorType;
  changeToPlans: ScChangeToPlan[];
}

class ScChangeToPlanUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScChangeToPlanUpdateResponse {
  error: ErrorType;
  changeToPlan: ScChangeToPlan | null = null;
}

class DeleteChangeToPlanExRequest {
  id: string;
  token: string;
}

class DeleteChangeToPlanExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-change-to-plans',
  styleUrl: 'sc-change-to-plans.css',
  shadow: true,
})
export class ScChangeToPlans {
  @State() changeToPlansResponse: ScChangeToPlanListResponse;

  newStatus: string;
  newSummary: string;
  newType: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScChangeToPlanUpdateRequest, ScChangeToPlanUpdateResponse>('sc/change-to-plans/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.changeToPlansResponse = {
        error: ErrorType.NoError,
        changeToPlans: this.changeToPlansResponse.changeToPlans.map(changeToPlan => (changeToPlan.id === id ? updateResponse.changeToPlan : changeToPlan)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteChangeToPlanExRequest, DeleteChangeToPlanExResponse>('sc/change-to-plans/delete', {
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
    this.changeToPlansResponse = await fetchAs<ScChangeToPlanListRequest, ScChangeToPlanListResponse>('sc/change-to-plans/list', {
      token: globals.globalStore.state.token,
    });
  }

  statusChange(event) {
    this.newStatus = event.target.value;
  }
  summaryChange(event) {
    this.newSummary = event.target.value;
  }
  typeChange(event) {
    this.newType = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateChangeToPlanExRequest, CreateChangeToPlanExResponse>('sc/change-to-plans/create-read', {
      token: globals.globalStore.state.token,
      changeToPlan: {
        status: this.newStatus,
        summary: this.newSummary,
        type: this.newType
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
      field: 'status',
      displayName: 'Status',
      width: 250,
      editable: true,
      selectOptions: [
        {display: 'a', value: 'a'},
        {display: 'b', value: 'b'},
        {display: 'c', value: 'c'},
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'summary',
      displayName: 'Summary',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'type',
      displayName: 'Type',
      width: 250,
      editable: true,
      selectOptions: [
        {display: 'a', value: 'a'},
        {display: 'b', value: 'b'},
        {display: 'c', value: 'c'},
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
        {this.changeToPlansResponse && <cf-table rowData={this.changeToPlansResponse.changeToPlans} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="status-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="status">Status</label>
              </span>
              <span class="form-thing">
                <select id="status" name="status" onInput={event => this.statusChange(event)}>
                  <option value="">Select Status</option>
                  <option value="a" selected={this.newStatus === 'a'}>a</option>
                  <option value="b" selected={this.newStatus === 'b'}>b</option>
                  <option value="c" selected={this.newStatus === 'c'}>c</option>
                </select>
              </span>
            </div>

            <div id="summary-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="summary">Summary</label>
              </span>
              <span class="form-thing">
                <input type="text" id="summary" name="summary" onInput={event => this.summaryChange(event)} />
              </span>
            </div>

            <div id="type-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="type">Type</label>
              </span>
              <span class="form-thing">
                <select id="type" name="type" onInput={event => this.typeChange(event)}>
                  <option value="">Select Type</option>
                  <option value="a" selected={this.newStatus === 'a'}>a</option>
                  <option value="b" selected={this.newStatus === 'b'}>b</option>
                  <option value="c" selected={this.newStatus === 'c'}>c</option>
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



import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreatePersonUnavailabilityExRequest {
  token: string;
  personUnavailability: {
    person: number;
    period_start: string;
    period_end: string;
    description: string;
  };
}
class CreatePersonUnavailabilityExResponse extends GenericResponse {
  personUnavailability: ScPersonUnavailability;
}

class ScPersonUnavailabilityListRequest {
  token: string;
}

class ScPersonUnavailabilityListResponse {
  error: ErrorType;
  personUnavailabilities: ScPersonUnavailability[];
}

class ScPersonUnavailabilityUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class ScPersonUnavailabilityUpdateResponse {
  error: ErrorType;
  personUnavailability: ScPersonUnavailability | null = null;
}

class DeletePersonUnavailabilityExRequest {
  id: number;
  token: string;
}

class DeletePersonUnavailabilityExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'sc-person-unavailabilities',
  styleUrl: 'sc-person-unavailabilities.css',
  shadow: true,
})
export class ScPersonUnavailabilities {
  @State() SpersonUnavailabilitiesResponse: ScPersonUnavailabilityListResponse;

  newPerson: number;
  newPeriod_start: string;
  newPeriod_end: string;
  newDescription: string;

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScPersonUnavailabilityUpdateRequest, ScPersonUnavailabilityUpdateResponse>('sc/person-unavailabilities/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.SpersonUnavailabilitiesResponse = {
        error: ErrorType.NoError,
        personUnavailabilities: this.SpersonUnavailabilitiesResponse.personUnavailabilities.map(personUnavailability =>
          personUnavailability.id === id ? updateResponse.personUnavailability : personUnavailability,
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
    const deleteResponse = await fetchAs<DeletePersonUnavailabilityExRequest, DeletePersonUnavailabilityExResponse>('sc/person-unavailabilities/delete', {
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
    this.SpersonUnavailabilitiesResponse = await fetchAs<ScPersonUnavailabilityListRequest, ScPersonUnavailabilityListResponse>('sc/person-unavailabilities/list', {
      token: globals.globalStore.state.token,
    });
  }

  personChange(event) {
    this.newPerson = event.target.value;
  }

  period_startChange(event) {
    this.newPeriod_start = event.target.value;
  }

  period_endChange(event) {
    this.newPeriod_end = event.target.value;
  }

  descriptionChange(event) {
    this.newDescription = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreatePersonUnavailabilityExRequest, CreatePersonUnavailabilityExResponse>('sc/person-unavailabilities/create-read', {
      token: globals.globalStore.state.token,
      personUnavailability: {
        person: this.newPerson,
        period_start: this.newPeriod_start,
        period_end: this.newPeriod_end,
        description: this.newDescription,
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
      field: 'person',
      displayName: 'Person',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'period_start',
      displayName: 'Period Start',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'period_end',
      displayName: 'Period End',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'description',
      displayName: 'Description',
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
        {this.SpersonUnavailabilitiesResponse && <cf-table rowData={this.SpersonUnavailabilitiesResponse.personUnavailabilities} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="person-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="person">Person</label>
              </span>
              <span class="form-thing">
                <input type="text" id="person" name="person" onInput={event => this.personChange(event)} />
              </span>
            </div>

            <div id="period_start-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="period_start">Period Start</label>
              </span>
              <span class="form-thing">
                <input type="text" id="period_start" name="period_start" onInput={event => this.period_startChange(event)} />
              </span>
            </div>

            <div id="period_end-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="period_end">Period End</label>
              </span>
              <span class="form-thing">
                <input type="text" id="period_end" name="period_end" onInput={event => this.period_endChange(event)} />
              </span>
            </div>

            <div id="description-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="description">Description</label>
              </span>
              <span class="form-thing">
                <textarea id="description" name="description" onInput={event => this.descriptionChange(event)}></textarea>
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

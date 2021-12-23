import { Component, Host, h, State, Prop } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';

class CreateWorkEstimateRequest {
  token: string;
  work_estimate: {
    person: number;
    hours: number;
    minutes: number;
    total_time?: number;
    comment?: string;
  };
}

class CommonWorkEstimateRow {
  id: string;
  ticket: number;
  person: number;
  created_at: string;
  created_by: number;
  modified_at: string;
  modified_by: number;
  owning_person: number;
  owning_group: number;
}

class CreateWorkEstimateResponse extends GenericResponse {
  work_estimate: CommonWorkEstimateRow;
}
class CommonWorkEstimateListRequest {
  token: string;
}

class CommonWorkEstimateResponse {
  error: ErrorType;
  work_estimate: CommonWorkEstimateRow[];
}

class CommonWorkEstimateUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class CommonWorkEstimateUpdateResponse {
  error: ErrorType;
  work_estimate: CommonWorkEstimateRow | null = null;
}

class DeleteWorkEstimateRequest {
  id: string;
  token: string;
}

class DeleteWorkEstimateResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'work-estimates',
  styleUrl: 'work-estimates.css',
  shadow: true,
})
export class WorkRecord {
  @Prop() onlyShowCreate: boolean = false;
  @State() CommonWorkEstimateResponse: CommonWorkEstimateResponse;
  newPerson: number;
  newHours: number;
  newMinutes: number;
  newComment: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonWorkEstimateUpdateRequest, CommonWorkEstimateUpdateResponse>('common/work-estimates/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    if (updateResponse.error == ErrorType.NoError) {
      this.CommonWorkEstimateResponse = {
        error: ErrorType.NoError,
        work_estimate: this.CommonWorkEstimateResponse.work_estimate.map(work_estimate => (work_estimate.id === id ? updateResponse.work_estimate : work_estimate)),
      };
      return true;
    } else {
      alert(updateResponse.error);
      return false;
    }
  };

  handleDelete = async id => {
    const result = await fetchAs<DeleteWorkEstimateRequest, DeleteWorkEstimateResponse>('common/work-estimates/delete', {
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
      field: 'person',
      displayName: 'Person',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'hours',
      displayName: 'Hours',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'minutes',
      displayName: 'Minutes',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'total_time',
      displayName: 'Total Time',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'total_time',
      displayName: 'Total Time',
      width: 200,
      editable: false,
    },
    {
      field: 'comment',
      displayName: 'Comment',
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
  }

  async getList() {
    this.CommonWorkEstimateResponse = await fetchAs<CommonWorkEstimateListRequest, CommonWorkEstimateResponse>('common/work-estimates/list', {
      token: globals.globalStore.state.token,
    });
  }

  personChange(event) {
    this.newPerson = event.target.value;
  }

  hoursChange(event) {
    this.newHours = event.target.value;
  }

  minutesChange(event) {
    this.newMinutes = event.target.value;
  }

  commentChange(event) {
    this.newComment = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const result = await fetchAs<CreateWorkEstimateRequest, CreateWorkEstimateResponse>('common/work-estimates/create-read', {
      token: globals.globalStore.state.token,
      work_estimate: {
        person: this.newPerson,
        hours: this.newHours,
        minutes: this.newMinutes,
        comment: this.newComment,
      },
    });

    if (result.error === ErrorType.NoError) {
      globals.globalStore.state.editMode = false;
      this.getList();
    }
  };

  render() {
    return (
      <Host>
        <slot></slot>

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {(globals.globalStore.state.editMode === true || this.onlyShowCreate === true) && (
          <form class="form-thing">
            <div id="person-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="person">Person:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="person" name="person" onInput={event => this.personChange(event)} />
              </span>
            </div>
            <div id="hours-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="hours">Hours:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="hours" name="hours" onInput={event => this.hoursChange(event)} />
              </span>
            </div>
            <div id="minutes-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="minutes">Minutes:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="minutes" name="minutes" onInput={event => this.minutesChange(event)} />
              </span>
            </div>
            <div id="comment-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="comment">Comment:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="comment" name="comment" onInput={event => this.commentChange(event)} />
              </span>
            </div>
            <span class="form-thing">
              <input id="create-button" type="submit" value="Create" onClick={this.handleInsert} />
            </span>
          </form>
        )}
        {/* table abstraction */}
        {this.CommonWorkEstimateResponse && this.onlyShowCreate === false && (
          <cf-table rowData={this.CommonWorkEstimateResponse.work_estimate} columnData={this.columnData}></cf-table>
        )}
      </Host>
    );
  }
}

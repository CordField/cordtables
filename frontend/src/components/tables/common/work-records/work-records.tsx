import { Component, Host, h, State, Prop } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';

class CreateWorkRecordRequest{
  token: string;
  work_record:{
    person: number;
    hours: number;
    minutes: number;
    total_time ?: number;
    comment ?: string;
  }
}

class CommonWorkRecordRow{
  id : number;
  ticket: number;
  person : number;
  created_at: string;
  created_by: number;
  modified_at: string;
  modified_by: number;
  owning_person: number;
  owning_group: number;
}


class CreateWorkRecordResponse extends GenericResponse {
  work_record: CommonWorkRecordRow;
}
class CommonWorkRecordListRequest{
  token: string;
}

class CommonWorkRecordResponse {
  error: ErrorType;
  work_record: CommonWorkRecordRow[];
}

class CommonWorkRecordUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}
class CommonWorkRecordUpdateResponse {
  error: ErrorType;
  work_record: CommonWorkRecordRow | null = null;
}

class DeleteWorkRecordRequest {
  id: number;
  token: string;
}

class DeleteWorkRecordResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'work-records',
  styleUrl: 'work-records.css',
  shadow: true,
})
export class WorkRecord{

  
  @Prop() onlyShowCreate: boolean = false;
  @State() CommonWorkRecordResponse: CommonWorkRecordResponse;
  newPerson: number;
  newHours: number;
  newMinutes: number;
  newComment: string;




  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> =>{
    const updateResponse = await fetchAs<CommonWorkRecordUpdateRequest, CommonWorkRecordUpdateResponse>('common-work-records/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    if (updateResponse.error == ErrorType.NoError) {
      this.CommonWorkRecordResponse = { error: ErrorType.NoError, work_record: this.CommonWorkRecordResponse.work_record.map(work_record => (work_record.id === id ? updateResponse.work_record : work_record)) };
      return true;
    } else {
      alert(updateResponse.error);
      return false;
  }
}

handleDelete = async id => {
  const result = await fetchAs<DeleteWorkRecordRequest, DeleteWorkRecordResponse>('common-work-records/delete', {
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
  this.CommonWorkRecordResponse = await fetchAs<CommonWorkRecordListRequest, CommonWorkRecordResponse>('common-work-records/list', {
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

  const result = await fetchAs<CreateWorkRecordRequest, CreateWorkRecordResponse>('common-work-records/create-read', {
    token: globals.globalStore.state.token,
    work_record: {
      person: this.newPerson,
      hours: this.newHours,
      minutes: this.newMinutes,
      comment: this.newComment
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
        {this.CommonWorkRecordResponse && this.onlyShowCreate === false && <cf-table rowData={this.CommonWorkRecordResponse.work_record} columnData={this.columnData}></cf-table>}
        
      </Host>
    );
  }

}


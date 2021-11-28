import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreatePrayerRequestExRequest {
  token: string;
  prayerRequest: {
    parent: number;
    subject: string;
    content: string;
  };
}
class CreatePrayerRequestExResponse extends GenericResponse {
  prayerRequest: CommonPrayerRequest;
}

class CommonPrayerRequestListRequest {
  token: string;
}

class CommonPrayerRequestListResponse {
  error: ErrorType;
  prayerRequests: CommonPrayerRequest[];
}


class CommonPrayerRequestUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class CommonPrayerRequestUpdateResponse {
  error: ErrorType;
  prayerRequest: CommonPrayerRequest | null = null;
}

class DeletePrayerRequestExRequest {
  id: number;
  token: string;
}

class DeletePrayerRequestExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'common-prayer-requests',
  styleUrl: 'common-prayer-requests.css',
  shadow: true,
})
export class CommonPrayerRequests {

  @State() prayerRequestsResponse: CommonPrayerRequestListResponse;

  newParent: number;
  newSubject: string;
  newContent: string;
  
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonPrayerRequestUpdateRequest, CommonPrayerRequestUpdateResponse>('common-prayer-requests/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.prayerRequestsResponse = { error: ErrorType.NoError, prayerRequests: this.prayerRequestsResponse.prayerRequests.map(prayerRequest => (prayerRequest.id === id ? updateResponse.prayerRequest : prayerRequest)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeletePrayerRequestExRequest, DeletePrayerRequestExResponse>('common-prayer-requests/delete', {
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
    this.prayerRequestsResponse = await fetchAs<CommonPrayerRequestListRequest, CommonPrayerRequestListResponse>('common-prayer-requests/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  parentChange(event) {
    this.newParent = event.target.value;
  }

  subjectChange(event){
    this.newSubject = event.target.value;
  }

  contentChange(event) {
    this.newContent = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreatePrayerRequestExRequest, CreatePrayerRequestExResponse>('common-prayer-requests/create-read', {
      token: globals.globalStore.state.token,
      prayerRequest: {
        parent: this.newParent,
        subject: this.newSubject,
        content: this.newContent,
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
      field: 'parent',
      displayName: 'Parent',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'subject',
      displayName: 'Subject',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'content',
      displayName: 'Content',
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
        {this.prayerRequestsResponse && <cf-table rowData={this.prayerRequestsResponse.prayerRequests} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">

            <div id="parent-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="parent">Parent</label>
              </span>
              <span class="form-thing">
                <input type="number" id="parent" name="parent" onInput={event => this.parentChange(event)} />
              </span>
            </div>

            <div id="subject-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="subject">Subject</label>
              </span>
              <span class="form-thing">
                <input type="text" id="subject" name="subject" onInput={event => this.subjectChange(event)} />
              </span>
            </div>

            <div id="content-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="content">Content</label>
              </span>
              <span class="form-thing">
                <textarea id="content" name="content" onInput={event => this.contentChange(event)}></textarea>
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

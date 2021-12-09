import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreatePrayerRequestExRequest {
  token: string;
  prayerRequest: {
    language_id: number;
    sensitivity: string;
    parent: number;
    translator: number;
    location: string;
    title: string;
    content: string;
    reviewed: boolean;
    prayer_type: string;
  };
}
class CreatePrayerRequestExResponse extends GenericResponse {
  prayerRequest: UpPrayerRequest;
}

class UpPrayerRequestListRequest {
  token: string;
}

class UpPrayerRequestListResponse {
  error: ErrorType;
  prayerRequests: UpPrayerRequest[];
}


class UpPrayerRequestUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class UpPrayerRequestUpdateResponse {
  error: ErrorType;
  prayerRequest: UpPrayerRequest | null = null;
}

class DeletePrayerRequestExRequest {
  id: number;
  token: string;
}

class DeletePrayerRequestExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'up-prayer-requests',
  styleUrl: 'up-prayer-requests.css',
  shadow: true,
})
export class UpPrayerRequests {

  @State() prayerRequestsResponse: UpPrayerRequestListResponse;

  newLanguage_id: number;
  newSensitivity: string;
  newParent: number;
  newTranslator: number;
  newLocation: string;
  newTitle: string;
  newContent: string;
  newReviewed: boolean;
  newPrayer_type
  
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<UpPrayerRequestUpdateRequest, UpPrayerRequestUpdateResponse>('up-prayer-requests/update-read', {
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
    const deleteResponse = await fetchAs<DeletePrayerRequestExRequest, DeletePrayerRequestExResponse>('up-prayer-requests/delete', {
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
    this.prayerRequestsResponse = await fetchAs<UpPrayerRequestListRequest, UpPrayerRequestListResponse>('up-prayer-requests/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  language_idChange(event) {
    this.newLanguage_id = event.target.value;
  }

  sensitivityChange(event){
    this.newSensitivity = event.target.value;
  }

  parentChange(event) {
    this.newParent = event.target.value;
  }

  translatorChange(event) {
    this.newTranslator = event.target.value;
  }

  locationChange(event){
    this.newLocation = event.target.value;
  }

  titleChange(event) {
    this.newTitle = event.target.value;
  }

  contentChange(event){
    this.newContent = event.target.value;
  }

  reviewedChange(event) {
    this.newReviewed = event.target.value;
  }

  prayer_typeChange(event) {
    this.newPrayer_type = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreatePrayerRequestExRequest, CreatePrayerRequestExResponse>('up-prayer-requests/create-read', {
      token: globals.globalStore.state.token,
      prayerRequest: {
        language_id: this.newLanguage_id,
        sensitivity: this.newSensitivity,
        parent: this.newParent,
        translator: this.newTranslator,
        location: this.newLocation,
        title: this.newTitle,
        content: this.newContent,
        reviewed: this.newReviewed,
        prayer_type: this.newPrayer_type,
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
      field: 'language_id',
      displayName: 'Language ID',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'sensitivity',
      displayName: 'Sensitivity',
      width: 200,
      editable: true,
      selectOptions: [
        {display:  "Low", value: "Low"},
        {display:  "Medium", value: "Medium"},
        {display:  "High", value: "High"},
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'parent',
      displayName: 'Parent',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'translator',
      displayName: 'Translator',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'location',
      displayName: 'Location',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'title',
      displayName: 'Title',
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
      field: 'reviewed',
      displayName: 'Reviewed',
      width: 200,
      editable: true,
      selectOptions: [
        {display: 'True', value: 'true'},
        {display: 'False', value: 'false'},
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'prayer_type',
      displayName: 'Prayer Type',
      width: 200,
      editable: true,
      selectOptions: [
        {display: 'Request', value: 'Request'},
        {display: 'Update', value: 'Update'},
        {display: 'Celebration', value: 'Celebration'},
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
        {this.prayerRequestsResponse && <cf-table rowData={this.prayerRequestsResponse.prayerRequests} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">

            <div id="language_id-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="language_id">Language ID</label>
              </span>
              <span class="form-thing">
                <input type="number" id="language_id" name="language_id" onInput={event => this.language_idChange(event)} />
              </span>
            </div>

            <div id="sensitivity-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="sensitivity">Sensitivity</label>
              </span>
              <span class="form-thing">
                <select id="sensitivity" name="sensitivity" onInput={event => this.sensitivityChange(event)}>
                    <option value="">Select Sensitivity</option>
                    <option value="Low" selected={this.newSensitivity === "Low"}>Low</option>
                    <option value="Medium" selected={this.newSensitivity === "Medium"}>Medium</option>
                    <option value="High" selected={this.newSensitivity === "High"}>High</option>
                </select>
              </span>
            </div>

            <div id="parent-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="parent">Parent</label>
              </span>
              <span class="form-thing">
                <input type="number" id="parent" name="parent" onInput={event => this.parentChange(event)} />
              </span>
            </div>

            <div id="translator-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="translator">Translator</label>
              </span>
              <span class="form-thing">
                <input type="number" id="translator" name="translator" onInput={event => this.translatorChange(event)} />
              </span>
            </div>

            <div id="location-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="location">Location</label>
              </span>
              <span class="form-thing">
                <input type="text" id="location" name="location" onInput={event => this.locationChange(event)} />
              </span>
            </div>

            <div id="title-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="title">Title</label>
              </span>
              <span class="form-thing">
                <input type="text" id="title" name="title" onInput={event => this.titleChange(event)} />
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

            <div id="reviewed-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="reviewed">Reviewed</label>
              </span>
              <span class="form-thing">
                <select id="reviewed" name="reviewed" onInput={event => this.reviewedChange(event)}>
                    <option value="">Select Reviewed</option>
                    <option value="true" selected={this.newReviewed === true}>True</option>
                    <option value="false" selected={this.newReviewed === false}>False</option>
                </select>
              </span>
            </div>  

            <div id="prayer_type-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="prayer_type">Prayer Type</label>
              </span>
              <span class="form-thing">
                <select id="prayer_type" name="prayer_type" onInput={event => this.prayer_typeChange(event)}>
                    <option value="">Select Reviewed</option>
                    <option value="Request" selected={this.newPrayer_type === 'Request'}>Request</option>
                    <option value="Update" selected={this.newPrayer_type === 'Update'}>Update</option>
                    <option value="Celebration" selected={this.newPrayer_type === 'Celebration'}>Celebration</option>
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
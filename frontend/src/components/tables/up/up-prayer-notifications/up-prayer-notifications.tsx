import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreatePrayerNotificationExRequest {
  token: string;
  prayerNotification: {
    request: string;
    person: string;
  };
}
class CreatePrayerNotificationExResponse extends GenericResponse {
  prayerNotification: UpPrayerNotification;
}

class UpPrayerNotificationListRequest {
  token: string;
}

class UpPrayerNotificationListResponse {
  error: ErrorType;
  prayerNotifications: UpPrayerNotification[];
}


class UpPrayerNotificationUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class UpPrayerNotificationUpdateResponse {
  error: ErrorType;
  prayerNotification: UpPrayerNotification | null = null;
}

class DeletePrayerNotificationExRequest {
  id: string;
  token: string;
}

class DeletePrayerNotificationExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'up-prayer-notifications',
  styleUrl: 'up-prayer-notifications.css',
  shadow: true,
})
export class UpPrayerNotifications {

  @State() prayerNotificationsResponse: UpPrayerNotificationListResponse;

  newRequest: string;
  newPerson: string;
  
  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<UpPrayerNotificationUpdateRequest, UpPrayerNotificationUpdateResponse>('up-prayer-notifications/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.prayerNotificationsResponse = { error: ErrorType.NoError, prayerNotifications: this.prayerNotificationsResponse.prayerNotifications.map(prayerNotification => (prayerNotification.id === id ? updateResponse.prayerNotification : prayerNotification)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeletePrayerNotificationExRequest, DeletePrayerNotificationExResponse>('up-prayer-notifications/delete', {
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
    this.prayerNotificationsResponse = await fetchAs<UpPrayerNotificationListRequest, UpPrayerNotificationListResponse>('up-prayer-notifications/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  requestChange(event) {
    this.newRequest = event.target.value;
  }

  personChange(event) {
    this.newPerson = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreatePrayerNotificationExRequest, CreatePrayerNotificationExResponse>('up-prayer-notifications/create-read', {
      token: globals.globalStore.state.token,
      prayerNotification: {
        request: this.newRequest,
        person: this.newPerson,
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
      field: 'request',
      displayName: 'Request',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'person',
      displayName: 'person',
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
        {this.prayerNotificationsResponse && <cf-table rowData={this.prayerNotificationsResponse.prayerNotifications} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">

            <div id="request-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="request">Request</label>
              </span>
              <span class="form-thing">
                <input type="text" id="request" name="request" onInput={event => this.requestChange(event)} />
              </span>
            </div>

            <div id="person-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="person">Person</label>
              </span>
              <span class="form-thing">
                <input type="text" id="person" name="person" onInput={event => this.personChange(event)} />
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

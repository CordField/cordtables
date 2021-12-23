import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateStageNotificationExRequest {
  token: string;
  stageNotification: {
    stage: number;
    on_enter: boolean;
    on_exit: boolean;
    person: number;
  };
}
class CreateStageNotificationExResponse extends GenericResponse {
  stageNotification: CommonStageNotification;
}

class CommonStageNotificationListRequest {
  token: string;
}

class CommonStageNotificationListResponse {
  error: ErrorType;
  stageNotifications: CommonStageNotification[];
}

class CommonStageNotificationUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class CommonStageNotificationUpdateResponse {
  error: ErrorType;
  stageNotification: CommonStageNotification | null = null;
}

class DeleteStageNotificationExRequest {
  id: string;
  token: string;
}

class DeleteStageNotificationExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'common-stage-notifications',
  styleUrl: 'common-stage-notifications.css',
  shadow: true,
})
export class CommonStageNotifications {
  @State() stageNotificationsResponse: CommonStageNotificationListResponse;

  newStage: number;
  newOn_enter: boolean;
  newOn_exit: boolean;
  newPerson: number;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonStageNotificationUpdateRequest, CommonStageNotificationUpdateResponse>('common/stage-notifications/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.stageNotificationsResponse = {
        error: ErrorType.NoError,
        stageNotifications: this.stageNotificationsResponse.stageNotifications.map(stageNotification =>
          stageNotification.id === id ? updateResponse.stageNotification : stageNotification,
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
    const deleteResponse = await fetchAs<DeleteStageNotificationExRequest, DeleteStageNotificationExResponse>('common/stage-notifications/delete', {
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
    this.stageNotificationsResponse = await fetchAs<CommonStageNotificationListRequest, CommonStageNotificationListResponse>('common/stage-notifications/list', {
      token: globals.globalStore.state.token,
    });
  }

  stageChange(event) {
    this.newStage = event.target.value;
  }

  on_enterChange(event) {
    this.newOn_enter = event.target.value;
  }

  on_exitChange(event) {
    this.newOn_exit = event.target.value;
  }

  personChange(event) {
    this.newPerson = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateStageNotificationExRequest, CreateStageNotificationExResponse>('common/stage-notifications/create-read', {
      token: globals.globalStore.state.token,
      stageNotification: {
        stage: this.newStage,
        on_enter: this.newOn_enter,
        on_exit: this.newOn_exit,
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
      field: 'stage',
      displayName: 'Stage',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'on_enter',
      displayName: 'On Enter',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'True', value: 'true' },
        { display: 'False', value: 'false' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'on_exit',
      displayName: 'On Exit',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'True', value: 'true' },
        { display: 'False', value: 'false' },
      ],
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
        {this.stageNotificationsResponse && <cf-table rowData={this.stageNotificationsResponse.stageNotifications} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="stage-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="stage">Stage</label>
              </span>
              <span class="form-thing">
                <input type="number" id="stage" name="stage" onInput={event => this.stageChange(event)} />
              </span>
            </div>

            <div id="on_enter-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="on_enter">On Enter</label>
              </span>
              <span class="form-thing">
                <select id="on_enter" name="on_enter" onInput={event => this.on_enterChange(event)}>
                  <option value="">Select On Enter</option>
                  <option value="true" selected={this.newOn_enter === true}>
                    True
                  </option>
                  <option value="false" selected={this.newOn_enter === false}>
                    False
                  </option>
                </select>
              </span>
            </div>

            <div id="on_exit-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="on_enon_exitter">On Exit</label>
              </span>
              <span class="form-thing">
                <select id="on_exit" name="on_exit" onInput={event => this.on_exitChange(event)}>
                  <option value="">Select On Exit</option>
                  <option value="true" selected={this.newOn_exit === true}>
                    True
                  </option>
                  <option value="false" selected={this.newOn_exit === false}>
                    False
                  </option>
                </select>
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

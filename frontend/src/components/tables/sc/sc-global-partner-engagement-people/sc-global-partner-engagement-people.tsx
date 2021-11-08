import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateGlobalPartnerEngagementPeopleExRequest {
  token: string;
  globalPartnerEngagementPeople: {
    engagement: number;
    person: number;
    role: string;
  };
}
class CreateGlobalPartnerEngagementPeopleExResponse extends GenericResponse {
  globalPartnerEngagementPeople: ScGlobalPartnerEngagementPeople;
}

class ScGlobalPartnerEngagementPeopleListRequest {
  token: string;
}

class ScGlobalPartnerEngagementPeopleListResponse {
  error: ErrorType;
  globalPartnerEngagementPeoples: ScGlobalPartnerEngagementPeople[];
}


class ScGlobalPartnerEngagementPeopleUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class ScGlobalPartnerEngagementPeopleUpdateResponse {
  error: ErrorType;
  globalPartnerEngagementPeople: ScGlobalPartnerEngagementPeople | null = null;
}

class DeleteGlobalPartnerEngagementPeopleExRequest {
  id: number;
  token: string;
}

class DeleteGlobalPartnerEngagementPeopleExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'sc-global-partner-engagement-people',
  styleUrl: 'sc-global-partner-engagement-people.css',
  shadow: true,
})
export class ScGlobalPartnerEngagementPeoples {

  @State() globalPartnerEngagementPeoplesResponse: ScGlobalPartnerEngagementPeopleListResponse;

  newEngagement: number;
  newPerson: number;
  newRole: string;
  
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScGlobalPartnerEngagementPeopleUpdateRequest, ScGlobalPartnerEngagementPeopleUpdateResponse>('sc-global-partner-engagement-people/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.globalPartnerEngagementPeoplesResponse = { error: ErrorType.NoError, globalPartnerEngagementPeoples: this.globalPartnerEngagementPeoplesResponse.globalPartnerEngagementPeoples.map(globalPartnerEngagementPeople => (globalPartnerEngagementPeople.id === id ? updateResponse.globalPartnerEngagementPeople : globalPartnerEngagementPeople)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteGlobalPartnerEngagementPeopleExRequest, DeleteGlobalPartnerEngagementPeopleExResponse>('sc-global-partner-engagement-people/delete', {
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
    this.globalPartnerEngagementPeoplesResponse = await fetchAs<ScGlobalPartnerEngagementPeopleListRequest, ScGlobalPartnerEngagementPeopleListResponse>('sc-global-partner-engagement-people/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  engagementChange(event) {
    this.newEngagement = event.target.value;
  }

  personChange(event) {
    this.newPerson = event.target.value;
  }

  roleChange(event) {
    this.newRole = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateGlobalPartnerEngagementPeopleExRequest, CreateGlobalPartnerEngagementPeopleExResponse>('sc-global-partner-engagement-people/create-read', {
      token: globals.globalStore.state.token,
      globalPartnerEngagementPeople: {
        engagement: this.newEngagement,
        person: this.newPerson,
        role: this.newRole,
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
      field: 'engagement',
      displayName: 'Engagement',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'person',
      displayName: 'Person',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'role',
      displayName: 'Role',
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
        {this.globalPartnerEngagementPeoplesResponse && <cf-table rowData={this.globalPartnerEngagementPeoplesResponse.globalPartnerEngagementPeoples} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="engagement-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="engagement">Engagement</label>
              </span>
              <span class="form-thing">
                <input type="text" id="engagement" name="engagement" onInput={event => this.engagementChange(event)} />
              </span>
            </div>

            <div id="person-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="person">Person</label>
              </span>
              <span class="form-thing">
                <input type="number" id="person" name="person" onInput={event => this.personChange(event)} />
              </span>
            </div>

            <div id="role-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="role">Role</label>
              </span>
              <span class="form-thing">
                <input type="text" id="role" name="role" onInput={event => this.roleChange(event)} />
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
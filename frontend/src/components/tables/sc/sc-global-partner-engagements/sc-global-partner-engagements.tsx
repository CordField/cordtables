import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateGlobalPartnerEngagementExRequest {
  token: string;
  globalPartnerEngagement: {
    organization: number;
    type: string;
    mou_start: string;
    mou_end: string;
    sc_roles: string;
    partner_roles: string;
  };
}
class CreateGlobalPartnerEngagementExResponse extends GenericResponse {
  globalPartnerEngagement: ScGlobalPartnerEngagement;
}

class ScGlobalPartnerEngagementListRequest {
  token: string;
}

class ScGlobalPartnerEngagementListResponse {
  error: ErrorType;
  globalPartnerEngagements: ScGlobalPartnerEngagement[];
}


class ScGlobalPartnerEngagementUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class ScGlobalPartnerEngagementUpdateResponse {
  error: ErrorType;
  globalPartnerEngagement: ScGlobalPartnerEngagement | null = null;
}

class DeleteGlobalPartnerEngagementExRequest {
  id: number;
  token: string;
}

class DeleteGlobalPartnerEngagementExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'sc-global-partner-engagements',
  styleUrl: 'sc-global-partner-engagements.css',
  shadow: true,
})
export class ScGlobalPartnerEngagements {

  @State() globalPartnerEngagementsResponse: ScGlobalPartnerEngagementListResponse;

  newOrganization: number;
  newType: string;
  newMou_start: string;
  newMou_end: string;
  newSc_roles: string;
  newPartner_roles: string;
  
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScGlobalPartnerEngagementUpdateRequest, ScGlobalPartnerEngagementUpdateResponse>('sc-global-partner-engagements/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.globalPartnerEngagementsResponse = { error: ErrorType.NoError, globalPartnerEngagements: this.globalPartnerEngagementsResponse.globalPartnerEngagements.map(globalPartnerEngagement => (globalPartnerEngagement.id === id ? updateResponse.globalPartnerEngagement : globalPartnerEngagement)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteGlobalPartnerEngagementExRequest, DeleteGlobalPartnerEngagementExResponse>('sc-global-partner-engagements/delete', {
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
    this.globalPartnerEngagementsResponse = await fetchAs<ScGlobalPartnerEngagementListRequest, ScGlobalPartnerEngagementListResponse>('sc-global-partner-engagements/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  organizationChange(event) {
    this.newOrganization = event.target.value;
  }

  typeChange(event) {
    this.newType = event.target.value;
  }

  mou_startChange(event) {
    this.newMou_start = event.target.value;
  }

  mou_endChange(event) {
    this.newMou_end = event.target.value;
  }

  sc_rolesChange(event) {
    this.newSc_roles = event.target.value;
  }

  partner_rolesChange(event) {
    this.newPartner_roles = event.target.value;
  }


  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateGlobalPartnerEngagementExRequest, CreateGlobalPartnerEngagementExResponse>('sc-global-partner-engagements/create-read', {
      token: globals.globalStore.state.token,
      globalPartnerEngagement: {
        organization: this.newOrganization,
        type: this.newType,
        mou_start: this.newMou_start,
        mou_end: this.newMou_end,
        sc_roles: this.newSc_roles,
        partner_roles: this.newPartner_roles,
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
      field: 'organization',
      displayName: 'Organization',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'type',
      displayName: 'Type',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'mou_start',
      displayName: 'Mou Start',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'mou_end',
      displayName: 'Mou End',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'sc_roles',
      displayName: 'Sc Roles',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'partner_roles',
      displayName: 'Partner Roles',
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
        {this.globalPartnerEngagementsResponse && <cf-table rowData={this.globalPartnerEngagementsResponse.globalPartnerEngagements} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="organization-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="organization">Organization</label>
              </span>
              <span class="form-thing">
                <input type="text" id="organization" name="organization" onInput={event => this.organizationChange(event)} />
              </span>
            </div>

            <div id="type-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="type">Type</label>
              </span>
              <span class="form-thing">
                <input type="text" id="type" name="directypetor" onInput={event => this.typeChange(event)} />
              </span>
            </div>

            <div id="mou_start-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="mou_start">Mou Start</label>
              </span>
              <span class="form-thing">
                <input type="text" id="mou_start" name="mou_start" onInput={event => this.mou_startChange(event)} />
              </span>
            </div>        

            <div id="mou_end-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="mou_end">Mou End</label>
              </span>
              <span class="form-thing">
                <input type="text" id="mou_end" name="mou_end" onInput={event => this.mou_endChange(event)} />
              </span>
            </div>

            <div id="sc_roles-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="sc_roles">SC Roles</label>
              </span>
              <span class="form-thing">
                <input type="text" id="sc_roles" name="sc_roles" onInput={event => this.sc_rolesChange(event)} />
              </span>
            </div>

            <div id="partner_roles-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="partner_roles">Partner Roles</label>
              </span>
              <span class="form-thing">
                <input type="text" id="partner_roles" name="partner_roles" onInput={event => this.partner_rolesChange(event)} />
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
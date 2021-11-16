import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreatePartnershipExRequest {
  token: string;
  partnership: {
    project: number;
    partner: number;
    change_to_plan: number;
    active: boolean;
    agreement: number;
  };
}
class CreatePartnershipExResponse extends GenericResponse {
  partnership: ScPartnership;
}

class ScPartnershipListRequest {
  token: string;
}

class ScPartnershipListResponse {
  error: ErrorType;
  partnerships: ScPartnership[];
}


class ScPartnershipUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class ScPartnershipUpdateResponse {
  error: ErrorType;
  partnership: ScPartnership | null = null;
}

class DeletePartnershipExRequest {
  id: number;
  token: string;
}

class DeletePartnershipExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'sc-partnerships',
  styleUrl: 'sc-partnerships.css',
  shadow: true,
})
export class ScPartnerships {

  @State() partnershipsResponse: ScPartnershipListResponse;

  newProject: number;
  newPartner: number;
  newChange_to_plan: number;
  newActive: boolean;
  newAgreement: number;
  
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScPartnershipUpdateRequest, ScPartnershipUpdateResponse>('sc-partnerships/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.partnershipsResponse = { error: ErrorType.NoError, partnerships: this.partnershipsResponse.partnerships.map(partnership => (partnership.id === id ? updateResponse.partnership : partnership)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeletePartnershipExRequest, DeletePartnershipExResponse>('sc-partnerships/delete', {
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
    this.partnershipsResponse = await fetchAs<ScPartnershipListRequest, ScPartnershipListResponse>('sc-partnerships/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  projectChange(event) {
    this.newProject = event.target.value;
  }

  partnerChange(event) {
    this.newPartner = event.target.value;
  }

  change_to_planChange(event) {
    this.newChange_to_plan = event.target.value;
  }

  activeChange(event) {
    this.newActive = event.target.value;
  }

  agreementChange(event) {
    this.newAgreement = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreatePartnershipExRequest, CreatePartnershipExResponse>('sc-partnerships/create-read', {
      token: globals.globalStore.state.token,
      partnership: {
        project: this.newProject,
        partner: this.newPartner,
        change_to_plan: this.newChange_to_plan,
        active: this.newActive,
        agreement: this.newAgreement,
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
      field: 'project',
      displayName: 'Project',
      width: 150,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'partner',
      displayName: 'Partner',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'change_to_plan',
      displayName: 'Change To Plan',
      width: 150,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'active',
      displayName: 'Active',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'agreement',
      displayName: 'Agreement',
      width: 150,
      editable: false,
      deleteFn: this.handleDelete,
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
        {this.partnershipsResponse && <cf-table rowData={this.partnershipsResponse.partnerships} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">

            <div id="project-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="project">Project</label>
              </span>
              <span class="form-thing">
                <input type="number" id="project" name="project" onInput={event => this.projectChange(event)} />
              </span>
            </div>

            <div id="partner-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="partner">Partner</label>
              </span>
              <span class="form-thing">
                <input type="number" id="partner" name="partner" onInput={event => this.partnerChange(event)} />
              </span>
            </div> 

            <div id="change_to_plan-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="change_to_plan">Change To Plan</label>
              </span>
              <span class="form-thing">
                <input type="number" id="change_to_plan" name="change_to_plan" onInput={event => this.change_to_planChange(event)} />
              </span>
            </div>

            <div id="active-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="active">Active</label>
              </span>
              <span class="form-thing">
                <select id="active" name="active" onInput={event => this.activeChange(event)}>
                  <option value="">Select Active</option>
                  <option value="true" selected={this.newActive === true}>True</option>
                   <option value="false" selected={this.newActive === false}>False</option>
                </select>
              </span>
            </div> 

            <div id="agreement-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="agreement">Agreement</label>
              </span>
              <span class="form-thing">
                <input type="number" id="agreement" name="agreement" onInput={event => this.agreementChange(event)} />
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
import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateGlobalPartnerTransitionExRequest {
  token: string;
  globalPartnerTransition: {
    organization: string;
    transition_type: string;
    effective_date: string;
  };
}
class CreateGlobalPartnerTransitionExResponse extends GenericResponse {
  globalPartnerTransition: ScGlobalPartnerTransition;
}

class ScGlobalPartnerTransitionListRequest {
  token: string;
}

class ScGlobalPartnerTransitionListResponse {
  error: ErrorType;
  globalPartnerTransitions: ScGlobalPartnerTransition[];
}

class ScGlobalPartnerTransitionUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScGlobalPartnerTransitionUpdateResponse {
  error: ErrorType;
  globalPartnerTransition: ScGlobalPartnerTransition | null = null;
}

class DeleteGlobalPartnerTransitionExRequest {
  id: string;
  token: string;
}

class DeleteGlobalPartnerTransitionExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-global-partner-transitions',
  styleUrl: 'sc-global-partner-transitions.css',
  shadow: true,
})
export class ScGlobalPartnerTransitions {
  @State() globalPartnerTransitionsResponse: ScGlobalPartnerTransitionListResponse;

  newOrganization: string;
  newTransition_type: string;
  newEffective_date: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScGlobalPartnerTransitionUpdateRequest, ScGlobalPartnerTransitionUpdateResponse>('sc/global-partner-transitions/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.globalPartnerTransitionsResponse = {
        error: ErrorType.NoError,
        globalPartnerTransitions: this.globalPartnerTransitionsResponse.globalPartnerTransitions.map(globalPartnerTransition =>
          globalPartnerTransition.id === id ? updateResponse.globalPartnerTransition : globalPartnerTransition,
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
    const deleteResponse = await fetchAs<DeleteGlobalPartnerTransitionExRequest, DeleteGlobalPartnerTransitionExResponse>('sc/global-partner-transitions/delete', {
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
    this.globalPartnerTransitionsResponse = await fetchAs<ScGlobalPartnerTransitionListRequest, ScGlobalPartnerTransitionListResponse>('sc/global-partner-transitions/list', {
      token: globals.globalStore.state.token,
    });
  }

  organizationChange(event) {
    this.newOrganization = event.target.value;
  }

  transition_typeChange(event) {
    this.newTransition_type = event.target.value;
  }

  effective_dateChange(event) {
    this.newEffective_date = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateGlobalPartnerTransitionExRequest, CreateGlobalPartnerTransitionExResponse>('sc/global-partner-transitions/create-read', {
      token: globals.globalStore.state.token,
      globalPartnerTransition: {
        organization: this.newOrganization,
        transition_type: this.newTransition_type,
        effective_date: this.newEffective_date,
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
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'transition_type',
      displayName: 'Transition Type',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'Organization Assessment', value: 'Organization Assessment' },
        { display: 'Development', value: 'Development' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'effective_date',
      displayName: 'Effective Date',
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
        {this.globalPartnerTransitionsResponse && <cf-table rowData={this.globalPartnerTransitionsResponse.globalPartnerTransitions} columnData={this.columnData}></cf-table>}

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

            <div id="transition_type-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="transition_type">Transition Type</label>
              </span>
              <span class="form-thing">
                <select id="transition_type" name="transition_type" onInput={event => this.transition_typeChange(event)}>
                  <option value="">Select Transition Type</option>
                  <option value="Organization Assessment" selected={this.newTransition_type === 'Organization Assessment'}>
                    Organization Assessment
                  </option>
                  <option value="Development" selected={this.newTransition_type === 'Development'}>
                    Development
                  </option>
                </select>
              </span>
            </div>

            <div id="effective_date-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="effective_date">Effective Date</label>
              </span>
              <span class="form-thing">
                <input type="text" id="effective_date" name="effective_date" onInput={event => this.effective_dateChange(event)} />
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

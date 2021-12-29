import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';

class CreateOrganizationRequest {
  token: string;
  organization: {
    name: string;
    sensitivity: string;
    primary_location: string;
  };
}
class CreateOrganizationResponse extends GenericResponse {
  organization: CommonOrganizationsRow;
}

class CommonOrganizationsListRequest {
  token: string;
}

class CommonOrganizationsRow {
  id: string;
  name: string;
  sensitivity: string;
  primary_location: string;
  created_at: string;
  created_by: string;
  modified_at: string;
  modified_by: string;
  owning_person: string;
  owning_group: string;
}

class CommonOrganizationsListResponse {
  error: ErrorType;
  organizations: CommonOrganizationsRow[];
}
class organizationsListResponse {
  error: ErrorType;
  organizations: CommonOrganizationsRow[];
}

class CommonOrganizationsUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class CommonOrganizationsUpdateResponse {
  error: ErrorType;
  organization: CommonOrganizationsRow | null = null;
}

class DeleteLanguageExRequest {
  id: string;
  token: string;
}

class DeleteLanguageExResponse extends GenericResponse {
  id: string;
}
@Component({
  tag: 'common-organizations',
  styleUrl: 'common-organizations.css',
  shadow: true,
})
export class ScLanguages {
  @State() commonOrganizationsResponse: CommonOrganizationsListResponse;
  newOrganizationName: string;
  newSensitivity: string;
  newPrimaryLocation: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonOrganizationsUpdateRequest, CommonOrganizationsUpdateResponse>('common-organizations/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    if (updateResponse.error == ErrorType.NoError) {
      this.commonOrganizationsResponse = {
        error: ErrorType.NoError,
        organizations: this.commonOrganizationsResponse.organizations.map(organization => (organization.id === id ? updateResponse.organization : organization)),
      };
      return true;
    } else {
      alert(updateResponse.error);
      return false;
    }
  };

  handleDelete = async id => {
    const result = await fetchAs<DeleteLanguageExRequest, DeleteLanguageExResponse>('common-organizations/delete', {
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
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'name',
      displayName: 'Organization Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'sensitivity',
      displayName: 'Sensitivity',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `Low`, value: 'Low' },
        { display: 'Medium', value: 'Medium' },
        { display: 'High', value: 'High' },
      ],
    },
    {
      field: 'primary_location',
      displayName: 'Primary Location',
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
    this.commonOrganizationsResponse = await fetchAs<CommonOrganizationsListRequest, organizationsListResponse>('common-organizations/list', {
      token: globals.globalStore.state.token,
    });
  }

  organizationNameChange(event) {
    this.newOrganizationName = event.target.value;
  }

  sensitivityChange(event) {
    this.newSensitivity = event.target.value;
  }

  primaryLocationNameChange(event) {
    this.newPrimaryLocation = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const result = await fetchAs<CreateOrganizationRequest, CreateOrganizationResponse>('common-organizations/create-read', {
      token: globals.globalStore.state.token,
      organization: {
        name: this.newOrganizationName,
        sensitivity: this.newSensitivity,
        primary_location: this.newPrimaryLocation,
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
        {/* table abstraction */}
        {this.commonOrganizationsResponse && <cf-table rowData={this.commonOrganizationsResponse.organizations} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="organization-name-holder" class="form-input-item form-thing">
              <label>
                {' '}
                <strong> New Organization: </strong>
              </label>
              <br />
              <span class="form-thing">
                <label htmlFor="organization-name">Organization Name:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="organization-name" name="organization-name" onInput={event => this.organizationNameChange(event)} />
              </span>
            </div>
            <div id="sensitivity-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="sensitivity">Sensitivity:</label>
              </span>
              <span class="form-thing">
                <select id="sensitivity" name="sensitivity" onInput={event => this.sensitivityChange(event)}>
                  <option value="-">-</option>
                  <option value="Low">Low</option>
                  <option value="Medium">Medium</option>
                  <option value="High">High</option>
                </select>
              </span>
            </div>
            <div id="primary-location-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="primary-location">Primary Location:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="primary-location" name="primary-location" onInput={event => this.primaryLocationNameChange(event)} />
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

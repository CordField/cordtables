import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateOrganizationLocationExRequest {
  token: string;
  organizationLocation: {
    organization: number;
    location: number;
  };
}
class CreateOrganizationLocationExResponse extends GenericResponse {
  organizationLocation: ScOrganizationLocation;
}

class ScOrganizationLocationListRequest {
  token: string;
}

class ScOrganizationLocationListResponse {
  error: ErrorType;
  organizationLocations: ScOrganizationLocation[];
}

class ScOrganizationLocationUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScOrganizationLocationUpdateResponse {
  error: ErrorType;
  organizationLocation: ScOrganizationLocation | null = null;
}

class DeleteOrganizationLocationExRequest {
  id: string;
  token: string;
}

class DeleteOrganizationLocationExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-organization-locations',
  styleUrl: 'sc-organization-locations.css',
  shadow: true,
})
export class ScOrganizationLocations {
  @State() organizationLocationsResponse: ScOrganizationLocationListResponse;

  newOrganization: number;
  newLocation: number;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScOrganizationLocationUpdateRequest, ScOrganizationLocationUpdateResponse>('sc/organization-locations/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.organizationLocationsResponse = {
        error: ErrorType.NoError,
        organizationLocations: this.organizationLocationsResponse.organizationLocations.map(organizationLocation =>
          organizationLocation.id === id ? updateResponse.organizationLocation : organizationLocation,
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
    const deleteResponse = await fetchAs<DeleteOrganizationLocationExRequest, DeleteOrganizationLocationExResponse>('sc/organization-locations/delete', {
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
    this.organizationLocationsResponse = await fetchAs<ScOrganizationLocationListRequest, ScOrganizationLocationListResponse>('sc/organization-locations/list', {
      token: globals.globalStore.state.token,
    });
  }

  organizationChange(event) {
    this.newOrganization = event.target.value;
  }

  locationChange(event) {
    this.newLocation = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateOrganizationLocationExRequest, CreateOrganizationLocationExResponse>('sc/organization-locations/create-read', {
      token: globals.globalStore.state.token,
      organizationLocation: {
        organization: this.newOrganization,
        location: this.newLocation,
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
      field: 'location',
      displayName: 'Location',
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
        {this.organizationLocationsResponse && <cf-table rowData={this.organizationLocationsResponse.organizationLocations} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="organization-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="organization">Organization</label>
              </span>
              <span class="form-thing">
                <input type="number" id="organization" name="organization" onInput={event => this.organizationChange(event)} />
              </span>
            </div>

            <div id="location-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="location">Location</label>
              </span>
              <span class="form-thing">
                <input type="number" id="location" name="location" onInput={event => this.locationChange(event)} />
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

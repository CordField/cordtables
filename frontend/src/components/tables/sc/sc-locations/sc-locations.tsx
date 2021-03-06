import { Component, Host, State, h } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { ScLocation } from './types';

class CreateLocationRequest {
  token: string;
  location: ScLocation;
}
class CreateLocationResponse extends GenericResponse {
  location: ScLocation;
}

class ScLocationsListRequest {
  token: string;
}

class ScLocationsListResponse {
  error: ErrorType;
  locations: ScLocation[];
}

class ScLocationsUpdateRequest {
  token: string;
  location: ScLocation;
}

class ScLocationUpdateResponse {
  error: ErrorType;
  location: ScLocation | null = null;
}

class DeleteLocationRequest {
  id: string;
  token: string;
}

class DeleteLocationResponse extends GenericResponse {
  id: string;
}

const locationTypeSelectOptions = [
  { display: `City`, value: 'City' },
  { display: 'County', value: 'County' },
  { display: 'State', value: 'State' },
  { display: 'Country', value: 'Country' },
  { display: 'Cross Border Area', value: 'CrossBorderArea' },
];

@Component({
  tag: 'sc-locations',
  styleUrl: 'sc-locations.css',
  shadow: true,
})
export class ScLocations {
  @State() locationsResponse: ScLocationsListResponse;
  newId: string;
  newDefault_region: string;
  newFunding_account: string;
  newIso_alpha_3: string;
  newLocationName: string;
  newLocationType: string = 'City';

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScLocationsUpdateRequest, ScLocationUpdateResponse>('sc/locations/update-read', {
      token: globals.globalStore.state.token,
      location: {
        id: id,
        [columnName]: value,
      },
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      return true;
    } else {
      alert(updateResponse.error);
      return false;
    }
  };

  handleDelete = async id => {
    const result = await fetchAs<DeleteLocationRequest, DeleteLocationResponse>('sc/locations/delete', {
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
      displayName: 'Location Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'default_region',
      displayName: 'Default Region',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'funding_account',
      displayName: 'Funding Account',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'iso_alpha_3',
      displayName: 'ISO Alpah 3',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'type',
      displayName: 'Type',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: locationTypeSelectOptions,
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
    this.locationsResponse = await fetchAs<ScLocationsListRequest, ScLocationsListResponse>('sc/locations/list', {
      token: globals.globalStore.state.token,
    });
  }

  idChange(event) {
    this.newId = event.target.value;
  }

  default_regionChange(event){
    this.newDefault_region = event.target.value;
  }

  

  funding_accountChange(event){
    this.newFunding_account = event.target.value;
  }

  iso_alpha_3Change(event){
    this.newIso_alpha_3 = event.target.value;
  }

  locationNameChange(event) {
    this.newLocationName = event.target.value;
  }

  handleSelectLocationType(event) {
    this.newLocationType = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();
    console.log('inserting location???');

    const result = await fetchAs<CreateLocationRequest, CreateLocationResponse>('sc/locations/create-read', {
      token: globals.globalStore.state.token,
      location: {
        id: this.newId,
        default_region: this.newDefault_region,
        funding_account: this.newFunding_account,
        iso_alpha_3: this.newIso_alpha_3,
        name: this.newLocationName,
        type: this.newLocationType,
      },
    });

    if (result.error === ErrorType.NoError) {
      // globals.globalStore.state.editMode = false;
      this.getList();
    }
  };
  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.locationsResponse && <cf-table rowData={this.locationsResponse.locations} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="location-ID-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="location-ID">ID</label>
              </span>
              <span class="form-thing">
                <input type="text" id="location-ID" name="location-ID" onInput={event => this.idChange(event)} />
              </span>
            </div>

            <div id="default_region-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="default_region">Default Region</label>
              </span>
              <span class="form-thing">
                <input type="text" id="default_region" name="default_region" onInput={event => this.default_regionChange(event)} />
              </span>
            </div>

            <div id="funding_account-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="funding_account">Funding Account</label>
              </span>
              <span class="form-thing">
                <input type="text" id="funding_account" name="funding_account" onInput={event => this.funding_accountChange(event)} />
              </span>
            </div>

            <div id="iso_alpha_3-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="iso_alpha_3">iso_alpha_3</label>
              </span>
              <span class="form-thing">
                <input type="text" id="iso_alpha_3" name="iso_alpha_3" onInput={event => this.iso_alpha_3Change(event)} />
              </span>
            </div>

            <div id="location-name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="location-name">New Location Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="location-name" name="location-name" onInput={event => this.locationNameChange(event)} />
              </span>
            </div>
            <div id="location-type-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="location-type">Location Type</label>
              </span>
              <span class="form-thing">
                <select id="location-type" name="location-type" onInput={event => this.handleSelectLocationType(event)}>
                  {locationTypeSelectOptions.map(option => (
                    <option value={option.value} selected={this.newLocationType === option.value}>
                      {option.display}
                    </option>
                  ))}
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

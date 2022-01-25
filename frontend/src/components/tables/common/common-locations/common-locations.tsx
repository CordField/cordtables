import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { AutocompleteRequest, AutocompleteResponse, ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateLocationExRequest {
  token: string;
  location: {
    name: string;
    sensitivity: string;
    type: string;
    iso_alpha3: string;
  };
}
class CreateLocationExResponse extends GenericResponse {
  location: CommonLocation;
}

class CommonLocationListRequest {
  token: string;
}

class CommonLocationListResponse {
  error: ErrorType;
  locations: CommonLocation[];
}

class CommonLocationUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class CommonLocationUpdateResponse {
  error: ErrorType;
  location: CommonLocation | null = null;
}

class DeleteLocationExRequest {
  id: string;
  token: string;
}

class DeleteLocationExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'common-locations',
  styleUrl: 'common-locations.css',
  shadow: true,
})
export class CommonLocations {
  @State() locationsResponse: CommonLocationListResponse;

  newName: string;
  newSensitivity: string;
  newType: string;
  newIso_alpha3: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonLocationUpdateRequest, CommonLocationUpdateResponse>('common/locations/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.locationsResponse = { error: ErrorType.NoError, locations: this.locationsResponse.locations.map(location => (location.id === id ? updateResponse.location : location)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteLocationExRequest, DeleteLocationExResponse>('common/locations/delete', {
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
    this.locationsResponse = await fetchAs<CommonLocationListRequest, CommonLocationListResponse>('common/locations/list', {
      token: globals.globalStore.state.token,
    });
    if (this.locationsResponse.error === ErrorType.NoError) {
      await this.updateForeignKeys();
    }
  }

  async updateForeignKeys() {
    for (const location of this.locationsResponse.locations) {
      for (const column of this.columnData) {
        if (column.foreignKey !== null && column.foreignKey !== undefined) {
          const autocompleteData = await fetchAs<AutocompleteRequest, AutocompleteResponse>('admin/autocomplete', {
            token: globals.globalStore.state.token,
            searchColumnName: 'id',
            resultColumnName: column.foreignTableColumn,
            tableName: column.foreignKey.split('/').join('.').replace('-', '_'),
            searchKeyword: location[column.field],
          });
          console.log(autocompleteData);
          if (autocompleteData.error === ErrorType.NoError) {
            this.locationsResponse.locations.map(location2 => {
              if (location.id === location2.id) {
                location2[column.field] = {
                  value: location[column.field],
                  displayValue: autocompleteData.data,
                };
              }
              return location2;
            });
          }
        }
      }
    }
  }
  nameChange(event) {
    this.newName = event.target.value;
  }

  sensitivityChange(event) {
    this.newSensitivity = event.target.value;
  }

  typeChange(event) {
    this.newType = event.target.value;
  }

  iso_alpha3Change(event) {
    this.newIso_alpha3 = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateLocationExRequest, CreateLocationExResponse>('common/locations/create-read', {
      token: globals.globalStore.state.token,
      location: {
        name: this.newName,
        sensitivity: this.newSensitivity,
        type: this.newType,
        iso_alpha3: this.newIso_alpha3,
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
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'name',
      displayName: 'Name',
      width: 230,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'sensitivity',
      displayName: 'Sensitivity',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'Low', value: 'Low' },
        { display: 'Medium', value: 'Medium' },
        { display: 'High', value: 'High' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'type',
      displayName: 'Type',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'City', value: 'City' },
        { display: 'County', value: 'County' },
        { display: 'State', value: 'State' },
        { display: 'Country', value: 'Country' },
        { display: 'CrossBorderArea', value: 'CrossBorderArea' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'iso_alpha3',
      displayName: 'ISO Alpha3',
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
      foreignKey: 'admin/people',
      foreignTableColumn: 'public_first_name',
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
      foreignKey: 'admin/people',
      foreignTableColumn: 'public_first_name',
    },
    {
      field: 'owning_person',
      displayName: 'Owning Person ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
      foreignKey: 'admin/people',
      foreignTableColumn: 'public_first_name',
    },
    {
      field: 'owning_group',
      displayName: 'Owning Group ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
      foreignKey: 'admin/groups',
      foreignTableColumn: 'name',
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
        {this.locationsResponse && <cf-table rowData={this.locationsResponse.locations} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="name">Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="name" name="name" onInput={event => this.nameChange(event)} />
              </span>
            </div>

            <div id="sensitivity-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="sensitivity">Sensitivity</label>
              </span>
              <span class="form-thing">
                <select id="sensitivity" name="sensitivity" onInput={event => this.sensitivityChange(event)}>
                  <option value="">Select Sensitivity</option>
                  <option value="Low" selected={this.newSensitivity === 'Low'}>
                    Low
                  </option>
                  <option value="Medium" selected={this.newSensitivity === 'Medium'}>
                    Medium
                  </option>
                  <option value="High" selected={this.newSensitivity === 'High'}>
                    High
                  </option>
                </select>
              </span>
            </div>

            <div id="type-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="type">Type</label>
              </span>
              <span class="form-thing">
                <select id="type" name="type" onInput={event => this.typeChange(event)}>
                  <option value="">Select Type</option>
                  <option value="City" selected={this.newType === 'City'}>
                    City
                  </option>
                  <option value="County" selected={this.newType === 'County'}>
                    County
                  </option>
                  <option value="State" selected={this.newType === 'State'}>
                    State
                  </option>
                  <option value="Country" selected={this.newType === 'Country'}>
                    Country
                  </option>
                  <option value="CrossBorderArea" selected={this.newType === 'CrossBorderArea'}>
                    CrossBorderArea
                  </option>
                </select>
              </span>
            </div>

            <div id="iso_alpha3-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="iso_alpha3">ISO Alpha3</label>
              </span>
              <span class="form-thing">
                <input type="text" id="iso_alpha3" name="iso_alpha3" onInput={event => this.iso_alpha3Change(event)} />
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

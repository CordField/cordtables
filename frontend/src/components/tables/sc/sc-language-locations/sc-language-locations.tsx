import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateLanguageLocationExRequest {
  token: string;
  languageLocation: {
    language: string;
    location: string;
  };
}
class CreateLanguageLocationExResponse extends GenericResponse {
  languageLocation: ScLanguageLocation;
}

class ScLanguageLocationListRequest {
  token: string;
}

class ScLanguageLocationListResponse {
  error: ErrorType;
  languageLocations: ScLanguageLocation[];
}

class ScLanguageLocationUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScLanguageLocationUpdateResponse {
  error: ErrorType;
  languageLocation: ScLanguageLocation | null = null;
}

class DeleteLanguageLocationExRequest {
  id: string;
  token: string;
}

class DeleteLanguageLocationExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-language-locations',
  styleUrl: 'sc-language-locations.css',
  shadow: true,
})
export class ScLanguageLocations {
  @State() languageLocationsResponse: ScLanguageLocationListResponse;

  newLanguage: string;
  newLocation: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScLanguageLocationUpdateRequest, ScLanguageLocationUpdateResponse>('sc/language-locations/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.languageLocationsResponse = {
        error: ErrorType.NoError,
        languageLocations: this.languageLocationsResponse.languageLocations.map(languageLocation => (languageLocation.id === id ? updateResponse.languageLocation : languageLocation)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteLanguageLocationExRequest, DeleteLanguageLocationExResponse>('sc/language-locations/delete', {
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
    this.languageLocationsResponse = await fetchAs<ScLanguageLocationListRequest, ScLanguageLocationListResponse>('sc/language-locations/list', {
      token: globals.globalStore.state.token,
    });
  }

  languageChange(event) {
    this.newLanguage = event.target.value;
  }

  locationChange(event) {
    this.newLocation = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateLanguageLocationExRequest, CreateLanguageLocationExResponse>('sc/language-locations/create-read', {
      token: globals.globalStore.state.token,
      languageLocation: {
        language: this.newLanguage,
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
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'language',
      displayName: 'Language',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'location',
      displayName: 'location',
      width: 250,
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
        {this.languageLocationsResponse && <cf-table rowData={this.languageLocationsResponse.languageLocations} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="language-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="language">Language</label>
              </span>
              <span class="form-thing">
                <input type="text" id="director" name="director" onInput={event => this.languageChange(event)} />
              </span>
            </div>

            <div id="location-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="location">Location</label>
              </span>
              <span class="form-thing">
                <input type="text" id="location" name="location" onInput={event => this.locationChange(event)} />
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

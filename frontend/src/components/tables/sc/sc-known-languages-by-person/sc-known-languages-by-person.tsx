import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateKnownLanguagesByPersonExRequest {
  token: string;
  knownLanguagesByPerson: {
    person: string;
    known_language: string;
  };
}
class CreateKnownLanguagesByPersonExResponse extends GenericResponse {
  knownLanguagesByPerson: ScKnownLanguagesByPerson;
}

class ScKnownLanguagesByPersonListRequest {
  token: string;
}

class ScKnownLanguagesByPersonListResponse {
  error: ErrorType;
  knownLanguagesByPersons: ScKnownLanguagesByPerson[];
}

class ScKnownLanguagesByPersonUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScKnownLanguagesByPersonUpdateResponse {
  error: ErrorType;
  knownLanguagesByPerson: ScKnownLanguagesByPerson | null = null;
}

class DeleteKnownLanguagesByPersonExRequest {
  id: string;
  token: string;
}

class DeleteKnownLanguagesByPersonExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-known-languages-by-person',
  styleUrl: 'sc-known-languages-by-person.css',
  shadow: true,
})
export class ScKnownLanguagesByPersons {
  @State() knownLanguagesByPersonsResponse: ScKnownLanguagesByPersonListResponse;

  newPrson: string;
  newKnown_language: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScKnownLanguagesByPersonUpdateRequest, ScKnownLanguagesByPersonUpdateResponse>('sc/known-languages-by-person/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.knownLanguagesByPersonsResponse = {
        error: ErrorType.NoError,
        knownLanguagesByPersons: this.knownLanguagesByPersonsResponse.knownLanguagesByPersons.map(knownLanguagesByPerson =>
          knownLanguagesByPerson.id === id ? updateResponse.knownLanguagesByPerson : knownLanguagesByPerson,
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
    const deleteResponse = await fetchAs<DeleteKnownLanguagesByPersonExRequest, DeleteKnownLanguagesByPersonExResponse>('sc/known-languages-by-person/delete', {
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
    this.knownLanguagesByPersonsResponse = await fetchAs<ScKnownLanguagesByPersonListRequest, ScKnownLanguagesByPersonListResponse>('sc/known-languages-by-person/list', {
      token: globals.globalStore.state.token,
    });
  }

  personChange(event) {
    this.newPrson = event.target.value;
  }

  known_languageChange(event) {
    this.newKnown_language = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateKnownLanguagesByPersonExRequest, CreateKnownLanguagesByPersonExResponse>('sc/known-languages-by-person/create-read', {
      token: globals.globalStore.state.token,
      knownLanguagesByPerson: {
        person: this.newPrson,
        known_language: this.newKnown_language,
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
      field: 'person',
      displayName: 'Person',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'known_language',
      displayName: 'Known Language',
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
        {this.knownLanguagesByPersonsResponse && <cf-table rowData={this.knownLanguagesByPersonsResponse.knownLanguagesByPersons} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="person-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="person">Person</label>
              </span>
              <span class="form-thing">
                <input type="text" id="person" name="person" onInput={event => this.personChange(event)} />
              </span>
            </div>

            <div id="known_language-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="known_language">Known Language</label>
              </span>
              <span class="form-thing">
                <input type="text" id="known_language" name="known_language" onInput={event => this.known_languageChange(event)} />
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

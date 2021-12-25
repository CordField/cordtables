import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateEthnologueExRequest {
  token: string;
  ethnologue: {
    neo4j_id: string;
    language_index: string;
    code: string;
    language_name: string;
    population: number;
    provisional_code: string;
    sensitivity: string;

  };
}
class CreateEthnologueExResponse extends GenericResponse {
  ethnologue: ScEthnologue;
}

class ScEthnologueListRequest {
  token: string;
}

class ScEthnologueListResponse {
  error: ErrorType;
  ethnologues: ScEthnologue[];
}


class ScEthnologueUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScEthnologueUpdateResponse {
  error: ErrorType;
  ethnologue: ScEthnologue | null = null;
}

class DeleteEthnologueExRequest {
  id: string;
  token: string;
}

class DeleteEthnologueExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-ethnologue',
  styleUrl: 'sc-ethnologue.css',
  shadow: true,
})
export class ScEthnologues {

  @State() ethnologuesResponse: ScEthnologueListResponse;

  newNeo4j_id: string;
  newLanguage_index: string;
  newCode: string;
  newLanguage_name: string;
  newPopulation: number;
  newProvisional_code: string;
  newSensitivity: string;
  
  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScEthnologueUpdateRequest, ScEthnologueUpdateResponse>('sc-ethnologue/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.ethnologuesResponse = { error: ErrorType.NoError, ethnologues: this.ethnologuesResponse.ethnologues.map(ethnologue => (ethnologue.id === id ? updateResponse.ethnologue : ethnologue)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteEthnologueExRequest, DeleteEthnologueExResponse>('sc-ethnologue/delete', {
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
    this.ethnologuesResponse = await fetchAs<ScEthnologueListRequest, ScEthnologueListResponse>('sc-ethnologue/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  neo4j_idChange(event) {
    this.newNeo4j_id = event.target.value;
  }

  language_indexChange(event) {
    this.newLanguage_index = event.target.value;
  }

  codeChange(event) {
    this.newCode = event.target.value;
  }

  language_nameChange(event) {
    this.newLanguage_name = event.target.value;
  }

  populationChange(event) {
    this.newPopulation = event.target.value;
  }

  provisional_codeChange(event) {
    this.newProvisional_code = event.target.value;
  }

  sensitivityChange(event) {
    this.newSensitivity = event.target.value;
  }


  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateEthnologueExRequest, CreateEthnologueExResponse>('sc-ethnologue/create-read', {
      token: globals.globalStore.state.token,
      ethnologue: {
        neo4j_id: this.newNeo4j_id,
        language_index: this.newLanguage_index,
        code: this.newCode,
        language_name: this.newLanguage_name,
        population: this.newPopulation,
        provisional_code: this.newProvisional_code,
        sensitivity: this.newSensitivity,
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
      field: 'neo4j_id',
      displayName: 'Neo4j ID',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
        field: 'language_index',
        displayName: 'Language Index',
        width: 200,
        editable: true,
        updateFn: this.handleUpdate,
      },
    {
      field: 'code',
      displayName: 'Code',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
        field: 'language_name',
        displayName: 'Language Name',
        width: 200,
        editable: true,
        updateFn: this.handleUpdate,
      },
      {
        field: 'population',
        displayName: 'Population',
        width: 200,
        editable: true,
        updateFn: this.handleUpdate,
      },
      {
        field: 'provisional_code',
        displayName: 'Provisional Code',
        width: 200,
        editable: true,
        updateFn: this.handleUpdate,
      },
      {
        field: 'sensitivity',
        displayName: 'Sensitivity',
        width: 200,
        editable: true,
        selectOptions: [
            {display:  "Low", value: "Low"},
            {display:  "Medium", value: "Medium"},
            {display:  "High", value: "High"},
        ],
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
        {this.ethnologuesResponse && <cf-table rowData={this.ethnologuesResponse.ethnologues} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">

            <div id="neo4j_id-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="neo4j_id">Neo4j ID</label>
              </span>
              <span class="form-thing">
                <input type="text" id="neo4j_id" name="neo4j_id" onInput={event => this.neo4j_idChange(event)} />
              </span>
            </div>

            <div id="language_index-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="language_index">Language Index</label>
              </span>
              <span class="form-thing">
                <input type="text" id="language_index" name="language_index" onInput={event => this.language_indexChange(event)} />
              </span>
            </div>   





            <div id="code-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="code">Code</label>
              </span>
              <span class="form-thing">
                <input type="text" id="code" name="code" onInput={event => this.codeChange(event)} />
              </span>
            </div>

            <div id="language_name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="language_name">Language Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="language_name" name="language_name" onInput={event => this.language_nameChange(event)} />
              </span>
            </div>

            <div id="population-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="population">Population</label>
              </span>
              <span class="form-thing">
                <input type="number" id="population" name="population" onInput={event => this.populationChange(event)} />
              </span>
            </div>

            <div id="provisional_code-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="provisional_code">Provisional Code</label>
              </span>
              <span class="form-thing">
                <input type="text" id="provisional_code" name="provisional_code" onInput={event => this.provisional_codeChange(event)} />
              </span>
            </div>

            <div id="sensitivity-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="sensitivity">Sensitivity</label>
              </span>
              <span class="form-thing">
                <select id="sensitivity" name="sensitivity" onInput={event => this.sensitivityChange(event)}>
                    <option value="">Select Sensitivity</option>
                    <option value="Low" selected={this.newSensitivity === "Low"}>Low</option>
                    <option value="Medium" selected={this.newSensitivity === "Medium"}>Medium</option>
                    <option value="High" selected={this.newSensitivity === "High"}>High</option>
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

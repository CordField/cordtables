import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreatePeopleExRequest {
  token: string;
  people: {
    neo4j_id: string;
    skills: string;
    status: string;
  };
}
class CreatePeopleExResponse extends GenericResponse {
  people: ScPeople;
}

class ScPeopleListRequest {
  token: string;
}

class ScPeopleListResponse {
  error: ErrorType;
  peoples: ScPeople[];
}


class ScPeopleUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScPeopleUpdateResponse {
  error: ErrorType;
  people: ScPeople | null = null;
}

class DeletePeopleExRequest {
  id: string;
  token: string;
}

class DeletePeopleExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-people',
  styleUrl: 'sc-people.css',
  shadow: true,
})
export class ScPeoples {

  @State() peoplesResponse: ScPeopleListResponse;

  newNeo4j_id: string;
  newSkills: string;
  newStatus: string;
  
  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScPeopleUpdateRequest, ScPeopleUpdateResponse>('sc-people/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.peoplesResponse = { error: ErrorType.NoError, peoples: this.peoplesResponse.peoples.map(people => (people.id === id ? updateResponse.people : people)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeletePeopleExRequest, DeletePeopleExResponse>('sc-people/delete', {
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
    this.peoplesResponse = await fetchAs<ScPeopleListRequest, ScPeopleListResponse>('sc-people/list', {
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

  skillsChange(event) {
    this.newSkills = event.target.value;
  }

  statusChange(event) {
    this.newStatus = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreatePeopleExRequest, CreatePeopleExResponse>('sc-people/create-read', {
      token: globals.globalStore.state.token,
      people: {
        neo4j_id: this.newNeo4j_id,
        skills: this.newSkills,
        status: this.newStatus,
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
      displayName: 'Neo4j_ ID',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'skills',
      displayName: 'Skills',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'status',
      displayName: 'status',
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
        {this.peoplesResponse && <cf-table rowData={this.peoplesResponse.peoples} columnData={this.columnData}></cf-table>}

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

            <div id="skills-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="person">Skills</label>
              </span>
              <span class="form-thing">
                <input type="text" id="skills" name="skills" onInput={event => this.skillsChange(event)} />
              </span>
            </div>

            <div id="status-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="status">Status</label>
              </span>
              <span class="form-thing">
                <input type="text" id="status" name="status" onInput={event => this.statusChange(event)} />
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

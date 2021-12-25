import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreatePeopleGraphExRequest {
  token: string;
  peopleGraph: {
    from_person: string;
    to_person: string;
    rel_type: string;
  };
}
class CreatePeopleGraphExResponse extends GenericResponse {
  peopleGraph: CommonPeopleGraph;
}

class CommonPeopleGraphListRequest {
  token: string;
}

class CommonPeopleGraphListResponse {
  error: ErrorType;
  peopleGraphs: CommonPeopleGraph[];
}


class CommonPeopleGraphUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class CommonPeopleGraphUpdateResponse {
  error: ErrorType;
  peopleGraph: CommonPeopleGraph | null = null;
}

class DeletePeopleGraphExRequest {
  id: string;
  token: string;
}

class DeletePeopleGraphExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'common-people-graph',
  styleUrl: 'common-people-graph.css',
  shadow: true,
})
export class CommonPeopleGraphs {

  @State() peopleGraphsResponse: CommonPeopleGraphListResponse;

  newFrom_person: string;
  newTo_person: string;
  newRel_type: string;
  
  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonPeopleGraphUpdateRequest, CommonPeopleGraphUpdateResponse>('common-people-graph/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.peopleGraphsResponse = { error: ErrorType.NoError, peopleGraphs: this.peopleGraphsResponse.peopleGraphs.map(peopleGraph => (peopleGraph.id === id ? updateResponse.peopleGraph : peopleGraph)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeletePeopleGraphExRequest, DeletePeopleGraphExResponse>('common-people-graph/delete', {
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
    this.peopleGraphsResponse = await fetchAs<CommonPeopleGraphListRequest, CommonPeopleGraphListResponse>('common-people-graph/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  from_personChange(event) {
    this.newFrom_person = event.target.value;
  }

  to_personChange(event) {
    this.newTo_person = event.target.value;
  }

  rel_typeChange(event) {
    this.newRel_type = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreatePeopleGraphExRequest, CreatePeopleGraphExResponse>('common-people-graph/create-read', {
      token: globals.globalStore.state.token,
      peopleGraph: {
        from_person: this.newFrom_person,
        to_person: this.newTo_person,
        rel_type: this.newRel_type,
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
      field: 'from_person',
      displayName: 'From Person',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'to_person',
      displayName: 'To Person',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
        field: 'rel_type',
        displayName: 'Rel Type',
        width: 200,
        editable: true,
        selectOptions: [
            {display: 'Friend', value: 'Friend'},
            {display: 'Colleague', value: 'Colleague'},
            {display: 'Other', value: 'Other'},
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
        {this.peopleGraphsResponse && <cf-table rowData={this.peopleGraphsResponse.peopleGraphs} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">

            <div id="from_person-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="from_person">From Person</label>
              </span>
              <span class="form-thing">
                <input type="text" id="from_person" name="from_person" onInput={event => this.from_personChange(event)} />
              </span>
            </div>

            <div id="to_person-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="to_person">To Person</label>
              </span>
              <span class="form-thing">
                <input type="text" id="to_person" name="to_person" onInput={event => this.to_personChange(event)} />
              </span>
            </div>      

            <div id="rel_type-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="rel_type">Relationship Type</label>
              </span>
              <span class="form-thing">
                <select id="rel_type" name="rel_type" onInput={event => this.rel_typeChange(event)}>
                    <option value="">Select Relationship Type</option>
                    <option value="Friend" selected={this.newRel_type === "Friend"}>Friend</option>
                    <option value="Colleague" selected={this.newRel_type === "Colleague"}>Colleague</option>
                    <option value="Other" selected={this.newRel_type === "Other"}>Other</option>
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

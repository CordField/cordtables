import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateWorkflowExRequest {
  token: string;
  workflow: {
    title: string;
  };
}
class CreateWorkflowExResponse extends GenericResponse {
  workflow: CommonWorkflow;
}

class CommonWorkflowListRequest {
  token: string;
}

class CommonWorkflowListResponse {
  error: ErrorType;
  workflows: CommonWorkflow[];
}


class CommonWorkflowUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class CommonWorkflowUpdateResponse {
  error: ErrorType;
  workflow: CommonWorkflow | null = null;
}

class DeleteWorkflowExRequest {
  id: number;
  token: string;
}

class DeleteWorkflowExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'common-workflows',
  styleUrl: 'common-workflows.css',
  shadow: true,
})
export class CommonWorkflows {

  @State() workflowsResponse: CommonWorkflowListResponse;

  newTitle: string;
  
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonWorkflowUpdateRequest, CommonWorkflowUpdateResponse>('common-workflows/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.workflowsResponse = { error: ErrorType.NoError, workflows: this.workflowsResponse.workflows.map(workflow => (workflow.id === id ? updateResponse.workflow : workflow)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteWorkflowExRequest, DeleteWorkflowExResponse>('common-workflows/delete', {
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
    this.workflowsResponse = await fetchAs<CommonWorkflowListRequest, CommonWorkflowListResponse>('common-workflows/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  titleChange(event) {
    this.newTitle = event.target.value;
  }


  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateWorkflowExRequest, CreateWorkflowExResponse>('common-workflows/create-read', {
      token: globals.globalStore.state.token,
      workflow: {
        title: this.newTitle,
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
      field: 'title',
      displayName: 'Title',
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
        {this.workflowsResponse && <cf-table rowData={this.workflowsResponse.workflows} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">

            <div id="title-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="title">Title</label>
              </span>
              <span class="form-thing">
                <input type="text" id="title" name="title" onInput={event => this.titleChange(event)} />
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

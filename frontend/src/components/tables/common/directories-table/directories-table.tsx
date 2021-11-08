import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateDirectoryExRequest {
  token: string;
  directory: {
    name: string;
    parent: number;
  };
}
class CreateDirectoryExResponse extends GenericResponse {
  directory: CommonDirectory;
}

class CommonDirectoryListRequest {
  token: string;
}

class CommonDirectoryListResponse {
  error: ErrorType;
  directories: CommonDirectory[];
}

class CommonDirectoryUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class CommonDirectoryUpdateResponse {
  error: ErrorType;
  directory: CommonDirectory | null = null;
}

class DeleteDirectoryExRequest {
  id: number;
  token: string;
}

class DeleteDirectoryExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'directories-table',
  styleUrl: 'directories-table.css',
  shadow: true,
})
export class DirectoriesTable {

  @State() directoriesResponse: CommonDirectoryListResponse;
  newDirectoryName: string;
  directoryParent: number;


  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonDirectoryUpdateRequest, CommonDirectoryUpdateResponse>('common-directories/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.directoriesResponse = { error: ErrorType.NoError, directories: this.directoriesResponse.directories.map(directory => (directory.id === id ? updateResponse.directory : directory)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteDirectoryExRequest, DeleteDirectoryExResponse>('common-directories/delete', {
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
    this.directoriesResponse = await fetchAs<CommonDirectoryListRequest, CommonDirectoryListResponse>('common-directories/list', {
      token: globals.globalStore.state.token,
    });
  }

  directoryNameChange(event) {
    this.newDirectoryName = event.target.value;
  }

  directoryParentChange(event) {
    this.directoryParent = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateDirectoryExRequest, CreateDirectoryExResponse>('common-directories/create-read', {
      token: globals.globalStore.state.token,
      directory: {
        name: this.newDirectoryName,
        parent: this.directoryParent,
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
      field: 'parent',
      displayName: 'Parent',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'name',
      displayName: 'Directory Name',
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


  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.directoriesResponse && <cf-table rowData={this.directoriesResponse.directories} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="directory-name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="directory-name">New Directory Name</label>
              </span>
              <span class="form-thing">
                <select onInput={event => this.directoryParentChange(event)}>
                {this.directoriesResponse.directories.map(option => (
                  <option value={option.id}>{option.name}</option>
                ))}
                </select>
              </span>
            </div>
            
            <div id="directory-name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="directory-name">New Directory Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="directory-name" name="directory-name" onInput={event => this.directoryNameChange(event)} />
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
import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { AutocompleteRequest, AutocompleteResponse, ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateFileExRequest {
  token: string;
  file: {
    name: string;
    directory: string;
    // display_name: string;
  };
}
class CreateFileExResponse extends GenericResponse {
  file: CommonFile;
}

class CommonFileListRequest {
  token: string;
}

class CommonFileListResponse {
  error: ErrorType;
  files: CommonFile[];
}

class CommonDirectoryListRequest {
  token: string;
}

class CommonDirectoryListResponse {
  error: ErrorType;
  directories: CommonDirectory[];
}

class CommonFileUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class CommonFileUpdateResponse {
  error: ErrorType;
  file: CommonFile | null = null;
}

class DeleteFileExRequest {
  id: string;
  token: string;
}

class DeleteFileExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'files-table',
  styleUrl: 'files-table.css',
  shadow: true,
})
export class FilesTable {
  @State() filesResponse: CommonFileListResponse;
  @State() directoriesResponse: CommonDirectoryListResponse;
  newFileName: string;
  newFileDirectory: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonFileUpdateRequest, CommonFileUpdateResponse>('common/files/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.filesResponse = { error: ErrorType.NoError, files: this.filesResponse.files.map(file => (file.id === id ? updateResponse.file : file)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteFileExRequest, DeleteFileExResponse>('common/files/delete', {
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
    this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common/files/list', {
      token: globals.globalStore.state.token,
    });
  }

  async getDirectories() {
    this.directoriesResponse = await fetchAs<CommonDirectoryListRequest, CommonDirectoryListResponse>('common/directories/list', {
      token: globals.globalStore.state.token,
    });
    if (this.directoriesResponse.error === ErrorType.NoError) {
      await this.updateForeignKeys();
    }
  }

  async updateForeignKeys() {
    for (const file of this.filesResponse.files) {
      for (const column of this.columnData) {
        if (column.foreignKey !== null && column.foreignKey !== undefined) {
          const autocompleteData = await fetchAs<AutocompleteRequest, AutocompleteResponse>('admin/autocomplete', {
            token: globals.globalStore.state.token,
            searchColumnName: 'id',
            resultColumnName: column.foreignTableColumn,
            tableName: column.foreignKey.split('/').join('.').replace('-', '_'),
            searchKeyword: file[column.field],
          });
          console.log(autocompleteData);
          if (autocompleteData.error === ErrorType.NoError) {
            this.filesResponse.files.map(file2 => {
              if (file.id === file2.id) {
                file2[column.field] = {
                  value: file[column.field],
                  displayValue: autocompleteData.data,
                };
              }
              return file2;
            });
          }
        }
      }
    }
  }
  fileNameChange(event) {
    this.newFileName = event.target.value;
  }

  fileDirectoryChange(event) {
    this.newFileDirectory = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateFileExRequest, CreateFileExResponse>('common/files/create-read', {
      token: globals.globalStore.state.token,
      file: {
        name: this.newFileName,
        directory: this.newFileDirectory,
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
      field: 'directory',
      displayName: 'Directory',
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
      foreignKey: 'common/directories',
      foreignTableColumn: 'name',
    },
    {
      field: 'name',
      displayName: 'File Name',
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
    await this.getDirectories();
  }

  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.filesResponse && <cf-table rowData={this.filesResponse.files} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="directory-name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="directory-name">File Directory</label>
              </span>
              <span class="form-thing">
                <select onInput={event => this.fileDirectoryChange(event)}>
                  <option value="">Select A Directory</option>
                  {this.directoriesResponse.directories.map(option => (
                    <option value={option.id}>{option.name}</option>
                  ))}
                </select>
              </span>
            </div>

            <div id="file-name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="file-name">New File Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="file-name" name="file-name" onInput={event => this.fileNameChange(event)} />
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

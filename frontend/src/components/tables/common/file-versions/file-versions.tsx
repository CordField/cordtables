import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateFileVersionExRequest {
  token: string;
  fileVersion: {
    category: string;
    mime_type: string;
    name: string;
    file: number;
    file_url: string;
    file_size: number;
  };
}
class CreateFileVersionExResponse extends GenericResponse {
  fileVersion: CommonFileVersion;
}

class CommonFileVersionListRequest {
  token: string;
}

class CommonFileVersionListResponse {
  error: ErrorType;
  fileVersions: CommonFileVersion[];
}

class CommonFileListRequest {
  token: string;
}

class CommonFileListResponse {
  error: ErrorType;
  files: CommonFile[];
}

class CommonFileVersionUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class CommonFileVersionUpdateResponse {
  error: ErrorType;
  fileVersion: CommonFileVersion | null = null;
}

class DeleteFileVersionExRequest {
  id: string;
  token: string;
}

class DeleteFileVersionExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'file-versions',
  styleUrl: 'file-versions.css',
  shadow: true,
})
export class FileVersions {
  @State() fileVersionsResponse: CommonFileVersionListResponse;
  @State() filesResponse: CommonFileListResponse;

  newCategory: string;
  newMimeType: string;
  newName: string;
  newFile: number;
  newFileUrl: string;
  newFileSize: number;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonFileVersionUpdateRequest, CommonFileVersionUpdateResponse>('common/file-versions/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.fileVersionsResponse = {
        error: ErrorType.NoError,
        fileVersions: this.fileVersionsResponse.fileVersions.map(fileVersion => (fileVersion.id === id ? updateResponse.fileVersion : fileVersion)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteFileVersionExRequest, DeleteFileVersionExResponse>('common/file-versions/delete', {
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
    this.fileVersionsResponse = await fetchAs<CommonFileVersionListRequest, CommonFileVersionListResponse>('common/file-versions/list', {
      token: globals.globalStore.state.token,
    });
  }

  async getFilesList() {
    this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common/files/list', {
      token: globals.globalStore.state.token,
    });
  }

  categoryChange(event) {
    this.newCategory = event.target.value;
  }

  mimetypeChange(event) {
    this.newMimeType = event.target.value;
  }

  fileversionNameChange(event) {
    this.newName = event.target.value;
  }

  fileUrlChange(event) {
    this.newFileUrl = event.target.value;
  }

  fileSizeChange(event) {
    this.newFileSize = event.target.value;
  }

  fileChange(event) {
    this.newFile = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateFileVersionExRequest, CreateFileVersionExResponse>('common/file-versions/create-read', {
      token: globals.globalStore.state.token,
      fileVersion: {
        category: this.newCategory,
        mime_type: this.newMimeType,
        name: this.newName,
        file: this.newFile,
        file_url: this.newFileUrl,
        file_size: this.newFileSize,
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
      field: 'category',
      displayName: 'category',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'mime_type',
      displayName: 'Mime Type',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'name',
      displayName: 'File Version Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'file',
      displayName: 'File Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'file_url',
      displayName: 'File Url',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'file_size',
      displayName: 'File Size',
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
    await this.getFilesList();
  }

  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.fileVersionsResponse && <cf-table rowData={this.fileVersionsResponse.fileVersions} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="category-name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="category-name">Category</label>
              </span>
              <span class="form-thing">
                <input type="text" id="category-name" name="category-name" onInput={event => this.categoryChange(event)} />
              </span>
            </div>

            <div id="mimetype-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="mimetype">Mime Type</label>
              </span>
              <span class="form-thing">
                <input type="text" id="mimetype" name="mimetype" onInput={event => this.mimetypeChange(event)} />
              </span>
            </div>

            <div id="fileversion-name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="fileversion-name">New File Version Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="fileversion-name" name="fileversion-name" onInput={event => this.fileversionNameChange(event)} />
              </span>
            </div>

            <div id="file-name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="file-name">File</label>
              </span>
              <span class="form-thing">
                <select name="file" onInput={event => this.fileChange(event)}>
                  <option value="">Select A Directory</option>
                  {this.filesResponse.files.map(option => (
                    <option value={option.id}>{option.name}</option>
                  ))}
                </select>
              </span>
            </div>

            <div id="file-url-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="file-url">File Url</label>
              </span>
              <span class="form-thing">
                <input type="text" id="file-url-name" name="file-url-name" onInput={event => this.fileUrlChange(event)} />
              </span>
            </div>

            <div id="file-size-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="file-size">File Size</label>
              </span>
              <span class="form-thing">
                <input type="number" id="file-size" name="file-size" onInput={event => this.fileSizeChange(event)} />
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

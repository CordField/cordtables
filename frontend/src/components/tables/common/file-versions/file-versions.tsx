import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { AutocompleteRequest, AutocompleteResponse, ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateFileVersionExRequest {
  token: string;
  fileVersion: {
    mime_type: string;
    name: string;
    file: string;
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

  newMimeType: string;
  newName: string;
  newFile: string;
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
    if (this.fileVersionsResponse.error === ErrorType.NoError) {
      await this.updateForeignKeys();
    }
  }

  async getFilesList() {
    this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common/files/list', {
      token: globals.globalStore.state.token,
    });
  }
  async updateForeignKeys() {
    for (const fileVersion of this.fileVersionsResponse.fileVersions) {
      for (const column of this.columnData) {
        if (column.foreignKey !== null && column.foreignKey !== undefined) {
          const autocompleteData = await fetchAs<AutocompleteRequest, AutocompleteResponse>('admin/autocomplete', {
            token: globals.globalStore.state.token,
            searchColumnName: 'id',
            resultColumnName: column.foreignTableColumn,
            tableName: column.foreignKey.split('/').join('.').replace('-', '_'),
            searchKeyword: fileVersion[column.field],
          });
          console.log(autocompleteData);
          if (autocompleteData.error === ErrorType.NoError) {
            this.fileVersionsResponse.fileVersions.map(fileVersion2 => {
              if (fileVersion.id === fileVersion2.id) {
                fileVersion2[column.field] = {
                  value: fileVersion[column.field],
                  displayValue: autocompleteData.data,
                };
              }
              return fileVersion2;
            });
          }
        }
      }
    }
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
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'mime_type',
      displayName: 'Mime Type',
      width: 250,
      editable: true,
      selectOptions: [
        { display: 'application/msword', value: 'application/msword' },
        { display: 'application/pdf', value: 'application/pdf' },
        { display: 'application/postscript', value: 'application/postscript' },
        { display: 'application/rtf', value: 'application/rtf' },
        { display: 'application/vnd.ms-excel', value: 'application/vnd.ms-excel' },
        { display: 'application/vnd.ms-excel.sheet.macroEnabled.12', value: 'application/vnd.ms-excel.sheet.macroEnabled.12' },
        { display: 'application/vnd.ms-excel.sheet.binary.macroEnabled.12', value: 'application/vnd.ms-excel.sheet.binary.macroEnabled.12' },
        { display: 'application/vnd.ms-outlook', value: 'application/vnd.ms-outlook' },
        { display: 'application/octet-stream', value: 'application/octet-stream' },
        { display: 'application/vnd.ms-powerpoint', value: 'application/vnd.ms-powerpoint' },
        { display: 'application/vnd.ms-project', value: 'application/vnd.ms-project' },
        { display: 'application/vnd.oasis.opendocument.chart', value: 'application/vnd.oasis.opendocument.chart' },
        { display: 'application/vnd.oasis.opendocument.chart-template', value: 'application/vnd.oasis.opendocument.chart-template' },
        { display: 'application/vnd.oasis.opendocument.database', value: 'application/vnd.oasis.opendocument.database' },
        { display: 'application/vnd.oasis.opendocument.graphics', value: 'application/vnd.oasis.opendocument.graphics' },
        { display: 'application/vnd.oasis.opendocument.graphics-template', value: 'application/vnd.oasis.opendocument.graphics-template' },
        { display: 'application/vnd.oasis.opendocument.image', value: 'application/vnd.oasis.opendocument.image' },
        { display: 'application/vnd.oasis.opendocument.image-template', value: 'application/vnd.oasis.opendocument.image-template' },
        { display: 'application/vnd.oasis.opendocument.presentation', value: 'application/vnd.oasis.opendocument.presentation' },
        { display: 'application/vnd.oasis.opendocument.presentation-template', value: 'application/vnd.oasis.opendocument.presentation-template' },
        { display: 'application/vnd.oasis.opendocument.spreadsheet', value: 'application/vnd.oasis.opendocument.spreadsheet' },
        { display: 'application/vnd.oasis.opendocument.spreadsheet-template', value: 'application/vnd.oasis.opendocument.spreadsheet-template' },
        { display: 'application/vnd.oasis.opendocument.text', value: 'application/vnd.oasis.opendocument.text' },
        { display: 'application/vnd.oasis.opendocument.text-master', value: 'application/vnd.oasis.opendocument.text-master' },
        { display: 'application/vnd.oasis.opendocument.text-template', value: 'application/vnd.oasis.opendocument.text-template' },
        { display: 'application/vnd.oasis.opendocument.text-web', value: 'application/vnd.oasis.opendocument.text-web' },
        { display: 'application/vnd.openxmlformats-officedocument.presentationml.slide', value: 'application/vnd.openxmlformats-officedocument.presentationml.slide' },
        { display: 'application/vnd.openxmlformats-officedocument.presentationml.slideshow', value: 'application/vnd.openxmlformats-officedocument.presentationml.slideshow' },
        { display: 'application/vnd.openxmlformats-officedocument.presentationml.template', value: 'application/vnd.openxmlformats-officedocument.presentationml.template' },
        { display: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', value: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' },
        { display: 'application/vnd.openxmlformats-officedocument.spreadsheetml.template', value: 'application/vnd.openxmlformats-officedocument.spreadsheetml.template' },
        { display: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', value: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' },
        { display: 'application/vnd.openxmlformats-officedocument.wordprocessingml.template', value: 'application/vnd.openxmlformats-officedocument.wordprocessingml.template' },
        { display: 'application/vnd.visio', value: 'application/vnd.visio' },
        { display: 'application/vnd.wordperfect', value: 'application/vnd.wordperfect' },
        { display: 'application/x-font-ghostscript', value: 'application/x-font-ghostscript' },
        { display: 'application/x-font-linux-psf', value: 'application/x-font-linux-psf' },
        { display: 'application/x-font-pcf', value: 'application/x-font-pcf' },
        { display: 'application/x-font-snf', value: 'application/x-font-snf' },
        { display: 'application/x-font-type1', value: 'application/x-font-type1' },
        { display: 'application/x-gtar', value: 'application/x-gtar' },
        { display: 'application/x-iso9660-image', value: 'application/x-iso9660-image' },
        { display: 'application/x-ms-wmd', value: 'application/x-ms-wmd' },
        { display: 'application/x-msaccess', value: 'application/x-msaccess' },
        { display: 'application/x-mspublisher', value: 'application/x-mspublisher' },
        { display: 'application/x-mswrite', value: 'application/x-mswrite' },
        { display: 'application/x-tar', value: 'application/x-tar' },
        { display: 'application/x-tex', value: 'application/x-tex' },
        { display: 'application/x-tex-tfm', value: 'application/x-tex-tfm' },
        { display: 'application/x-texinfo', value: 'application/x-texinfo' },
        { display: 'application/x-zip-compressed', value: 'application/x-zip-compressed' },
        { display: 'application/zip', value: 'application/zip' },
        { display: 'audio/adpcm', value: 'audio/adpcm' },
        { display: 'audio/basic', value: 'audio/basic' },
        { display: 'audio/midi', value: 'audio/midi' },
        { display: 'audio/mp4', value: 'audio/mp4' },
        { display: 'audio/mpeg', value: 'audio/mpeg' },
        { display: 'audio/ogg', value: 'audio/ogg' },
        { display: 'audio/s3m', value: 'audio/s3m' },
        { display: 'audio/silk', value: 'audio/silk' },
        { display: 'audio/vnd.rip', value: 'audio/vnd.rip' },
        { display: 'audio/webm', value: 'audio/webm' },
        { display: 'audio/x-aac', value: 'audio/x-aac' },
        { display: 'audio/x-aiff', value: 'audio/x-aiff' },
        { display: 'audio/x-caf', value: 'audio/x-caf' },
        { display: 'audio/x-flac', value: 'audio/x-flac' },
        { display: 'audio/x-matroska', value: 'audio/x-matroska' },
        { display: 'audio/x-mpegurl', value: 'audio/x-mpegurl' },
        { display: 'audio/x-ms-wax', value: 'audio/x-ms-wax' },
        { display: 'audio/x-ms-wma', value: 'audio/x-ms-wma' },
        { display: 'audio/xpn-realaudio', value: 'audio/xpn-realaudio' },
        { display: 'audio/x-wav', value: 'audio/x-wav' },
        { display: 'audio/xm', value: 'audio/xm' },
        { display: 'font/otf', value: 'font/otf' },
        { display: 'font/ttf', value: 'font/ttf' },
        { display: 'font/woff', value: 'font/woff' },
        { display: 'font/woff2', value: 'font/woff2' },
        { display: 'image/bmp', value: 'image/bmp' },
        { display: 'image/cgm', value: 'image/cgm' },
        { display: 'image/g3fax', value: 'image/g3fax' },
        { display: 'image/gif', value: 'image/gif' },
        { display: 'image/ief', value: 'image/ief' },
        { display: 'image/jpeg', value: 'image/jpeg' },
        { display: 'image/ktx', value: 'image/ktx' },
        { display: 'image/png', value: 'image/png' },
        { display: 'image/sgi', value: 'image/sgi' },
        { display: 'image/svg+xml', value: 'image/svg+xml' },
        { display: 'image/tiff', value: 'image/tiff' },
        { display: 'image/vnd.adobe.photoshop', value: 'image/vnd.adobe.photoshop' },
        { display: 'image/vnd.dwg', value: 'image/vnd.dwg' },
        { display: 'image/vnd.dxf', value: 'image/vnd.dxf' },
        { display: 'image/x-3ds', value: 'image/x-3ds' },
        { display: 'image/x-cmu-raster', value: 'image/x-cmu-raster' },
        { display: 'image/x-cmx', value: 'image/x-cmx' },
        { display: 'image/x-freehand', value: 'image/x-freehand' },
        { display: 'image/x-icon', value: 'image/x-icon' },
        { display: 'image/x-mrsid-image', value: 'image/x-mrsid-image' },
        { display: 'image/x-pcx', value: 'image/x-pcx' },
        { display: 'image/x-pict', value: 'image/x-pict' },
        { display: 'image/x-portable-anymap', value: 'image/x-portable-anymap' },
        { display: 'image/x-portable-bitmap', value: 'image/x-portable-bitmap' },
        { display: 'image/x-portable-graymap', value: 'image/x-portable-graymap' },
        { display: 'image/x-portable-pixmap', value: 'image/x-portable-pixmap' },
        { display: 'image/x-rgb', value: 'image/x-rgb' },
        { display: 'image/x-tga', value: 'image/x-tga' },
        { display: 'image/x-xbitmap', value: 'image/x-xbitmap' },
        { display: 'image/x-xpixmap', value: 'image/x-xpixmap' },
        { display: 'image/xwindowdump', value: 'image/xwindowdump' },
        { display: 'message/rfc822', value: 'message/rfc822' },
        { display: 'text/calendar', value: 'text/calendar' },
        { display: 'text/css', value: 'text/css' },
        { display: 'text/csv', value: 'text/csv' },
        { display: 'text/html', value: 'text/html' },
        { display: 'text/plain', value: 'text/plain' },
        { display: 'text/richtext', value: 'text/richtext' },
        { display: 'text/rtf', value: 'text/rtf' },
        { display: 'text/sgml', value: 'text/sgml' },
        { display: 'text/tab-separated-values', value: 'text/tab-separated-values' },
        { display: 'video/3gpp', value: 'video/3gpp' },
        { display: 'video/3gp2', value: 'video/3gp2' },
        { display: 'video/h261', value: 'video/h261' },
        { display: 'video/h263', value: 'video/h263' },
        { display: 'video/h264', value: 'video/h264' },
        { display: 'video/jpeg', value: 'video/jpeg' },
        { display: 'video/jpm', value: 'video/jpm' },
        { display: 'video/mj2', value: 'video/mj2' },
        { display: 'video/mp4', value: 'video/mp4' },
        { display: 'video/mpeg', value: 'video/mpeg' },
        { display: 'video/ogg', value: 'video/ogg' },
        { display: 'video/quicktime', value: 'video/quicktime' },
        { display: 'video/vnd.mpegurl', value: 'video/vnd.mpegurl' },
        { display: 'video/vnd.vivo', value: 'video/vnd.vivo' },
        { display: 'video/webm', value: 'video/webm' },
        { display: 'video/x-f4v', value: 'video/x-f4v' },
        { display: 'video/x-fli', value: 'video/x-fli' },
        { display: 'video/x-flv', value: 'video/x-flv' },
        { display: 'video/x-m4v', value: 'video/x-m4v' },
        { display: 'video/x-matroska', value: 'video/x-matroska' },
        { display: 'video/x-mng', value: 'video/x-mng' },
        { display: 'video/x-ms-asf', value: 'video/x-ms-asf' },
        { display: 'video/x-ms-vob', value: 'video/x-ms-vob' },
        { display: 'video/x-ms-wm', value: 'video/x-ms-wm' },
        { display: 'video/x-ms-wmv', value: 'video/x-ms-wmv' },
        { display: 'video/x-ms-wmx', value: 'video/x-ms-wmx' },
        { display: 'video/x-ms-wvx', value: 'video/x-ms-wvx' },
        { display: 'video/x-msvideo', value: 'video/x-msvideo' },
        { display: 'video/x-sgi-movie', value: 'video/x-sgi-movie' },
        { display: 'video/x-smv', value: 'video/x-smv' },
      ],
      updateFn: this.handleUpdate,
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
      foreignKey: 'common/files',
      foreignTableColumn: 'name',
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
            <div id="mimetype-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="mimetype">Mime Type</label>
              </span>
              <span class="form-thing">
                <select id="mimetype" name="mimetype" onInput={event => this.mimetypeChange(event)}>
                  <option disabled selected value="">
                    Select Mime Type
                  </option>
                  {this.columnData[1].selectOptions.map(mimeType => (
                    <option key={mimeType.value} selected={this.newMimeType === mimeType.value}>
                      {mimeType.value}
                    </option>
                  ))}
                </select>
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
                  <option value="">Select A File</option>
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

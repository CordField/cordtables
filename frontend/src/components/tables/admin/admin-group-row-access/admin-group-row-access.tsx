import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateGroupRowAccessExRequest {
  token: string;
  groupRowAccess: {
    group_id: number;
    table_name: string;
    row: number;

  };
}
class CreateGroupRowAccessExResponse extends GenericResponse {
  groupRowAccess: AdminGroupRowAccess;
}

class AdminGroupRowAccessListRequest {
  token: string;
}

class AdminGroupRowAccessListResponse {
  error: ErrorType;
  groupRowAccesses: AdminGroupRowAccess[];
}


class AdminGroupRowAccessUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class AdminGroupRowAccessUpdateResponse {
  error: ErrorType;
  groupRowAccess: AdminGroupRowAccess | null = null;
}

class DeleteGroupRowAccessExRequest {
  id: number;
  token: string;
}

class DeleteGroupRowAccessExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'admin-group-row-access',
  styleUrl: 'admin-group-row-access.css',
  shadow: true,
})
export class AdminGroupRowAccesss {

  @State() groupRowAccessesResponse: AdminGroupRowAccessListResponse;

  newGroup_id: number;
  newTable_name: string;
  newRow: number;
 
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<AdminGroupRowAccessUpdateRequest, AdminGroupRowAccessUpdateResponse>('admin-group-row-access/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.groupRowAccessesResponse = { error: ErrorType.NoError, groupRowAccesses: this.groupRowAccessesResponse.groupRowAccesses.map(groupRowAccess => (groupRowAccess.id === id ? updateResponse.groupRowAccess : groupRowAccess)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteGroupRowAccessExRequest, DeleteGroupRowAccessExResponse>('admin-group-row-access/delete', {
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
    this.groupRowAccessesResponse = await fetchAs<AdminGroupRowAccessListRequest, AdminGroupRowAccessListResponse>('admin-group-row-access/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  group_idChange(event) {
    this.newGroup_id = event.target.value;
  }

  table_nameChange(event) {
    this.newTable_name = event.target.value;
  }

  rowChange(event) {
    this.newRow = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateGroupRowAccessExRequest, CreateGroupRowAccessExResponse>('admin-group-row-access/create-read', {
      token: globals.globalStore.state.token,
      groupRowAccess: {
        group_id: this.newGroup_id,
        table_name: this.newTable_name,
        row: this.newRow,
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
      field: 'group_id',
      displayName: 'Group ID',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'table_name',
      displayName: 'Table Name',
      width: 200,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'row',
      displayName: 'Row',
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
        {this.groupRowAccessesResponse && <cf-table rowData={this.groupRowAccessesResponse.groupRowAccesses} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="group_id-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="group_id">group_id</label>
              </span>
              <span class="form-thing">
                <input type="text" id="group_id" name="group_id" onInput={event => this.group_idChange(event)} />
              </span>
            </div>

            <div id="table_name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="table_name">Table Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="table_name" name="table_name" onInput={event => this.table_nameChange(event)} />
              </span>
            </div>

            <div id="row-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="row">Row</label>
              </span>
              <span class="form-thing">
                <input type="text" id="row-name" name="row" onInput={event => this.rowChange(event)} />
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

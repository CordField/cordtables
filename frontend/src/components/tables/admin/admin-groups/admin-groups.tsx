import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateGroupExRequest {
  token: string;
  group: {
    parent_group: string;
    name: string;
  };
}
class CreateGroupExResponse extends GenericResponse {
  group: AdminGroup;
}

class AdminGroupListRequest {
  token: string;
}

class AdminGroupListResponse {
  error: ErrorType;
  groups: AdminGroup[];
}

class AdminGroupUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class AdminGroupUpdateResponse {
  error: ErrorType;
  group: AdminGroup | null = null;
}

class DeleteGroupExRequest {
  id: string;
  token: string;
}

class DeleteGroupExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'admin-groups',
  styleUrl: 'admin-groups.css',
  shadow: true,
})
export class AdminGroups {
  @State() groupsResponse: AdminGroupListResponse;

  newParent_group: string;
  newName: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<AdminGroupUpdateRequest, AdminGroupUpdateResponse>('admin/groups/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.groupsResponse = { error: ErrorType.NoError, groups: this.groupsResponse.groups.map(group => (group.id === id ? updateResponse.group : group)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteGroupExRequest, DeleteGroupExResponse>('admin/groups/delete', {
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
    this.groupsResponse = await fetchAs<AdminGroupListRequest, AdminGroupListResponse>('admin/groups/list', {
      token: globals.globalStore.state.token,
    });
  }

  parent_groupChange(event) {
    this.newParent_group = event.target.value;
  }

  nameChange(event) {
    this.newName = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateGroupExRequest, CreateGroupExResponse>('admin/groups/create-read', {
      token: globals.globalStore.state.token,
      group: {
        parent_group: this.newParent_group,
        name: this.newName,
      },
    });

    if (createResponse.error === ErrorType.NoError) {
      globals.globalStore.state.editMode = false;
      this.newParent_group = "";
      this.newName = "";
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
      field: 'parent_group',
      displayName: 'Parent Group',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'name',
      displayName: 'Group Name',
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
        {this.groupsResponse && <cf-table rowData={this.groupsResponse.groups} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="parent_group-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="parent_group">Parent Group</label>
              </span>
              <span class="form-thing">
                <input type="text" id="parent_group" name="parent_group" onInput={event => this.parent_groupChange(event)} />
              </span>
            </div>

            <div id="field-region-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="field-region">New Group Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="field-region-name" name="field-region-name" onInput={event => this.nameChange(event)} />
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

import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateGroupMembershipExRequest {
  token: string;
  groupMembership: {
    group_id: string;
    person: string;
  };
}
class CreateGroupMembershipExResponse extends GenericResponse {
  groupMembership: AdminGroupMembership;
}

class AdminGroupMembershipListRequest {
  token: string;
}

class AdminGroupMembershipListResponse {
  error: ErrorType;
  groupMemberships: AdminGroupMembership[];
}

class AdminGroupMembershipUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class AdminGroupMembershipUpdateResponse {
  error: ErrorType;
  groupMembership: AdminGroupMembership | null = null;
}

class DeleteGroupMembershipExRequest {
  id: string;
  token: string;
}

class DeleteGroupMembershipExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'admin-group-memberships',
  styleUrl: 'admin-group-memberships.css',
  shadow: true,
})
export class AdminGroupMemberships {
  @State() groupMembershipsResponse: AdminGroupMembershipListResponse;

  newGroup_id: string;
  newPerson: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<AdminGroupMembershipUpdateRequest, AdminGroupMembershipUpdateResponse>('admin/group-memberships/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.groupMembershipsResponse = {
        error: ErrorType.NoError,
        groupMemberships: this.groupMembershipsResponse.groupMemberships.map(groupMembership => (groupMembership.id === id ? updateResponse.groupMembership : groupMembership)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteGroupMembershipExRequest, DeleteGroupMembershipExResponse>('admin/group-memberships/delete', {
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
    this.groupMembershipsResponse = await fetchAs<AdminGroupMembershipListRequest, AdminGroupMembershipListResponse>('admin/group-memberships/list', {
      token: globals.globalStore.state.token,
    });
  }

  group_idChange(event) {
    this.newGroup_id = event.target.value;
  }

  personChange(event) {
    this.newPerson = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateGroupMembershipExRequest, CreateGroupMembershipExResponse>('admin/group-memberships/create-read', {
      token: globals.globalStore.state.token,
      groupMembership: {
        group_id: this.newGroup_id,
        person: this.newPerson,
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
      field: 'group_id',
      displayName: 'Group ID',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'person',
      displayName: 'person',
      width: 250,
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
        {this.groupMembershipsResponse && <cf-table rowData={this.groupMembershipsResponse.groupMemberships} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="group_id-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="group_id">Group ID</label>
              </span>
              <span class="form-thing">
                <input type="text" id="director" name="director" onInput={event => this.group_idChange(event)} />
              </span>
            </div>

            <div id="person-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="person">Person</label>
              </span>
              <span class="form-thing">
                <input type="text" id="person" name="person" onInput={event => this.personChange(event)} />
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

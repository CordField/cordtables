import { Component, Host, h, State } from '@stencil/core';
import { v4 } from 'uuid';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ActionType, ErrorType } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
class GroupsListRequest {
  token: string;
}

class GroupsRow {
  id: string;
  name: string;
  createdAt: string;
  createdBy: string;
  modifiedAt: string;
  modifiedBy: string;
  owningPerson: string;
  owningGroup: string;
}

class GroupsListResponse {
  error: ErrorType;
  groups: Array<GroupsRow>;
}

class GroupCreateRequest {
  token: string;
  name: string;
}

class GroupCreateResponse {
  error: ErrorType;
}

class GroupUpdateRequest {
  token: string;
  column: string;
  id: string;
  value?: any;
}

class GroupUpdateResponse {
  error: ErrorType;
  group: AdminGroup | null = null;
}

class GroupDeleteRequest {
  token: string;
  id: string;
}

class GroupDeleteResponse {
  error: ErrorType;
}

@Component({
  tag: 'groups-table',
  styleUrl: 'groups-table.css',
  shadow: true,
})
export class CfGroups {
  @State() listResponse: GroupsListResponse;
  @State() showNewForm = false;

  createResponse: GroupCreateResponse;
  deleteResponse: GroupDeleteResponse;

  newRowName: string;

  handleDelete = async id => {
    const deleteResponse = await fetchAs<GroupDeleteRequest, GroupDeleteResponse>('admin/groups/delete', {
      id,
      token: globals.globalStore.state.token,
    });
    if (deleteResponse.error === ErrorType.NoError) {
      this.getList();
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item deleted successfully', id: v4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: deleteResponse.error, id: v4(), type: 'error' });
      return false;
    }
  };

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<GroupUpdateRequest, GroupUpdateResponse>('common/blogs/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    if (updateResponse.error == ErrorType.NoError) {
      this.listResponse = {
        error: ErrorType.NoError,
        groups: this.listResponse.groups.map(blog => (blog.id === id ? updateResponse.group : blog)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: v4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: v4(), type: 'error' });
      return false;
    }
  };

  editableKeys = ['name'];

  columnData: ColumnDescription[] = [
    {
      field: 'id',
      displayName: 'ID',
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'title',
      displayName: 'Title',
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

  async connectedCallback() {
    this.getList();
  }

  async getList() {
    this.listResponse = await fetchAs<GroupsListRequest, GroupsListResponse>('admin/groups/list', { token: globals.globalStore.state.token });
    if (this.listResponse.error === ErrorType.NoError) {
      // await this.updateForeignKeys();
    }
  }

  toggleNewForm = () => {
    this.showNewForm = !this.showNewForm;
  };

  inputName(event) {
    this.newRowName = event.target.value;
  }

  submit = async () => {
    this.createResponse = await fetchAs<GroupCreateRequest, GroupCreateResponse>('admin/groups/create', { token: globals.globalStore.state.token, name: this.newRowName });

    if (this.createResponse.error == ErrorType.NoError) {
      this.showNewForm = false;
      this.listResponse = await fetchAs<GroupsListRequest, GroupsListResponse>('admin/groups/list', { token: globals.globalStore.state.token });
    } else {
      console.warn('Error creating group');
    }
  };

  updateName = async (id: string, columnName: string, value: string): Promise<boolean> => {
    this.createResponse = await fetchAs<GroupUpdateRequest, GroupUpdateResponse>('admin/groups/update', { token: globals.globalStore.state.token, name: value, id });

    if (this.createResponse.error == ErrorType.NoError) {
      this.listResponse = await fetchAs<GroupsListRequest, GroupsListResponse>('admin/groups/list', { token: globals.globalStore.state.token });
      return true;
    } else {
    }
  };

  clickRemoveRowIcon = async (value: string): Promise<boolean> => {
    this.deleteResponse = await fetchAs<GroupDeleteRequest, GroupDeleteResponse>('admin/groups/delete', { token: globals.globalStore.state.token, id: value });

    if (this.deleteResponse.error === ErrorType.NoError) {
      this.listResponse = await fetchAs<GroupsListRequest, GroupsListResponse>('admin/groups/list', { token: globals.globalStore.state.token });
      return true;
    } else {
      return false;
    }
  };

  render() {
    return (
      <Host>
        <slot></slot>
        <h3>Groups</h3>
        <div id="table-wrap">
          <table>
            <tr>
              {this.listResponse && this.listResponse.groups && this.listResponse.groups.length > 0 && Object.keys(this.listResponse.groups[0]).map(key => <th>{key}</th>)}
              <th>ACTIONS</th>
            </tr>

            {this.listResponse &&
              this.listResponse.groups &&
              this.listResponse.groups.map(item => (
                <tr>
                  {Object.keys(item).map(key => (
                    <td>
                      <cf-cell
                        key={key}
                        rowId={item.id}
                        propKey={key}
                        value={item[key]}
                        isEditable={this.editableKeys.includes(key)}
                        updateFn={this.editableKeys.includes(key) ? this.updateName : null}
                      ></cf-cell>
                    </td>
                  ))}
                  <td>
                    <cf-action actionType={ActionType.Delete} value={item.id} text={'DELETE'} actionFn={this.clickRemoveRowIcon} />
                  </td>
                </tr>
              ))}

            {this.showNewForm && (
              <tr>
                <td class="disabled">&nbsp;</td>
                <td>
                  <input type="text" id="name-input" name="name" onInput={event => this.inputName(event)}></input>
                </td>
                <td class="disabled">&nbsp;</td>
                <td class="disabled">&nbsp;</td>
                <td class="disabled">&nbsp;</td>
                <td class="disabled">&nbsp;</td>
                <td class="disabled">&nbsp;</td>
                <td class="disabled">&nbsp;</td>
              </tr>
            )}
          </table>
        </div>

        <div id="button-group">
          {!this.showNewForm && (
            <button id="new-button" onClick={this.toggleNewForm}>
              Create New Group
            </button>
          )}

          {this.showNewForm && (
            <div>
              <button id="cancel-button" onClick={this.toggleNewForm}>
                Cancel
              </button>
              <button id="submit-button" onClick={this.submit}>
                Submit
              </button>
            </div>
          )}
        </div>
      </Host>
    );
  }
}

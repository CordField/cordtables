import { Component, Host, h, State } from '@stencil/core';
import { ActionType, ErrorType } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';

class GroupMembershipsListRequest {
  token: string;
}

class GroupMembershipsRow {
  id: number;
  group: number;
  person: number;
  createdAt: string;
  createdBy: number;
  modifiedAt: string;
  modifiedBy: number;
}

class GroupMembershipsListResponse {
  error: ErrorType;
  groupMemberships: Array<GroupMembershipsRow>;
}

class GroupCreateRequest {
  token: string;
  group: number;
  person: number;
}

class GroupCreateResponse {
  error: ErrorType;
}

class GroupUpdateRequest {
  token: string;
  name: string;
  id: number;
}

class GroupUpdateResponse {
  error: ErrorType;
}

class GroupDeleteRequest {
  token: string;
  id: number;
}

class GroupDeleteResponse {
  error: ErrorType;
}

@Component({
  tag: 'group-memberships',
  styleUrl: 'group-memberships.css',
  shadow: true,
})
export class GroupMemberships {
  @State() listResponse: GroupMembershipsListResponse;
  @State() showNewForm = false;

  createResponse: GroupCreateResponse;
  deleteResponse: GroupDeleteResponse;

  newRowGroup: number;
  newRowPerson: number;

  editableKeys = [];

  async connectedCallback() {
    this.getList();
  }

  async getList() {
    this.listResponse = await fetchAs<GroupMembershipsListRequest, GroupMembershipsListResponse>('admin-group-memberships/list', { token: globals.globalStore.state.token });
  }

  toggleNewForm = () => {
    this.showNewForm = !this.showNewForm;
  };

  inputGroup(event) {
    this.newRowGroup = +event.target.value;
  }

  inputPerson(event) {
    this.newRowPerson = +event.target.value;
  }

  submit = async () => {
    this.createResponse = await fetchAs<GroupCreateRequest, GroupCreateResponse>('admin-group-memberships/create', {
      token: globals.globalStore.state.token,
      group: this.newRowGroup,
      person: this.newRowPerson,
    });

    if (this.createResponse.error == ErrorType.NoError) {
      this.showNewForm = false;
      this.listResponse = await fetchAs<GroupMembershipsListRequest, GroupMembershipsListResponse>('admin-group-memberships/list', { token: globals.globalStore.state.token });
    } else {
      console.warn('Error creating group');
    }
  };

  updateName = async (id: number, value: string): Promise<boolean> => {
    this.createResponse = await fetchAs<GroupUpdateRequest, GroupUpdateResponse>('groupmemberships/update', { token: globals.globalStore.state.token, name: value, id });

    if (this.createResponse.error == ErrorType.NoError) {
      this.listResponse = await fetchAs<GroupMembershipsListRequest, GroupMembershipsListResponse>('admin-group-memberships/list', { token: globals.globalStore.state.token });
      return true;
    } else {
    }
  };

  clickRemoveRowIcon = async (value: number): Promise<boolean> => {
    this.deleteResponse = await fetchAs<GroupDeleteRequest, GroupDeleteResponse>('groupmemberships/delete', { token: globals.globalStore.state.token, id: value });

    if (this.deleteResponse.error === ErrorType.NoError) {
      this.listResponse = await fetchAs<GroupMembershipsListRequest, GroupMembershipsListResponse>('admin-group-memberships/list', { token: globals.globalStore.state.token });
      return true;
    } else {
      return false;
    }
  };

  render() {
    return (
      <Host>
        <slot></slot>
        <h3>Group Memberships</h3>
        <div id="table-wrap">
          <table>
            <tr id="header-row">
              {this.listResponse &&
                this.listResponse.groupMemberships &&
                this.listResponse.groupMemberships.length > 0 &&
                Object.keys(this.listResponse.groupMemberships[0]).map(key => <th>{key}</th>)}
              <th>ACTIONS</th>
            </tr>

            {this.listResponse &&
              this.listResponse.groupMemberships &&
              this.listResponse.groupMemberships.map(item => (
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
                  <input type="text" id="group-input" name="group" onInput={event => this.inputGroup(event)}></input>
                </td>
                <td>
                  <input type="text" id="group-input" name="group" onInput={event => this.inputPerson(event)}></input>
                </td>
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
              Create New Group Membership
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

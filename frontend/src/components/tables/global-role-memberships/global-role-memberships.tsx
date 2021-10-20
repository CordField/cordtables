import { Component, Host, h, State } from '@stencil/core';
import { ActionType, ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
class GroupsGlobalRoleMembershipsRequest {
  token: string;
}

class GroupsGlobalRoleMembershipsRow {
  id: number;
  globalRole: number;
  person: number;
  createdAt: string;
  createdBy: number;
  modifiedAt: string;
  modifiedBy: number;
  owningPerson: number;
  owningGroup: number;
}

class GlobalRoleMembershipsListResponse {
  error: ErrorType;
  groups: Array<GroupsGlobalRoleMembershipsRow>;
}

class GlobalRoleMembershipCreateRequest {
  token: string;
  role: number;
  person: number;
  owning_group: number;
}

class GroupCreateResponse {
  error: ErrorType;
}

class GroupUpdateRequest {
  token: string;
  role?: number;
  person?: number;
  owning_group?: number;
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
  tag: 'role-memberships',
  styleUrl: 'global-role-memberships.css',
  shadow: true,
})
export class GlobalRoleMemberships {
  @State() listResponse: GlobalRoleMembershipsListResponse;
  @State() showNewForm = false;

  createResponse: GroupCreateResponse;
  deleteResponse: GroupDeleteResponse;

  newGlobalRole: number;
  newPerson: number;
  newOwningGroup: number;

  editableKeys = ['name'];

  async connectedCallback() {
    this.getList();
  }

  async getList() {
    this.listResponse = await fetchAs<GroupsGlobalRoleMembershipsRequest, GlobalRoleMembershipsListResponse>('role-memberships/list', {
      token: globals.globalStore.state.token,
    });
  }

  toggleNewForm = () => {
    this.showNewForm = !this.showNewForm;
  };

  inputGlobalRole(event) {
    this.newGlobalRole = event.target.value;
  }
  inputPerson(event) {
    this.newPerson = event.target.value;
  }
  inputOwningGroup(event) {
    this.newOwningGroup = event.target.value;
  }

  submit = async () => {
    this.createResponse = await fetchAs<GlobalRoleMembershipCreateRequest, GroupCreateResponse>('role-memberships/create', {
      token: globals.globalStore.state.token,
      role: this.newGlobalRole,
      person: this.newPerson,
      owning_group: this.newOwningGroup,
    });

    if (this.createResponse.error == ErrorType.NoError) {
      this.showNewForm = false;
      this.listResponse = await fetchAs<GroupsGlobalRoleMembershipsRequest, GlobalRoleMembershipsListResponse>('role-memberships/list', {
        token: globals.globalStore.state.token,
      });
    } else {
      console.warn('Error creating group');
    }
  };

  updateGlobalRole = async (id: number, columnName: string, value: number): Promise<boolean> => {
    this.createResponse = await fetchAs<GroupUpdateRequest, GroupUpdateResponse>('groups/update', { token: globals.globalStore.state.token, role: value, id });

    if (this.createResponse.error == ErrorType.NoError) {
      this.listResponse = await fetchAs<GroupsGlobalRoleMembershipsRequest, GlobalRoleMembershipsListResponse>('role-memberships/list', {
        token: globals.globalStore.state.token,
      });
      return true;
    } else {
    }
  };

  clickRemoveRowIcon = async (value: number): Promise<boolean> => {
    this.deleteResponse = await fetchAs<GroupDeleteRequest, GroupDeleteResponse>('groups/delete', { token: globals.globalStore.state.token, id: value });

    if (this.deleteResponse.error === ErrorType.NoError) {
      this.listResponse = await fetchAs<GroupsGlobalRoleMembershipsRequest, GlobalRoleMembershipsListResponse>('role-memberships/list', {
        token: globals.globalStore.state.token,
      });
      return true;
    } else {
      return false;
    }
  };

  render() {
    return (
      <Host>
        <slot></slot>
        <h3>Global Role Memberships</h3>
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
                        updateFn={this.editableKeys.includes(key) ? this.updateGlobalRole : null}
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
                  <input type="text" id="role-input" placeholder="Global Role" name="role" onInput={event => this.inputGlobalRole(event)}></input>
                </td>
                <td class="disabled">
                  <input type="text" id="person-input" placeholder="Person" name="person" onInput={event => this.inputPerson(event)}></input>
                </td>
                <td class="disabled">
                  <input type="text" id="owning_group" placeholder="Owning Group" name="owning_group" onInput={event => this.inputOwningGroup(event)}></input>
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
              Create New
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

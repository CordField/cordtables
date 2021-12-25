import { Component, Host, h, State } from '@stencil/core';
import { ActionType, ErrorType } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
class GroupRowAccessListRequest {
  token: string;
}

class GroupRowAccessRow {
  id: string;
  group: string;
  person: string;
  createdAt: string;
  createdBy: string;
  modifiedAt: string;
  modifiedBy: string;
}

class GroupRowAccessListResponse {
  error: ErrorType;
  groupRowAccessList: Array<GroupRowAccessRow>;
}

class GroupRowAccessCreateRequest {
  token: string;
  group: string;
  tableName: string;
  row: number;
}

class GroupRowAccessCreateResponse {
  error: ErrorType;
}

class GroupRowAccessDeleteRequest {
  token: string;
  id: string;
}

class GroupRowAccessDeleteResponse {
  error: ErrorType;
}

@Component({
  tag: 'groups-row-access',
  styleUrl: 'groups-row-access.css',
  shadow: true,
})
export class GroupsRowAccess {
  @State() listResponse: GroupRowAccessListResponse;
  @State() showNewForm = false;

  createResponse: GroupRowAccessCreateResponse;
  deleteResponse: GroupRowAccessDeleteResponse;

  newRowGroup: string;
  newRowTableName: string;
  newRowRow: number;

  editableKeys = [];

  async connectedCallback() {
    this.getList();
  }

  async getList() {
    this.listResponse = await fetchAs<GroupRowAccessListRequest, GroupRowAccessListResponse>('grouprowaccess/list', { token: globals.globalStore.state.token });
  }

  toggleNewForm = () => {
    this.showNewForm = !this.showNewForm;
  };

  inputGroup(event) {
    this.newRowGroup = event.target.value;
  }

  inputTableName(event) {
    this.newRowTableName = event.target.value;
  }

  inputRow(event) {
    this.newRowRow = +event.target.value;
  }

  submit = async () => {
    this.createResponse = await fetchAs<GroupRowAccessCreateRequest, GroupRowAccessCreateResponse>('grouprowaccess/create', {
      token: globals.globalStore.state.token,
      group: this.newRowGroup,
      tableName: this.newRowTableName,
      row: this.newRowRow,
    });

    if (this.createResponse.error == ErrorType.NoError) {
      this.showNewForm = false;
      this.listResponse = await fetchAs<GroupRowAccessListRequest, GroupRowAccessListResponse>('grouprowaccess/list', { token: globals.globalStore.state.token });
    } else {
      console.warn('Error creating group');
    }
  };

  clickRemoveRowIcon = async (value: string): Promise<boolean> => {
    this.deleteResponse = await fetchAs<GroupRowAccessDeleteRequest, GroupRowAccessDeleteResponse>('grouprowaccess/delete', { token: globals.globalStore.state.token, id: value });

    if (this.deleteResponse.error === ErrorType.NoError) {
      this.listResponse = await fetchAs<GroupRowAccessListRequest, GroupRowAccessListResponse>('grouprowaccess/list', { token: globals.globalStore.state.token });
      return true;
    } else {
      return false;
    }
  };

  render() {
    return (
      <Host>
        <slot></slot>
        <h3>Group Row Access</h3>
        <div id="table-wrap">
          <table>
            <tr id="header-row">
              {this.listResponse &&
                this.listResponse.groupRowAccessList &&
                this.listResponse.groupRowAccessList.length > 0 &&
                Object.keys(this.listResponse.groupRowAccessList[0]).map(key => <th>{key}</th>)}
              <th>ACTIONS</th>
            </tr>

            {this.listResponse &&
              this.listResponse.groupRowAccessList &&
              this.listResponse.groupRowAccessList.map(item => (
                <tr>
                  {Object.keys(item).map(key => (
                    <td>
                      <cf-cell
                        key={key}
                        rowId={item.id}
                        propKey={key}
                        value={item[key]}
                        isEditable={this.editableKeys.includes(key)}
                        updateFn={this.editableKeys.includes(key) ? null : null}
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
                  <input type="text" id="table-name-input" name="tableName" onInput={event => this.inputTableName(event)}></input>
                </td>
                <td>
                  <input type="text" id="row-input" name="row" onInput={event => this.inputRow(event)}></input>
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

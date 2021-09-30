import { Component, Host, h, State } from '@stencil/core';
import { ErrorType } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
class GroupsListRequest {
  token: string;
}

class GroupsRow {
  id: number;
  name: string;
  createdAt: string;
  createdBy: number;
  modifiedAt: string;
  modifiedBy: number;
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
  name: string;
  id: number;
}

class GroupUpdateResponse {
  error: ErrorType;
}

@Component({
  tag: 'cf-groups',
  styleUrl: 'cf-groups.css',
  shadow: true,
})
export class CfGroups {
  @State() listResponse: GroupsListResponse;
  @State() showNewForm = false;

  createResponse: GroupCreateResponse;

  newRowName: string;

  editableKeys = ['name'];

  async connectedCallback() {
    this.getList();
  }

  async getList() {
    this.listResponse = await fetchAs<GroupsListRequest, GroupsListResponse>('groups/list', { token: globals.globalStore.state.token });
  }

  toggleNewForm = () => {
    this.showNewForm = !this.showNewForm;
  };

  inputName(event) {
    this.newRowName = event.target.value;
  }

  submit = async () => {
    this.createResponse = await fetchAs<GroupCreateRequest, GroupCreateResponse>('groups/create', { token: globals.globalStore.state.token, name: this.newRowName });

    if (this.createResponse.error == ErrorType.NoError) {
      this.showNewForm = false;
      this.listResponse = await fetchAs<GroupsListRequest, GroupsListResponse>('groups/list', { token: globals.globalStore.state.token });
    } else {
      console.warn('Error creating group');
    }
  };

  updateName = async (id: number, value: string): Promise<boolean> => {
    this.createResponse = await fetchAs<GroupUpdateRequest, GroupUpdateResponse>('groups/update', { token: globals.globalStore.state.token, name: value, id });

    if (this.createResponse.error == ErrorType.NoError) {
      this.listResponse = await fetchAs<GroupsListRequest, GroupsListResponse>('groups/list', { token: globals.globalStore.state.token });
      return true;
    } else {
    }
  };

  render() {
    return (
      <Host>
        <slot></slot>
        <h3>Groups</h3>
        <table>
          <tr>{this.listResponse && this.listResponse.groups && this.listResponse.groups.length > 0 && Object.keys(this.listResponse.groups[0]).map(key => <th>{key}</th>)}</tr>

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
            </tr>
          )}
        </table>

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

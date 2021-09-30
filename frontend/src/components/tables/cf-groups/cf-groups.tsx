import { Component, Host, h, State } from '@stencil/core';
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
  error: String;
  groups: Array<GroupsRow>;
}

@Component({
  tag: 'cf-groups',
  styleUrl: 'cf-groups.css',
  shadow: true,
})
export class CfGroups {
  @State() response: GroupsListResponse;

  async componentDidLoad() {
    this.response = await fetchAs<GroupsListRequest, GroupsListResponse>('groups/list', { token: globals.globalStore.state.token });
    console.log(this.response);
  }

  render() {
    return (
      <Host>
        <slot></slot>
        <h3>Groups</h3>
        <table>
          <tr>{this.response && this.response.groups && this.response.groups.length > 0 && Object.keys(this.response.groups[0]).map(key => <th>{key}</th>)}</tr>

          {this.response &&
            this.response.groups &&
            this.response.groups.map(item => (
              <tr>
                <td>{item.id}</td>
                <td>{item.name}</td>
                <td>{item.createdAt}</td>
                <td>{item.createdBy}</td>
                <td>{item.modifiedAt}</td>
                <td>{item.modifiedBy}</td>
              </tr>
            ))}
        </table>
      </Host>
    );
  }
}

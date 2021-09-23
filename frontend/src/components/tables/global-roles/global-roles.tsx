import { Component, Host, h, State } from '@stencil/core';
import { GenericResponse, globalRole } from '../../../common/types';
import { fetchAs } from '../../../common/utility';

class GlobalRolesRequest {
  // tableName: string;
}
class GlobalRolesResponse extends GenericResponse {
  globalRoles: globalRole[];
}

@Component({
  tag: 'global-roles',
  styleUrl: 'global-roles.css',
  shadow: true,
})
export class GlobalRoles {
  globalRoles: globalRole[] = [];
  componentWillLoad() {
    fetchAs<GlobalRolesRequest, GlobalRolesResponse>('globalRoles/read', {}).finally(() => {
      // set global state with data
      // globals.globalStore.state.globalRolesData = JSON.stringify(res.globalRoles);
      // this.globalRoles = res.globalRoles;
      // some weird cors error
    });
    this.globalRoles = [
      {
        id: 1,
        createdAt: '24th Sep',
        createdBy: 0,
        modifiedAt: '25th Sep',
        modifiedBy: 0,
        name: 'Admin',
        org: 1,
      },
      {
        id: 2,
        createdAt: '24th Sep',
        createdBy: 0,
        modifiedAt: '25th Sep',
        modifiedBy: 0,
        name: 'Project Manager',
        org: 1,
      },
      {
        id: 3,
        createdAt: '24th Sep',
        createdBy: 0,
        modifiedAt: '25th Sep',
        modifiedBy: 0,
        name: 'Team Lead',
        org: 1,
      },
    ];
  }
  render() {
    return (
      <Host>
        <slot></slot>
        <h1>Global Roles</h1>
        <table>
          <thead>
            {/* this will be fixed -> on a shared component, this will be passed in and use Map to preserve order */}
            <tr>
              <th>id</th>
              <th>created_at</th>
              <th>created_by</th>
              <th>modified_at</th>
              <th>modified_by</th>
              <th>name</th>
              <th>org</th>
            </tr>
          </thead>
          <tbody>
            {this.globalRoles.map(globalRole => (
              <tr>
                {/* can loop over these as well (using Map to preserve order) */}
                <td>{globalRole.id}</td>
                <td>{globalRole.createdAt}</td>
                <td>{globalRole.createdBy}</td>
                <td>{globalRole.modifiedAt}</td>
                <td>{globalRole.modifiedBy}</td>
                <td>{globalRole.name}</td>
                <td>{globalRole.org}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </Host>
    );
  }
}

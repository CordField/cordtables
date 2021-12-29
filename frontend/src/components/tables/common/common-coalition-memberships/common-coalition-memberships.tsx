import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateCoalitionMembershipExRequest {
  token: string;
  coalitionMembership: {
    coalition: number;
    organization: number;
  };
}
class CreateCoalitionMembershipExResponse extends GenericResponse {
  coalitionBembership: CommonCoalitionMembership;
}

class CommonCoalitionMembershipListRequest {
  token: string;
}

class CommonCoalitionMembershipListResponse {
  error: ErrorType;
  coalitionMemberships: CommonCoalitionMembership[];
}


class CommonCoalitionMembershipUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class CommonCoalitionMembershipUpdateResponse {
  error: ErrorType;
  coalitionMembership: CommonCoalitionMembership | null = null;
}

class DeleteCoalitionMembershipExRequest {
  id: number;
  token: string;
}

class DeleteCoalitionMembershipExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'common-coalition-memberships',
  styleUrl: 'common-coalition-memberships.css',
  shadow: true,
})
export class CommonCoalitionMemberships {

  @State() coalitionMembershipsResponse: CommonCoalitionMembershipListResponse;

  newCoalition: number;
  newOrganization: number;
  
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonCoalitionMembershipUpdateRequest, CommonCoalitionMembershipUpdateResponse>('common-coalition-memberships/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.coalitionMembershipsResponse = { error: ErrorType.NoError, coalitionMemberships: this.coalitionMembershipsResponse.coalitionMemberships.map(coalitionMembership => (coalitionMembership.id === id ? updateResponse.coalitionMembership : coalitionMembership)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteCoalitionMembershipExRequest, DeleteCoalitionMembershipExResponse>('common-coalition-memberships/delete', {
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
    this.coalitionMembershipsResponse = await fetchAs<CommonCoalitionMembershipListRequest, CommonCoalitionMembershipListResponse>('common-coalition-memberships/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  coalitionChange(event) {
    this.newCoalition = event.target.value;
  }

  organizationChange(event) {
    this.newOrganization = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateCoalitionMembershipExRequest, CreateCoalitionMembershipExResponse>('common-coalition-memberships/create-read', {
      token: globals.globalStore.state.token,
      coalitionMembership: {
        coalition: this.newCoalition,
        organization: this.newOrganization,
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
      field: 'coalition',
      displayName: 'Coalition',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'organization',
      displayName: 'Organization',
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
        {this.coalitionMembershipsResponse && <cf-table rowData={this.coalitionMembershipsResponse.coalitionMemberships} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="neo4jcoalition_id-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="coalition">Coalition</label>
              </span>
              <span class="form-thing">
                <input type="text" id="coalition" name="coalition" onInput={event => this.coalitionChange(event)} />
              </span>
            </div>

            <div id="organization-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="organization">Organization</label>
              </span>
              <span class="form-thing">
                <input type="number" id="organization" name="organization" onInput={event => this.organizationChange(event)} />
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

import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateOrgChartPositionExRequest {
  token: string;
  orgChartPosition: {
    organization: number;
    name: string;
  };
}
class CreateOrgChartPositionExResponse extends GenericResponse {
  orgChartPosition: CommonOrgChartPosition;
}

class CommonOrgChartPositionListRequest {
  token: string;
}

class CommonOrgChartPositionListResponse {
  error: ErrorType;
  orgChartPositions: CommonOrgChartPosition[];
}


class CommonOrgChartPositionUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class CommonOrgChartPositionUpdateResponse {
  error: ErrorType;
  orgChartPosition: CommonOrgChartPosition | null = null;
}

class DeleteOrgChartPositionExRequest {
  id: string;
  token: string;
}

class DeleteOrgChartPositionExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'common-org-chart-positions',
  styleUrl: 'common-org-chart-positions.css',
  shadow: true,
})
export class CommonOrgChartPositions {

  @State() orgChartPositionsResponse: CommonOrgChartPositionListResponse;

  newOrganization: number;
  newName: string;
  
  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonOrgChartPositionUpdateRequest, CommonOrgChartPositionUpdateResponse>('common-org-chart-positions/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.orgChartPositionsResponse = { error: ErrorType.NoError, orgChartPositions: this.orgChartPositionsResponse.orgChartPositions.map(orgChartPosition => (orgChartPosition.id === id ? updateResponse.orgChartPosition : orgChartPosition)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteOrgChartPositionExRequest, DeleteOrgChartPositionExResponse>('common-org-chart-positions/delete', {
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
    this.orgChartPositionsResponse = await fetchAs<CommonOrgChartPositionListRequest, CommonOrgChartPositionListResponse>('common-org-chart-positions/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  organizationChange(event) {
    this.newOrganization = event.target.value;
  }

  nameChange(event) {
    this.newName = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateOrgChartPositionExRequest, CreateOrgChartPositionExResponse>('common-org-chart-positions/create-read', {
      token: globals.globalStore.state.token,
      orgChartPosition: {
        organization: this.newOrganization,
        name: this.newName,
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
      field: 'organization',
      displayName: 'Organization',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'name',
      displayName: 'Name',
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
        {this.orgChartPositionsResponse && <cf-table rowData={this.orgChartPositionsResponse.orgChartPositions} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">

            <div id="organization-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="organization">Organization</label>
              </span>
              <span class="form-thing">
                <input type="number" id="organization" name="organization" onInput={event => this.organizationChange(event)} />
              </span>
            </div>

            <div id="name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="name">Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="name" name="name" onInput={event => this.nameChange(event)} />
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

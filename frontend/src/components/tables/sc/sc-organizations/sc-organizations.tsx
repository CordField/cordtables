import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateOrganizationExRequest {
  token: string;
  organization: {
    id: number;
    neo4j_id: string;
    address: string;
  };
}
class CreateOrganizationExResponse extends GenericResponse {
  organization: ScOrganization;
}

class ScOrganizationListRequest {
  token: string;
}

class ScOrganizationListResponse {
  error: ErrorType;
  organizations: ScOrganization[];
}


class ScOrganizationUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class ScOrganizationUpdateResponse {
  error: ErrorType;
  organization: ScOrganization | null = null;
}

class DeleteOrganizationExRequest {
  id: number;
  token: string;
}

class DeleteOrganizationExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'sc-organizations',
  styleUrl: 'sc-organizations.css',
  shadow: true,
})
export class ScOrganizations {

  @State() organizationsResponse: ScOrganizationListResponse;

  newNeo4j_id: string;
  newAddress: string;
  newId: number;
  
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScOrganizationUpdateRequest, ScOrganizationUpdateResponse>('sc-organizations/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.organizationsResponse = { error: ErrorType.NoError, organizations: this.organizationsResponse.organizations.map(organization => (organization.id === id ? updateResponse.organization : organization)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteOrganizationExRequest, DeleteOrganizationExResponse>('sc-organizations/delete', {
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
    this.organizationsResponse = await fetchAs<ScOrganizationListRequest, ScOrganizationListResponse>('sc-organizations/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  neo4j_idChange(event) {
    this.newNeo4j_id = event.target.value;
  }

  idChange(event) {
    this.newId = event.target.value;
  }

  addressChange(event) {
    this.newAddress = event.target.value;
  }


  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateOrganizationExRequest, CreateOrganizationExResponse>('sc-organizations/create-read', {
      token: globals.globalStore.state.token,
      organization: {
        id: this.newId,
        neo4j_id: this.newNeo4j_id,
        address: this.newAddress,
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
      field: 'neo4j_id',
      displayName: 'neo4j_id',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'address',
      displayName: 'Address',
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
        {this.organizationsResponse && <cf-table rowData={this.organizationsResponse.organizations} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">

            <div id="id-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="id">Common Organization ID</label>
              </span>
              <span class="form-thing">
                <input type="number" id="id" name="id" onInput={event => this.idChange(event)} />
              </span>
            </div>

            <div id="neo4j_id-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="neo4j_id">neo4j_id</label>
              </span>
              <span class="form-thing">
                <input type="text" id="neo4j_id" name="neo4j_id" onInput={event => this.neo4j_idChange(event)} />
              </span>
            </div>

            <div id="address-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="address">Address</label>
              </span>
              <span class="form-thing">
                <input type="text" id="address" name="address" onInput={event => this.addressChange(event)} />
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

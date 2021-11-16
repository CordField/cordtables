import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateFundingAccountExRequest {
  token: string;
  fundingAccount: {
    neo4j_id: string ;
    account_number: number;
    name: string;
  };
}
class CreateFundingAccountExResponse extends GenericResponse {
  fundingAccount: ScFundingAccount;
}

class ScFundingAccountListRequest {
  token: string;
}

class ScFundingAccountListResponse {
  error: ErrorType;
  fundingAccounts: ScFundingAccount[];
}


class ScFundingAccountUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class ScFundingAccountUpdateResponse {
  error: ErrorType;
  fundingAccount: ScFundingAccount | null = null;
}

class DeleteFundingAccountExRequest {
  id: number;
  token: string;
}

class DeleteFundingAccountExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'sc-funding-accounts',
  styleUrl: 'sc-funding-accounts.css',
  shadow: true,
})
export class ScFundingAccounts {

  @State() fundingAccountsResponse: ScFundingAccountListResponse;

  newNeo4j_id: string ;
  newAccount_number: number;
  newName: string;
 
  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScFundingAccountUpdateRequest, ScFundingAccountUpdateResponse>('sc-funding-accounts/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.fundingAccountsResponse = { error: ErrorType.NoError, fundingAccounts: this.fundingAccountsResponse.fundingAccounts.map(fundingAccount => (fundingAccount.id === id ? updateResponse.fundingAccount : fundingAccount)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteFundingAccountExRequest, DeleteFundingAccountExResponse>('sc-funding-accounts/delete', {
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
    this.fundingAccountsResponse = await fetchAs<ScFundingAccountListRequest, ScFundingAccountListResponse>('sc-funding-accounts/list', {
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

  account_numberChange(event) {
    this.newAccount_number = event.target.value;
  }

  nameChange(event) {
    this.newName = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateFundingAccountExRequest, CreateFundingAccountExResponse>('sc-funding-accounts/create-read', {
      token: globals.globalStore.state.token,
      fundingAccount: {
        neo4j_id: this.newNeo4j_id,
        account_number: this.newAccount_number,
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
      field: 'neo4j_id',
      displayName: 'neo4j_id',
      width: 150,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'account_number',
      displayName: 'Account Number',
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
        {this.fundingAccountsResponse && <cf-table rowData={this.fundingAccountsResponse.fundingAccounts} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="neo4j_id-holder" class="form-input-item form-thing">
              <span class="neo4j_id-thing">
                <label htmlFor="neo4j_id">No4j_id</label>
              </span>
              <span class="form-thing">
                <input type="text" id="neo4j_id" name="neo4j_id" onInput={event => this.neo4j_idChange(event)} />
              </span>
            </div>

            <div id="account_number-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="account_number">Account Number</label>
              </span>
              <span class="form-thing">
                <input type="number" id="account_number" name="account_number" onInput={event => this.account_numberChange(event)} />
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

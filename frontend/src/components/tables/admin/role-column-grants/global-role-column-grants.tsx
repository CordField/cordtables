import { Component, Host, h, Listen, State, Prop } from '@stencil/core';
import { RouterHistory } from '@stencil/router';
import { GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';

class readAllResponse extends GenericResponse {
  id: string;
  access_level?: String;
  column_name?: String;
  created_at?: String;
  created_by?: string;
  role?: string;
  modified_at?: String;
  modified_by?: string;
  table_name?: String;
  response?: Array<any>;
}

class readOneRequest {
  id: string;
}

class create {
  access_level?: String;
  column_name?: String;
  created_at?: String;
  created_by?: string;
  role?: string;
  table_name?: String;
  response?: Array<any>;
  token: String;
}

class update {
  id: string;
  access_level?: String;
  column_name?: String;
  created_at?: String;
  created_by?: string;
  modified_at?: String;
  modified_by?: string;
  role?: string;
  table_name?: String;
  response?: Array<any>;
  token: String;
}

class deleteRow {
  id: string;
  token: String;
}

const tableColumns = ['Id', 'Access Level', 'Column Name', 'Created At', 'Created By', 'Global Role', 'Modified At', 'Modified By', 'Table Name'];

interface readOne {
  id: string;
  access_level?: string;
  column_name?: string;
  created_at?: string;
  created_by?: string;
  role?: string;
  modified_at?: string;
  modified_by?: string;
  table_name?: string;
}

let rowId = '';

@Component({
  tag: 'role-column-grants',
  styleUrl: 'global-role-column-grants.css',
  shadow: true,
})
export class GlobalRoleColumnGrants {
  @Prop() history: RouterHistory;
  @State() email: string;
  @State() password: string;
  @State() isOpen: boolean;
  @State() dataAll: Array<object>;
  @State() dataOne: Array<readOne>;
  @State() selectValue: string;
  @State() readOneValues: readOne = {
    id: '',
    access_level: '',
    column_name: '',
    created_at: '',
    created_by:'',
    role: '',
    modified_at: '',
    modified_by: '',
    table_name: '',
  };

  async componentWillLoad() {
    await this.loadData();
  }

  async loadData() {
    const result = await fetchAs<{}, readAllResponse>('table/role-column-grants', {});
    if (result && result?.response) this.dataAll = result.response;
  }

  @Listen('rowClicked')
  async handleClick(event) {
    if (event && event.detail) {
      rowId = event.detail;
      const result = await fetchAs<readOneRequest, readAllResponse>('table/role-column-grants-read-one', { id: rowId });
      if (result && result?.response) {
        result.response.map(item => {
          this.readOneValues.id = item.id;
          this.selectValue = item.access_level;
          this.readOneValues.column_name = item.column_name;
          this.readOneValues.created_at = item.created_at;
          this.readOneValues.created_by = item.created_by;
          this.readOneValues.role = item.role;
          this.readOneValues.modified_at = item.modified_at;
          this.readOneValues.modified_by = item.modified_by;
          this.readOneValues.table_name = item.table_name;
        });
      }
      this.isOpen = !this.isOpen;
    }
  }

  @Listen('modalClosed')
  handleModalClose(event) {
    if (event && event.detail) {
      this.isOpen = !this.isOpen;
    }
  }

  @Listen('modalOkay')
  async handleModalOkay(event) {
    if (event && event.detail) {
      if (this.readOneValues.id === '') {
        try {
          await fetchAs<create, readAllResponse>('table/role-column-grants-create', {
            access_level: this.selectValue,
            column_name: this.readOneValues.column_name,
            created_by: this.readOneValues.created_by,
            role: this.readOneValues.role,
            table_name: this.readOneValues.table_name,
            token: localStorage.getItem('token'),
          });

          await this.loadData();
        } catch (error) {
          console.log('Error during row insertion: ', error);
        }
      } else {
        try {
          await fetchAs<update, readAllResponse>('table/role-column-grants-update', {
            id: this.readOneValues.id,
            access_level: this.selectValue,
            column_name: this.readOneValues.column_name,
            role: this.readOneValues.role,
            table_name: this.readOneValues.table_name,
            token: localStorage.getItem('token'),
          });

          await this.loadData();
        } catch (error) {
          console.log('Error during row update: ', error);
        }
      }
      this.isOpen = !this.isOpen;
    }
  }

  @Listen('modalDelete')
  async handleModalDelete(event) {
    if (event && event.detail) {
      if (this.readOneValues.id !== '') {
        try {
          await fetchAs<deleteRow, readAllResponse>('table/role-column-grants-delete', {
            id: this.readOneValues.id,
            token: localStorage.getItem('token'),
          });

          await this.loadData();
        } catch (error) {
          console.log('An error ocurred when trying to delete the record: ', error);
        }
      }
      this.isOpen = !this.isOpen;
    }
  }

  handleChangeAccessLevel(event) {
    this.selectValue = event.target.value;
  }
  handleChangeColumnName(event) {
    this.readOneValues.column_name = event.target.value;
  }
  handleChangeGlobalRole(event) {
    this.readOneValues.role = event.target.value;
  }
  handleChangeTableName(event) {
    this.readOneValues.table_name = event.target.value;
  }

  cleanFields() {
    this.readOneValues.id = '';
    this.readOneValues.column_name = '';
    this.readOneValues.created_at = '';
    this.readOneValues.created_by = null;
    this.readOneValues.role = null;
    this.readOneValues.modified_at = '';
    this.readOneValues.modified_by = null;
    this.readOneValues.table_name = '';
  }

  onInsertNew() {
    this.cleanFields();
    this.isOpen = !this.isOpen;
  }

  render() {
    return (
      <Host>
        <div class="insertNewButton">
          <button onClick={() => this.onInsertNew()}>Insert New</button>
        </div>
        <create-update-modal isOpen={this.isOpen}>
          <div class="container">
            <div class="row">
              <div class="col">
                <select onInput={event => this.handleChangeAccessLevel(event)}>
                  <option value="Write" selected>
                    Write
                  </option>
                  <option value="Read" selected={this.selectValue === 'Read'}>
                    Read
                  </option>
                  <option value="Admin" selected={this.selectValue === 'Admin'}>
                    Admin
                  </option>
                </select>
              </div>
              <div class="col">
                <input type="text" placeholder="Column Name" value={this.readOneValues.column_name} onInput={event => this.handleChangeColumnName(event)}>
                  {' '}
                </input>
              </div>
              <div class="col">
                <input type="text" placeholder="Global Role" value={this.readOneValues.role} onInput={event => this.handleChangeGlobalRole(event)}>
                  {' '}
                </input>
              </div>
              <div class="col">
                <input type="text" placeholder="Table Name" value={this.readOneValues.table_name} onInput={event => this.handleChangeTableName(event)}>
                  {' '}
                </input>
              </div>
            </div>
          </div>
        </create-update-modal>
        <slot></slot>
        <generic-table name="Global Role Column Grants" columns={tableColumns} values={this.dataAll}></generic-table>
      </Host>
    );
  }
}

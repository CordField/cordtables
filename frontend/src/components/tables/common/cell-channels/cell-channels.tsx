import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
class CreateCommonCellChannelsRequest {
  token: string;
  cell_channel: {
    column_name: string;
    row: number;
    table_name: string;
  };
}

class CommonCellChannelsRow {
  id: number;
  table_name: string;
  column_name: string;
  row: number;
  created_at: string;
  created_by: number;
  modified_at: string;
  modified_by: number;
  owning_person: number;
  owning_group: number;
}

class CreateCommonCellChannelResponse extends GenericResponse {
  cell_channel: CommonCellChannelsRow;
}

class CreateCommonCellChannelRequest {
  token: string;
}

class CommonTicketsListResponse {
  error: ErrorType;
  tickets: CommonCellChannelsRow[];
}

class CommonTicketsUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class CommonTicketsUpdateResponse {
  error: ErrorType;
  ticket: CommonCellChannelsRow | null = null;
}

class DeleteTicketRequest {
  id: number;
  token: string;
}

class DeleteTicketResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'cell-channels-table',
  styleUrl: 'cell-channels.css',
  shadow: true,
})
export class CellChannelsTable {
  @State() commonTicketsResponse: CommonTicketsListResponse;
  @State() newTableName: string;
  @State() newRow: number;
  @State() newColumnName: string;

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonTicketsUpdateRequest, CommonTicketsUpdateResponse>('common-cell-channels/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    if (updateResponse.error == ErrorType.NoError) {
      this.commonTicketsResponse = { error: ErrorType.NoError, tickets: this.commonTicketsResponse.tickets.map(ticket => (ticket.id === id ? updateResponse.ticket : ticket)) };
      return true;
    } else {
      alert(updateResponse.error);
      return false;
    }
  };

  handleDelete = async id => {
    const result = await fetchAs<DeleteTicketRequest, DeleteTicketResponse>('common-cell-channels/delete', {
      id,
      token: globals.globalStore.state.token,
    });
    if (result.error === ErrorType.NoError) {
      this.getList();
      return true;
    } else {
      return false;
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
    // {
    //   field: 'ticket_status',
    //   displayName: 'Ticket Status',
    //   width: 100,
    //   editable: true,
    //   updateFn: this.handleUpdate,
    //   selectOptions: [
    //     { display: `-`, value: '' },
    //     { display: `Open`, value: 'Open' },
    //     { display: 'Blocked', value: 'Blocked' },
    //     { display: 'Closed', value: 'Closed' },
    //   ],
    // },
    {
      field: 'table_name',
      displayName: 'Table Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'column_name',
      displayName: 'Column Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'row',
      displayName: 'row',
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
  }

  async getList() {
    this.commonTicketsResponse = await fetchAs<CreateCommonCellChannelRequest, CommonTicketsListResponse>('common-cell-channels/list', {
      token: globals.globalStore.state.token,
    });
  }

  columnNameChange(event) {
    this.newColumnName = event.target.value;
  }

  rowChange(event) {
    this.newRow = event.target.value;
  }

  tableNameChange(event) {
    this.newTableName = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const result = await fetchAs<CreateCommonCellChannelsRequest, CreateCommonCellChannelResponse>('common-cell-channels/create-read', {
      token: globals.globalStore.state.token,
      cell_channel: {
        column_name: this.newColumnName,
        table_name: this.newTableName,
        row: this.newRow,
      },
    });

    if (result.error === ErrorType.NoError) {
      globals.globalStore.state.editMode = false;
      this.getList();
    }
  };

  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.commonTicketsResponse && <cf-table rowData={this.commonTicketsResponse.tickets} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <label>
              {' '}
              <strong> New Cell Channel: </strong>
            </label>
            <div id="table-name-holder" class="form-input-item form-thing">
              <br />
              <span class="form-thing">
                <label htmlFor="">Table Name:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="channel-table-name" name="channel-table-name" onInput={event => this.tableNameChange(event)} />
              </span>

              {/* <span class="form-thing">
                <select id="ticket-status" name="ticket-status" onInput={event => this.columnNameChange(event)}>
                  <option value="-">-</option>
                  <option value="Open">Open</option>
                  <option value="Blocked">Blocked</option>
                  <option value="Closed">Closed</option>
                </select>
              </span> */}
            </div>
            <div id="column-name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="column-name">Column Name:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="column-name" name="column-name" onInput={event => this.columnNameChange(event)} />
              </span>
            </div>
            <div id="row-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="row">Row:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="row" name="row" onInput={event => this.tableNameChange(event)} />
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

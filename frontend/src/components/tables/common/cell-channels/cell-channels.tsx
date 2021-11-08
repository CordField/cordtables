import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';

class CommonCellChannel {
  id?: number | undefined;
  table_name?: string | undefined;
  column_name?: string | undefined;
  row?: number | undefined;
  created_at?: string | undefined;
  created_by?: number | undefined;
  modified_at?: string | undefined;
  modified_by?: number | undefined;
  owning_person?: number | undefined;
  owning_group?: number | undefined;
}
class CreateCommonCellChannelsRequest {
  token: string;
  cell_channel: {
    column_name: string;
    row: number;
    table_name: string;
  };
}

class CreateCommonCellChannelResponse extends GenericResponse {
  cell_channel: CommonCellChannel;
}

class CommonCellChannelsListRequest {
  token: string;
}

class CommonCellChannelsListResponse {
  error: ErrorType;
  cell_channels: CommonCellChannel[];
}

class CommonCellChannelsUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class CommonCellChannelsUpdateResponse {
  error: ErrorType;
  cell_channel: CommonCellChannel | null = null;
}

class DeleteCommonCellChannelsRequest {
  id: number;
  token: string;
}

class DeleteCommonCellChannelsResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'common-cell-channels',
  styleUrl: 'cell-channels.css',
  shadow: true,
})
export class CellChannelsTable {
  @State() commonCellChannelsResponse: CommonCellChannelsListResponse;
  newTableName: string;
  newRow: number;
  newColumnName: string;

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonCellChannelsUpdateRequest, CommonCellChannelsUpdateResponse>('common-cell-channels/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    if (updateResponse.error == ErrorType.NoError) {
      this.commonCellChannelsResponse = {
        error: ErrorType.NoError,
        cell_channels: this.commonCellChannelsResponse.cell_channels.map(cellChannel => (cellChannel.id === id ? updateResponse.cell_channel : cellChannel)),
      };
      return true;
    } else {
      alert(updateResponse.error);
      return false;
    }
  };

  handleDelete = async id => {
    const result = await fetchAs<DeleteCommonCellChannelsRequest, DeleteCommonCellChannelsResponse>('common-cell-channels/delete', {
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

  async componentWillLoad() {
    await this.getList();
  }

  async getList() {
    this.commonCellChannelsResponse = await fetchAs<CommonCellChannelsListRequest, CommonCellChannelsListResponse>('common-cell-channels/list', {
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

  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.commonCellChannelsResponse && <cf-table rowData={this.commonCellChannelsResponse.cell_channels} columnData={this.columnData}></cf-table>}

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
                <input type="text" id="row" name="row" onInput={event => this.rowChange(event)} />
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

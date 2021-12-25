import { Component, Host, h, State, Prop } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
class CreateTicketRequest {
  token: string;
  ticket: {
    ticket_status: string;
    parent: string;
    content: string;
  };
}

class CommonTicketsRow {
  id: string;
  ticket_status: string;
  parent: string;
  content: string;
  created_at: string;
  created_by: string;
  modified_at: string;
  modified_by: string;
  owning_person: string;
  owning_group: string;
}

class CreateTicketResponse extends GenericResponse {
  ticket: CommonTicketsRow;
}

class CommonTicketsListRequest {
  token: string;
}

class CommonTicketsListResponse {
  error: ErrorType;
  tickets: CommonTicketsRow[];
}

class CommonTicketsUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class CommonTicketsUpdateResponse {
  error: ErrorType;
  ticket: CommonTicketsRow | null = null;
}

class DeleteTicketRequest {
  id: string;
  token: string;
}

class DeleteTicketResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'tickets-table',
  styleUrl: 'tickets-table.css',
  shadow: true,
})
export class TicketsTable {
  @Prop() onlyShowCreate: boolean = false;
  @State() commonTicketsResponse: CommonTicketsListResponse;
  newTicketStatusName: string;
  newParent: string;
  newContent: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonTicketsUpdateRequest, CommonTicketsUpdateResponse>('common/tickets/update-read', {
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
    const result = await fetchAs<DeleteTicketRequest, DeleteTicketResponse>('common/tickets/delete', {
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
    {
      field: 'ticket_status',
      displayName: 'Ticket Status',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `Open`, value: 'Open' },
        { display: 'Blocked', value: 'Blocked' },
        { display: 'Closed', value: 'Closed' },
      ],
    },
    {
      field: 'parent',
      displayName: 'Parent',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'content',
      displayName: 'Content',
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
    this.commonTicketsResponse = await fetchAs<CommonTicketsListRequest, CommonTicketsListResponse>('common/tickets/list', {
      token: globals.globalStore.state.token,
    });
  }

  ticketStatusNameChange(event) {
    this.newTicketStatusName = event.target.value;
  }

  parentChange(event) {
    this.newParent = event.target.value;
  }

  contentChange(event) {
    this.newContent = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const result = await fetchAs<CreateTicketRequest, CreateTicketResponse>('common/tickets/create-read', {
      token: globals.globalStore.state.token,
      ticket: {
        ticket_status: this.newTicketStatusName,
        parent: this.newParent,
        content: this.newContent,
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

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {(globals.globalStore.state.editMode === true || this.onlyShowCreate === true) && (
          <form class="form-thing">
            <div id="ticket-status-holder" class="form-input-item form-thing">
              <label>
                {' '}
                <strong> New Ticket: </strong>
              </label>
              <br />
              <span class="form-thing">
                <label htmlFor="ticket-status">Ticket Status:</label>
              </span>
              <span class="form-thing">
                <select id="ticket-status" name="ticket-status" onInput={event => this.ticketStatusNameChange(event)}>
                  <option value="-">-</option>
                  <option value="Open">Open</option>
                  <option value="Blocked">Blocked</option>
                  <option value="Closed">Closed</option>
                </select>
              </span>
            </div>
            <div id="ticket-parent-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="ticket-parent">Ticket Parent:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="ticket-parent" name="ticket-parent" onInput={event => this.parentChange(event)} />
              </span>
            </div>
            <div id="content-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="content">Content:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="content" name="content" onInput={event => this.contentChange(event)} />
              </span>
            </div>
            <span class="form-thing">
              <input id="create-button" type="submit" value="Create" onClick={this.handleInsert} />
            </span>
          </form>
        )}
        {/* table abstraction */}
        {this.commonTicketsResponse && this.onlyShowCreate === false && <cf-table rowData={this.commonTicketsResponse.tickets} columnData={this.columnData}></cf-table>}
      </Host>
    );
  }
}

import { Component, Host, h, State, Prop } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';

class CreateTicketGraphRequest{
  token: string;
  ticket_graph:{
    from_ticket: number;
    to_ticket: number;
  }
}

class CommonTicketGraphRow{
  id : string;
  from_ticket: number;
  to_ticket : number;
  created_at: string;
  created_by: number;
  modified_at: string;
  modified_by: number;
  owning_person: number;
  owning_group: number;
}

class CreateTicketGraphResponse extends GenericResponse {
  ticket_graph: CommonTicketGraphRow;
}

class CommonTicketGraphListRequest{
  token: string;
}

class CommonTicketGraphResponse {
  error: ErrorType;
  ticket_graph: CommonTicketGraphRow[];
}

class CommonTicketGraphUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class CommonTicketGraphUpdateResponse {
  error: ErrorType;
  ticket_graph: CommonTicketGraphRow | null = null;
}

class DeleteTicketRequest {
  id: string;
  token: string;
}

class DeleteTicketResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'ticket-graph',
  styleUrl: 'ticket-graph.css',
  shadow: true,
})
export class TicketGraph {

  @Prop() onlyShowCreate: boolean = false;
  @State() commonTicketGraphResponse: CommonTicketGraphResponse;
  newFromTicket: number;
  newToTicket: number;



  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> =>{
    const updateResponse = await fetchAs<CommonTicketGraphUpdateRequest, CommonTicketGraphUpdateResponse>('common-ticket-graph/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    if (updateResponse.error == ErrorType.NoError) {
      this.commonTicketGraphResponse = { error: ErrorType.NoError, ticket_graph: this.commonTicketGraphResponse.ticket_graph.map(ticket_graph => (ticket_graph.id === id ? updateResponse.ticket_graph : ticket_graph)) };
      return true;
    } else {
      alert(updateResponse.error);
      return false;
  }
}

handleDelete = async id => {
  const result = await fetchAs<DeleteTicketRequest, DeleteTicketResponse>('common-ticket-graph/delete', {
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
    field: 'from_ticket',
    displayName: 'From Ticket',
    width: 200,
    editable: true,
    updateFn: this.handleUpdate,
  },
  {
    field: 'to_ticket',
    displayName: 'To Ticket',
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
  this.commonTicketGraphResponse = await fetchAs<CommonTicketGraphListRequest, CommonTicketGraphResponse>('common-ticket-graph/list', {
    token: globals.globalStore.state.token,
  });
}

fromTicketChange(event) {
  this.newFromTicket = event.target.value;
}

toTicketChange(event) {
  this.newToTicket = event.target.value;
}


handleInsert = async (event: MouseEvent) => {
  event.preventDefault();
  event.stopPropagation();  

  const result = await fetchAs<CreateTicketGraphRequest, CreateTicketGraphResponse>('common-ticket-graph/create-read', {
    token: globals.globalStore.state.token,
    ticket_graph: {
      from_ticket: this.newFromTicket,
      to_ticket: this.newToTicket,
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
             <div id="from-ticket-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="from-ticket">From Ticket:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="from-ticket" name="from-ticket" onInput={event => this.fromTicketChange(event)} />
              </span>
            </div>
            <div id="to-ticket-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="to-ticket">To Ticket:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="to-ticket" name="to-ticket" onInput={event => this.toTicketChange(event)} />
              </span>
            </div>
            <span class="form-thing">
              <input id="create-button" type="submit" value="Create" onClick={this.handleInsert} />
            </span>
          </form>
        )}
        {/* table abstraction */}
        {this.commonTicketGraphResponse && this.onlyShowCreate ===  false && <cf-table rowData={this.commonTicketGraphResponse.ticket_graph} columnData={this.columnData}></cf-table>}
      </Host>
    );
  }

}

import { Component, Host, h, State, Prop } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';

class CreateTicketAssignmentRequest {
  token: string;
  ticket_assignment: {
    ticket: number;
    person: number;
  };
}

class CommonTicketAssignmentRow {
  id: string;
  ticket: number;
  person: number;
  created_at: string;
  created_by: number;
  modified_at: string;
  modified_by: number;
  owning_person: number;
  owning_group: number;
}

class CreateTicketAssignmentResponse extends GenericResponse {
  ticket_assignment: CommonTicketAssignmentRow;
}
class CommonTicketAssignmentListRequest {
  token: string;
}

class CommonTicketAssignmentResponse {
  error: ErrorType;
  ticket_assignment: CommonTicketAssignmentRow[];
}
class CommonTicketAssignmentUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}
class CommonTicketAssignmentUpdateResponse {
  error: ErrorType;
  ticket_assignment: CommonTicketAssignmentRow | null = null;
}

class DeleteTicketRequest {
  id: string;
  token: string;
}

class DeleteTicketResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'ticket-assignments',
  styleUrl: 'ticket-assignments.css',
  shadow: true,
})
export class TicketAssignment {
  @Prop() onlyShowCreate: boolean = false;
  @State() commonTicketAssignmentResponse: CommonTicketAssignmentResponse;
  newTicketAssignment: number;
  newPerson: number;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonTicketAssignmentUpdateRequest, CommonTicketAssignmentUpdateResponse>('common/ticket-assignments/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    if (updateResponse.error == ErrorType.NoError) {
      this.commonTicketAssignmentResponse = {
        error: ErrorType.NoError,
        ticket_assignment: this.commonTicketAssignmentResponse.ticket_assignment.map(ticket_assignment =>
          ticket_assignment.id === id ? updateResponse.ticket_assignment : ticket_assignment,
        ),
      };
      return true;
    } else {
      alert(updateResponse.error);
      return false;
    }
  };

  handleDelete = async id => {
    const result = await fetchAs<DeleteTicketRequest, DeleteTicketResponse>('common/ticket-assignments/delete', {
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
      field: 'ticket',
      displayName: 'Ticket ID',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'person',
      displayName: 'Person',
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
    this.commonTicketAssignmentResponse = await fetchAs<CommonTicketAssignmentListRequest, CommonTicketAssignmentResponse>('common/ticket-assignments/list', {
      token: globals.globalStore.state.token,
    });
  }

  ticketAssignmentChange(event) {
    this.newTicketAssignment = event.target.value;
  }

  personChange(event) {
    this.newPerson = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const result = await fetchAs<CreateTicketAssignmentRequest, CreateTicketAssignmentResponse>('common/ticket-assignments/create-read', {
      token: globals.globalStore.state.token,
      ticket_assignment: {
        ticket: this.newTicketAssignment,
        person: this.newPerson,
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
            <div id="ticket-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="ticket">Ticket:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="ticket" name="ticket" onInput={event => this.ticketAssignmentChange(event)} />
              </span>
            </div>
            <div id="person-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="person">Person:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="person" name="person" onInput={event => this.personChange(event)} />
              </span>
            </div>
            <span class="form-thing">
              <input id="create-button" type="submit" value="Create" onClick={this.handleInsert} />
            </span>
          </form>
        )}
        {/* table abstraction */}
        {this.commonTicketAssignmentResponse && this.onlyShowCreate === false && (
          <cf-table rowData={this.commonTicketAssignmentResponse.ticket_assignment} columnData={this.columnData}></cf-table>
        )}
      </Host>
    );
  }
}

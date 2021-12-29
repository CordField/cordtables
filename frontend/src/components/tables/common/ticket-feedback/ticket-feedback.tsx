import { Component, Host, h, State, Prop } from '@stencil/core';
import { isThisMinute } from 'date-fns';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';

class CreateTicketFeedbackRequest {
  token: string;
  ticket_feedback: {
    ticket: number;
    stake_holder: number;
    feedback: string;
  };
}

class CommonTicketFeedbackRow {
  id: number;
  ticket: number;
  stake_holder: number;
  feedback: string;
  created_at: string;
  created_by: number;
  modified_at: string;
  modified_by: number;
  owning_person: number;
  owning_group: number;
}

class CreateTicketFeedbackResponse extends GenericResponse {
  ticket_feedback: CommonTicketFeedbackRow;
}

class CommonTicketFeedbackListRequest {
  token: string;
}

class CommonTicketFeedbackResponse {
  error: ErrorType;
  ticket_feedback: CommonTicketFeedbackRow[];
}

class CommonTicketFeedbackUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class CommonTicketFeedbackUpdateResponse {
  error: ErrorType;
  ticket_feedback: CommonTicketFeedbackRow | null = null;
}

class DeleteTicketFeedbackRequest {
  id: number;
  token: string;
}

class DeleteTicketFeedbackResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'ticket-feedback',
  styleUrl: 'ticket-feedback.css',
  shadow: true,
})
export class TicketFeedback {
  @Prop() onlyShowCreate: boolean = false;
  @State() CommonTicketFeedbackResponse: CommonTicketFeedbackResponse;
  newTicket: number;
  newStakeHolder: number;
  newFeedback: string;

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonTicketFeedbackUpdateRequest, CommonTicketFeedbackUpdateResponse>('common/ticket-feedback/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    if (updateResponse.error == ErrorType.NoError) {
      this.CommonTicketFeedbackResponse = {
        error: ErrorType.NoError,
        ticket_feedback: this.CommonTicketFeedbackResponse.ticket_feedback.map(ticket_feedback => (ticket_feedback.id === id ? updateResponse.ticket_feedback : ticket_feedback)),
      };
      return true;
    } else {
      alert(updateResponse.error);
      return false;
    }
  };

  handleDelete = async id => {
    const result = await fetchAs<DeleteTicketFeedbackRequest, DeleteTicketFeedbackResponse>('common/ticket-feedback/delete', {
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
      displayName: 'Ticket',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'stake_holder',
      displayName: 'Stakeholder',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'feedback',
      displayName: 'Feedback',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `Satisfied`, value: 'Satisfied' },
        { display: 'Unsatisfied', value: 'Unsatisfied' },
      ],
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
    this.CommonTicketFeedbackResponse = await fetchAs<CommonTicketFeedbackListRequest, CommonTicketFeedbackResponse>('common/ticket-feedback/list', {
      token: globals.globalStore.state.token,
    });
  }

  ticketChange(event) {
    this.newTicket = event.target.value;
  }

  stakeholderChange(event) {
    this.newStakeHolder = event.target.value;
  }

  feedbackChange(event) {
    this.newFeedback = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const result = await fetchAs<CreateTicketFeedbackRequest, CreateTicketFeedbackResponse>('common/ticket-feedback/create-read', {
      token: globals.globalStore.state.token,
      ticket_feedback: {
        ticket: this.newTicket,
        stake_holder: this.newStakeHolder,
        feedback: this.newFeedback,
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
                <input type="text" id="ticket" name="ticket" onInput={event => this.ticketChange(event)} />
              </span>
            </div>
            <div id="stakeholder-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="stakeholder">Stakeholder:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="stakeholder" name="stakeholder" onInput={event => this.stakeholderChange(event)} />
              </span>
            </div>
            <div id="feedback-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="feedback">Feedback:</label>
              </span>
              <span class="form-thing">
                <select id="feedback" name="feedback" onInput={event => this.feedbackChange(event)}>
                  <option value="-">-</option>
                  <option value="Satisfied">Satisfied</option>
                  <option value="Unsatisfied">Unsatisfied</option>
                </select>
              </span>
            </div>
            <span class="form-thing">
              <input id="create-button" type="submit" value="Create" onClick={this.handleInsert} />
            </span>
          </form>
        )}
        {/* table abstraction */}
        {this.CommonTicketFeedbackResponse && this.onlyShowCreate === false && (
          <cf-table rowData={this.CommonTicketFeedbackResponse.ticket_feedback} columnData={this.columnData}></cf-table>
        )}
      </Host>
    );
  }
}

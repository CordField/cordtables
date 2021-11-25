import { Component, Host, h, State, Watch } from '@stencil/core';
import { globals } from '../../../core/global.store';
import { ColumnDescription } from '../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../common/types';
import { fetchAs } from '../../../common/utility';


class CommonTicketsPageListRequest{
  token: string;
}

class CommonTicketsPageRow{
  id : number;
  ticket_status : string;
  parent : number;
  content : string;
  estimated_total_time : number;
  blocked_by: number;
  assigned_person: number;
  total_time_worked : number;
  comment: string;
  feedback: string;
}


class CommonTicketsPageResponse {
  error: ErrorType;
  workflow: CommonTicketsPageRow[];
}

@Component({
  tag: 'tickets-page',
  styleUrl: 'tickets-page.css',
  shadow: true,
})
export class WorkflowsTable {

  @State() isCreateNewTicketVisible: boolean = false;
  @State() isAddEstimateToTicketVisible: boolean = false;
  @State() isAssignATicketVisible: boolean = false;
  @State() isBlockATicketVisible: boolean = false;
  @State() isAddAWorkRecordVisible: boolean = false;
  @State() isAddFeedBacktoTicketVisible: boolean = false;
  @State() commonTicketsPageResponse: CommonTicketsPageResponse;

  columnData: ColumnDescription[] = [
    {
      field: 'id',
      displayName: 'ID',
      width: 50,
      editable: false,
    },
    {
      field: 'ticket_status',
      displayName: 'Ticket Status',
      width: 100,
      editable: false,
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
      editable: false,
    },
    {
      field: 'content',
      displayName: 'Content',
      width: 200,
      editable: false,
    },
    {
      field: 'estimated_total_time',
      displayName: 'Total Time Estimated',
      width: 250,
      editable: false,
    },
    {
      field: 'blocked_by',
      displayName: 'Blocked By',
      width: 100,
      editable: false,
    },
    {
      field: 'assigned_person',
      displayName: 'Assigned Person',
      width: 250,
      editable: false,
    },
    {
      field: 'total_time_worked',
      displayName: 'Total Time Worked',
      width: 100,
      editable: false,
    },
    {
      field: 'comment',
      displayName: 'Comment',
      width: 100,
      editable: false,
    },
    {
      field: 'feedback',
      displayName: 'Feedback',
      width: 100,
      editable: false,
    },
  ];
  
  async componentWillLoad() {
    await this.getList();
  }
  
  async getList() {
    this.commonTicketsPageResponse = await fetchAs<CommonTicketsPageListRequest, CommonTicketsPageResponse>('common-workflows/list', {
      token: globals.globalStore.state.token,
    });
  }

  createNewTicket = () => {
    if (globals.globalStore.state.editMode === false) this.isCreateNewTicketVisible = !this.isCreateNewTicketVisible;
  }

  addEstimateToTicket = () => {
    if (globals.globalStore.state.editMode === false) this.isAddEstimateToTicketVisible = !this.isAddEstimateToTicketVisible;
  }

  assignATicket = () =>{
    if (globals.globalStore.state.editMode === false) this.isAssignATicketVisible = !this.isAssignATicketVisible;
  }

  blockTicket = () =>{
    if (globals.globalStore.state.editMode === false) this.isBlockATicketVisible = !this.isBlockATicketVisible;
  }

  addWorkRecord = () => {
    if (globals.globalStore.state.editMode === false) this.isAddAWorkRecordVisible = !this.isAddAWorkRecordVisible;
  }

  addFeedbackToTicket = () => {
    if (globals.globalStore.state.editMode === false) this.isAddFeedBacktoTicketVisible = !this.isAddFeedBacktoTicketVisible;
  }

  render() {
    return (
      <Host>
         <slot></slot>
        <div id="main">
          <button onClick={this.createNewTicket}>Create New Ticket</button>
          <button onClick={this.addEstimateToTicket}>Add Estimate To Ticket</button>
          <button onClick={this.assignATicket}> Assign a Ticket</button>
          <button onClick={this.blockTicket}>Block a Ticket</button>
          <button onClick={this.addWorkRecord}>Add a Work Record</button>
          <button onClick={this.addFeedbackToTicket}>Add Feedback to Ticket</button>
        </div>

        {/*Beggining of conditional rendering*/}

        { this.isCreateNewTicketVisible  === true && (
        <div id="contentTickets">
          <tickets-table onlyShowCreate={this.isCreateNewTicketVisible}></tickets-table> 
        </div>)}
        { this.isAddEstimateToTicketVisible  === true && (
        <div id="estimatesTicket">
          <span> <strong> New Estimate To Ticket: </strong> </span>
          <work-estimates onlyShowCreate={this.isAddEstimateToTicketVisible}></work-estimates> 
        </div>)}
        { this.isAssignATicketVisible  === true && (
        <div id="ticketAssignment">
          <span> <strong> Assign Person To Ticket: </strong> </span>
          <ticket-assignments onlyShowCreate={this.isAssignATicketVisible}></ticket-assignments> 
        </div>)}
        { this.isBlockATicketVisible  === true && (
        <div id="ticketGraph">
          <span> <strong> Block a Ticket: </strong> </span>
          <ticket-graph onlyShowCreate={this.isBlockATicketVisible}></ticket-graph> 
        </div>)}
        { this.isAddAWorkRecordVisible  === true && (
        <div id="workRecord">
          <span> <strong> Add a Work Record: </strong> </span>
          <work-records onlyShowCreate={this.isAddAWorkRecordVisible}></work-records> 
        </div>)}
        { this.isAddFeedBacktoTicketVisible  === true && (
        <div id="workFeedback">
          <span> <strong> Add Feedback: </strong> </span>
          <ticket-feedback onlyShowCreate={this.isAddFeedBacktoTicketVisible}></ticket-feedback> 
        </div>)}
        
        {/*End of conditional rendering*/}
        
        <h2> Tickets List </h2>
        {/* table abstraction */}
        {<cf-table rowData={this.commonTicketsPageResponse.workflow} columnData={this.columnData}></cf-table>}
      </Host>
    );
  }

}

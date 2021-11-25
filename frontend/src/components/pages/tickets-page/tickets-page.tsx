import { Component, Host, h, State, Watch } from '@stencil/core';
import { globals } from '../../../core/global.store';

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
        <tickets-table onlyShowCreate={false}></tickets-table> 

        
       
      </Host>
    );
  }

}

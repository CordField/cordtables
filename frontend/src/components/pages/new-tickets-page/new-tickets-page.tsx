import { Component, Host, h, State, Listen, Watch } from '@stencil/core';
import { globals } from '../../../core/global.store';
import { ErrorType, GenericResponse } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import '@ionic/core';
import { format, parseISO } from 'date-fns';


class CommonTicketsPageListRequest{
  token: string;
  limit: number;
  offset: number;
}

class CommonPagesNumberRequest{
    token: string;
    wordToSearch?: string;
}

class PagesNumberRequestPeople{
    token: string;
    wordToSearch?: string;
}

class TicketsPeopleRequest{
    wordToSearch?: string;
    token: string;
    limit: number;
    offset: number;
}

class CommonTicketsIdTitleRequest{
    wordToSearch?: string;
    token: string;
    limit: number;
    offset: number;
}

class AssignTicketRequest{
    token: string;
    ticket_assignment: {
        ticket: string;
        person: string;
    }
}

class AssignTicketUpdateRequest{
  token: string;
  id: string;
  ticket: string;
  person: string;
  
}

class AssignTicketUpdateResponse{
  error: ErrorType;
  id: string;
}

class ReadTicketAssignmentRequest{
  token: string;
  ticket: string;
}

class AssignTicketResponse{
    error: ErrorType;
    id: string;
}

class ReadTicketAssignmentResponse{
  error: ErrorType;
  ticket_assignment: {
    id: string;
    ticket: string,
    person: string,
  }
}

class CreateTicketRequest{
    token: string;
    ticket:{
      title: string;
      ticket_status: string;
      assigned_to?: string;
      parent: string;
      content : string;
    }
  }

class UpdateTicketRequest {
  token: string;
  id: string;
  ticket:{
    title: string;
    ticket_status: string;
    parent: string;
    content : string;
  }
}

class updateWorkEstimateRequest {
  token: string;
  id: string;
  ticket: string;
  hours?: number;
  minutes?: number;
}

class updateWorkRecordRequest {
  token: string;
  id: string;
  ticket: string;
  hours?: number;
  minutes?: number;
}

class updateWorkRecordResponse {
  erro: ErrorType;
}

class createWorkEstimateRequest {
  token: string;
  work_estimate: {
    ticket: string;
    hours: number;
    minutes: number;
    comment?: string;
  };
}

class createWorkRecordRequest {
  token: string;
  work_record: {
    person?: string;
    ticket: string;
    hours: number;
    minutes: number;
    comment?: string;
  };
}

class createWorkRecordResponse {
  error: ErrorType;
  work_record: {
    id?: number;
    ticket?: string;
    person: string;
    hours: number;
    minutes: number;
    total_time: number;
    created_at: string;
    created_by: number;
  };
}

class createWorkEstimateResponse {
  error: ErrorType;
  work_estimate: {
    id?: string;
    ticket?: string;
    person: string;
    hours: number;
    minutes: number;
    total_time: number;
    created_at: string;
    created_by: number;
  };
}

class deleteWorkEstimateRequest {
  token: string;
  id: string;
}

class deleteWorkEstimateResponse {
  error: ErrorType;
  id: string;
}

class deleteWorkRecordRequest {
  token: string;
  id: string;
}

class deleteWorkRecordResponse {
  error: ErrorType;
  id: string;
}

class ReadTicketRequest {
  token: string;
  id: string;
}

class ReadWorkEstimatesRequest {
  token: string;
  id: string;
}

class ListWorkEstimatesRequest {
  token: string;
  ticket: string;
}

class ListWorkRecordsRequest {
  token: string;
  ticket: string;
}

  interface User {
    id: string;
    first_name: string;
    last_name: string;
  }

class CreateTicketResponse extends GenericResponse {
  error: ErrorType;
  ticket: CommonTicketsRow;
}

class UpdateTicketResponse extends GenericResponse {
  error: ErrorType;
  ticket: CommonTicketsRow;
}


class ReadTicketResponse extends GenericResponse {
  error: ErrorType;
  ticket: CommonTicketsRow;
}

class ReadWorkEstimatesResponse extends GenericResponse {
  error: ErrorType;
  work_estimate: CommonWorkEstimatesRow;
}

class ListWorkEstimatesResponse extends GenericResponse {
  error: ErrorType;
  work_estimate?: CommonWorkEstimatesRow[];
}

class ListWorkRecordsResponse extends GenericResponse {
  error: ErrorType;
  work_record?: CommonWorkRecordsRow[];
}

class ListTicketResponse extends GenericResponse {
  error: ErrorType;
  tickets: CommonTicketsRow[];
}

class TicketsIdAndTitlesResponse extends GenericResponse {
    error: ErrorType;
    tickets: CommonTicketIdTitleRow[];
}

class PeopleIdNamesResponse extends GenericResponse {
    error: ErrorType;
    people: PeopleIdNameRow[];
}

class TotalTicketsResponse extends GenericResponse{
    error: ErrorType;
    total_tickets: CommonTotalTickets[] 
}

class TotalPeopleResponse extends GenericResponse{
    error: ErrorType;
    total_people: PeopleTotal[] 
}

class CommonTotalTickets {
    total: number;
}

class PeopleTotal {
    total: number;
}

class CommonTicketIdTitleRow {
    id: string;
    title: string;
}

class PeopleIdNameRow {
    id: string;
    name: string;
}

class CommonTicketsRow {
    id : string;
    title: string;
    ticket_status : string;
    parent : string;
    content : string;
    created_at: string;
    created_by: number;
    modified_at: string;
    modified_by: number;
    owning_person: number;
    owning_group: number;
}

class CommonWorkEstimatesRow{
  id?: string;
  ticket?: string = null;
  person: string;
  public_full_name?: string = null;
  hours: number;
  minutes: number;
  total_time: number;
  created_at: string;
  created_by: number;
}

class CommonWorkRecordsRow {
  id?: string;
  ticket?: string;
  person: string;
  public_full_name?: string = null;
  hours: number;
  minutes: number;
  total_time: number;
  created_at: string;
  created_by: number;
}



@Component({
  tag: 'new-tickets-page',
  styleUrl: 'new-tickets-page.css',
  shadow: true,
})
export class NewTicketsPage {




  @State() isOpen: boolean;
  @State() isCreateNewTicketVisible: boolean = false;
  @State() isAddEstimateToTicketVisible: boolean = false;
  @State() isAssignATicketVisible: boolean = false;
  @State() isBlockATicketVisible: boolean = false;
  @State() isAddAWorkRecordVisible: boolean = false;
  @State() isAddFeedBacktoTicketVisible: boolean = false;
  @State() commonTicketsPageResponse: ListTicketResponse;
  @State() CommonTicketsReadResponse : ReadTicketResponse;
  @State() CommonWorkEstimatesListResponse: ListWorkEstimatesResponse;
  @State() CommonWorkRecordsListResponse: ListWorkRecordsResponse;
  @State() ticketsIdAndTitleResponse:  TicketsIdAndTitlesResponse;
  @State() ticketsPeopleNameResponse: PeopleIdNamesResponse;
  @State() users: User[] = [];
  @State() pages: TotalTicketsResponse;
  @State() pagesPeople: TotalPeopleResponse;
  @State() paginationPages : number;
  @State() page: number = 1;
  @State() limit: number = 5;
  @State() offset: number = 0;
  @State() workEstimateId?: string = null;
  @State() newHoursWorkEstimates?: number = null;
  @State() newMinutesWorkEstimates?: number = null;
  @State() workRecordId?: string = null;
  @State() newHoursWorkRecords?: number = null;
  @State() newMinutesWorkRecords?: number = null;
  @State() peoplePaginationPages : number;
  @State() mainTicketsPaginationPages: number;
  @State() peoplePage: number = 1;
  @State() mainTicketsPage: number = 1;
  @State() peopleLimit: number = 5;
  @State() peopleOffset: number = 0;
  @State() mainTicketsOffset: number = 0;
  @State() showUpdateWorkEstimates : boolean = false;
  newTicketStatusName: string;
  newParent?: string = null;
  newAssignee: string;
  newContent: string;
  @State() newTicketTitle: string;
  newTicketContent: string; 
  wordToSearch: string;
  wordToSearchPeople : string;
  modalType: string;
  modalTitle: string;
  ticketId: string;
  
  
  async componentWillLoad() {
    await this.getList();
    await this.getListIdTitles();
    await this.getNamesPeople();
    await this.getTotalTickets();
    await this.getTotalPeople();
    this._fetchTickets(1);
    this._fetchPeople(1);
    this._fetchMainTickets(1);
    
  }
  
  async getList() {
    this.commonTicketsPageResponse = await fetchAs<CommonTicketsPageListRequest, ListTicketResponse>('common/tickets/list', {
      token: globals.globalStore.state.token,
      limit: this.limit,
      offset: this.mainTicketsOffset
    });
  }

  async readTicket(ticket_id) {
    this.CommonTicketsReadResponse = await fetchAs<ReadTicketRequest, ReadTicketResponse>('common/tickets/read', {
      token: globals.globalStore.state.token,
      id: ticket_id
    });
    if(this.CommonTicketsReadResponse){
      this.newTicketTitle = this.CommonTicketsReadResponse.ticket.title;
      this.newTicketStatusName = this.CommonTicketsReadResponse.ticket.ticket_status;
      this.newTicketContent = this.CommonTicketsReadResponse.ticket.content;
      this.isOpen = !this.isOpen;
      this.CommonTicketsReadResponse.ticket.parent ? this.parentChange(this.CommonTicketsReadResponse.ticket.parent) : this.parentChange(null)

    }
  }

  async listWorkEstimates(ticket_id) {
    this.CommonWorkEstimatesListResponse = await fetchAs<ListWorkEstimatesRequest, ListWorkEstimatesResponse>('common/work-estimates/list', {
      token: globals.globalStore.state.token,
      ticket: ticket_id ? ticket_id : this.ticketId
    });
  }

  async listWorkRecords(ticket_id) {
    this.CommonWorkRecordsListResponse = await fetchAs<ListWorkRecordsRequest, ListWorkEstimatesResponse>('common/work-records/list', {
      token: globals.globalStore.state.token,
      ticket: ticket_id ? ticket_id : this.ticketId
    });
  }

  async getListIdTitles(){
      this.ticketsIdAndTitleResponse = await fetchAs<CommonTicketsIdTitleRequest, TicketsIdAndTitlesResponse> ('common/tickets/list-id-and-title', {
        token: globals.globalStore.state.token,
        wordToSearch: this.wordToSearch,
        limit: this.limit,
        offset: this.offset
      });
  }

  async getNamesPeople(){
    this.ticketsPeopleNameResponse = await fetchAs<TicketsPeopleRequest, PeopleIdNamesResponse> ('common/tickets/list-people-names', {
      token: globals.globalStore.state.token,
      wordToSearch: this.wordToSearchPeople,
      limit: this.peopleLimit,
      offset: this.peopleOffset
    });
}

  async getTotalTickets(){
    let totalTickets = await fetchAs<CommonPagesNumberRequest, TotalTicketsResponse> ('common/tickets/count-tickets', {
      token: globals.globalStore.state.token,
      wordToSearch: this.wordToSearch
    });
    this.paginationPages = Math.ceil(totalTickets.total_tickets[0].total/5);
    this.mainTicketsPaginationPages = Math.ceil(totalTickets.total_tickets[0].total/5);
}


async getTotalPeople(){
    let totalPeople = await fetchAs<PagesNumberRequestPeople, TotalPeopleResponse> ('common/tickets/count-people-tickets', {
      token: globals.globalStore.state.token,
      wordToSearch: this.wordToSearchPeople
    });
    this.peoplePaginationPages = Math.ceil(totalPeople.total_people[0].total/5);
}

   _fetchTickets (page) {
    this.offset = ((page*5) - 5); 
    this.getListIdTitles();
  }

  _fetchPeople (page) {
    this.peopleOffset = ((page*5) - 5); 
    this.getNamesPeople();
  }

  _fetchMainTickets (page) {
    this.mainTicketsOffset = ((page*5) - 5); 
    this.getList();
  }


  createNewTicket = async (event: MouseEvent) => {
    event.preventDefault(); 
    event.stopPropagation();
    
    
    const result = await fetchAs<CreateTicketRequest, CreateTicketResponse>('common/tickets/create-read', {
      token: globals.globalStore.state.token,
      ticket: {
        title: this.newTicketTitle,
        ticket_status: 'Open',
        parent: this.newParent,
        content: this.newTicketContent,

      },
    });

    if (result.ticket.id && this.newAssignee){
        await this.assignTicketToPerson(result.ticket.id, this.newAssignee);
    }

    await this.getList();
  
    /*
    if (result.error === ErrorType.NoError) {
      globals.globalStore.state.editMode = false;
      this.getList();
    }
    */
  };

  createNewWorkEstimate = async (event: MouseEvent) => {
    event.preventDefault(); 
    event.stopPropagation();
    
    
    const result = await fetchAs<createWorkEstimateRequest, createWorkEstimateResponse>('common/work-estimates/create-read', {
      token: globals.globalStore.state.token,
      work_estimate: {
        ticket: this.ticketId,
        hours: this.newHoursWorkEstimates,
        minutes: this.newMinutesWorkEstimates

      }
    });

    await this.listWorkEstimates(this.ticketId);
  
  };
qq

  createNewWorkRecord = async (event: MouseEvent) => {
    event.preventDefault(); 
    event.stopPropagation();
    
    
    const result = await fetchAs<createWorkRecordRequest, createWorkRecordResponse>('common/work-records/create-read', {
      token: globals.globalStore.state.token,
      work_record: {
        ticket: this.ticketId,
        hours: this.newHoursWorkRecords,
        minutes: this.newMinutesWorkRecords

      }
    });

    await this.listWorkRecords(this.ticketId);
  
  };

  

  updateTicket = async (event: MouseEvent) => {
    event.preventDefault(); 
    event.stopPropagation();
    
    
    const result = await fetchAs<UpdateTicketRequest, UpdateTicketResponse>('common/tickets/update-read', {
      token: globals.globalStore.state.token,
      id: this.ticketId,
      ticket: {
        title: this.newTicketTitle,
        ticket_status: this.newTicketStatusName,
        parent: this.newParent,
        content: this.newTicketContent,

      },
    });

    if (result.ticket.id && this.newAssignee){
        await this.assignTicketToPerson(result.ticket.id, this.newAssignee);
    }

    await this.getList();
  
  };


  assignTicketToPerson = async (ticketId, personId) =>{
    const idTicketAlreadyAssigned = await this.readTicketAssignment(ticketId);
    if (idTicketAlreadyAssigned) {
      await fetchAs<AssignTicketUpdateRequest, AssignTicketResponse>('common/ticket-assignments/update-read', {
        token: globals.globalStore.state.token,
        id: idTicketAlreadyAssigned,
        ticket: ticketId,
        person: personId
        });

    } else {
      await fetchAs<AssignTicketRequest, AssignTicketResponse>('common/ticket-assignments/create-read', {
      token: globals.globalStore.state.token,
      ticket_assignment: {
        ticket: ticketId,
        person: personId
        },
      });
    }
  }

  readTicketAssignment = async (ticketId) => {
    const result = await fetchAs<ReadTicketAssignmentRequest, ReadTicketAssignmentResponse>('common/ticket-assignments/read', {
      token: globals.globalStore.state.token,
      ticket: ticketId
    });
    return result.ticket_assignment && result.ticket_assignment [0] ? result.ticket_assignment[0].id : null;
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

  onInsertNew() {
    this.modalType = 'Create';
    this.modalTitle = 'Create New Ticket'
    this.isOpen = !this.isOpen;
    this.workEstimateId = null;
    this.workRecordId = null;
    this.newHoursWorkEstimates = null;
    this.newMinutesWorkEstimates = null;
    this.newHoursWorkRecords = null;
    this.newMinutesWorkRecords = null;
    this.CommonWorkEstimatesListResponse = {
      error: ErrorType.NoError,
      work_estimate: []
    }
    this.CommonWorkRecordsListResponse = {
      error: ErrorType.NoError,
      work_record: []
    }
    this.newParent = null;
  }

  async onUpdateTicket(ticket_id) {
    this.modalType = 'Update';
    this.modalTitle = 'Update Ticket'
    this.ticketId = ticket_id;
    await this.readTicket(ticket_id);
    this.workEstimateId = null;
    this.workRecordId = null;
    this.newHoursWorkEstimates = null;
    this.newMinutesWorkEstimates = null;
    this.newHoursWorkRecords = null;
    this.newMinutesWorkRecords = null;

    await this.listWorkEstimates(ticket_id);
    await this.listWorkRecords(ticket_id);
  }


  async onClickUpdateWorkEstimates(id, hours, minutes) {
    console.log('id: ', id);
    this.workEstimateId = id;
    console.log('new id: ', this.workEstimateId);
    this.newHoursWorkEstimates = hours;
    this.newMinutesWorkEstimates = minutes;
  }

  async onClickUpdateWorkRecords(id, hours, minutes) {
    this.workRecordId = id;
    this.newHoursWorkRecords = hours;
    this.newMinutesWorkRecords = minutes;
  }

  @Watch('workEstimateId')
  watchStateHandler(newValue: string, oldValue: string) {
    this.workEstimateId = newValue;
  }

  @Watch('workRecordId')
  watchStateRecordHandler(newValue: string, oldValue: string) {
    this.workRecordId = newValue;
  }


  onClickSubmitUpdateWorkEstimates = async (event: MouseEvent) => {
    event.preventDefault(); 
    event.stopPropagation();
    console.log('id Submit: ', this.workEstimateId);
    console.log('id Ticket: ', this.ticketId);
    
    const result = await fetchAs<updateWorkEstimateRequest, ReadWorkEstimatesResponse>('common/work-estimates/update-read', {
      token: globals.globalStore.state.token,
      id: this.workEstimateId,
      ticket: this.ticketId,
      hours: this.newHoursWorkEstimates,
      minutes: this.newMinutesWorkEstimates

    });

    await this.listWorkEstimates(this.ticketId);
  
  };

  onClickSubmitDeleteWorkEstimates = async (estimate_id, event: MouseEvent) => {

    this.workEstimateId = estimate_id;
    event.preventDefault(); 
    event.stopPropagation();
    
    const result = await fetchAs<deleteWorkEstimateRequest, deleteWorkEstimateResponse>('common/work-estimates/delete', {
      token: globals.globalStore.state.token,
      id: this.workEstimateId,
    });

    await this.listWorkEstimates(this.ticketId);
  
  };


  onClickSubmitDeleteWorkRecords = async (record_id, event: MouseEvent) => {

    this.workRecordId = record_id;
    event.preventDefault(); 
    event.stopPropagation();
    
    const result = await fetchAs<deleteWorkRecordRequest, deleteWorkRecordResponse>('common/work-records/delete', {
      token: globals.globalStore.state.token,
      id: this.workRecordId,
    });

    await this.listWorkRecords(this.ticketId);
  
  };


  onClickSubmitUpdateWorkRecords = async (event: MouseEvent) => {
    event.preventDefault(); 
    event.stopPropagation();
    
    const result = await fetchAs<updateWorkRecordRequest, updateWorkRecordResponse>('common/work-records/update-read', {
      token: globals.globalStore.state.token,
      id: this.workRecordId,
      ticket: this.ticketId,
      hours: this.newHoursWorkRecords,
      minutes: this.newMinutesWorkRecords

    });
    await this.listWorkRecords(this.ticketId);
  
  };

  ticketTitleChange(event) {
      this.newTicketTitle = event.target.value;
  }

  ticketStatusNameChange(event) {
    this.newTicketStatusName = event.target.value;
  }
  
  parentChange(event) {
    this.newParent = null;
    if(event){
      if(event.target && event.target.value){
        this.newParent = event.target.value;
      } else {
        this.newParent = event;
      }
    }
  }
  
  assigneeChange(event) {
    this.newAssignee = event.target.value;
  }

  ticketStatusChange(event) {
    this.newTicketStatusName = event.target.value;
  }
  
  ticketContentChange(event) {
    this.newTicketContent = event.target.value;
  }

  wordToSearchChange(event){
      this.wordToSearch = event.target.value;
  }

  wordToSearchChangePeople(event){
    this.wordToSearchPeople = event.target.value;
}

  onPressSearchButton(event){
      this.limit = 5;
      this.page = 1;
      this.offset = 0;
      this.getTotalTickets();
      this._fetchTickets(this.page);
  }

  onPressSearchButtonPeople(event){
    this.peopleLimit = 5;
    this.peoplePage = 1;
    this.peopleOffset = 0;
    this.getTotalPeople();
    this._fetchPeople(this.peoplePage);
}


  pagination () {
    let totalPages = this.paginationPages;
    let pages = [];
    for(let i = 1; i <= totalPages; i ++) {
      if(i === this.page) {
        pages.push(<li class="active">{i}</li>)
      } else {
        pages.push(<li onClick={() => this.changePage(i)}>{i}</li>)
      }
    }
    return pages;
  }

  paginationPeople () {
    let totalPages = this.peoplePaginationPages;
    let pages = [];
    for(let i = 1; i <= totalPages; i ++) {
      if(i === this.peoplePage) {
        pages.push(<li class="active">{i}</li>)
      } else {
        pages.push(<li onClick={() => this.changePeoplePage(i)}>{i}</li>)
      }
    }
    return pages;
  }

  paginationMainTickets () {
    let totalPages = this.mainTicketsPaginationPages;
    let pages = [];
    for(let i = 1; i <= totalPages; i ++) {
      if(i === this.mainTicketsPage) {
        pages.push(<li class="active">{i}</li>)
      } else {
        pages.push(<li onClick={() => this.changePageMainTickets(i)}>{i}</li>)
      }
    }
    return pages;
  }


  changePage (page) {
    this._fetchTickets(page);
    this.page = page;
  }

  workEstimateHourChange (event) {
    this.newHoursWorkEstimates = event.target.value;
  }

  workEstimateMinuteChange (event) {
    this.newMinutesWorkEstimates = event.target.value;
  }

  workRecordHourChange (event) {
    this.newHoursWorkRecords = event.target.value;
  }

  workRecordMinuteChange (event) {
    this.newMinutesWorkRecords = event.target.value;
  }

  changePeoplePage (page) {
    this._fetchPeople(page);
    this.peoplePage = page;
  }

  changePageMainTickets (page) {
    this._fetchMainTickets(page);
    this.mainTicketsPage = page;
  }


  @Listen('modalOkay')
  async handleModalOkay(event){
    if(event && event.detail){
        if(this.modalType === 'Create'){
          await this.createNewTicket(event);
        } else {
          await this.updateTicket(event);
        }
        
        this.page = 1;
        this.peoplePage = 1;
        this.limit = 5;
        this.peopleLimit = 5;
        this.offset = 0;
        this.peopleOffset = 0;
        this.isOpen = !this.isOpen;
        this.newTicketTitle = "";
        this.newTicketContent = "";
        this.wordToSearch = '';
        this.wordToSearchPeople = '';
        await this.getList();
    }
  }

  @Listen('modalClosed')
  handleModalClose(event) {
    if (event && event.detail) {
      this.page = 1;
      this.peoplePage = 1;
      this.limit = 5;
      this.peopleLimit = 5;
      this.offset = 0;
      this.peopleOffset = 0;
      this.newTicketTitle = "";
      this.newTicketContent = "";
      this.isOpen = !this.isOpen;
      this.wordToSearch = '';  
      this.wordToSearchPeople = ''; 
    }
  }

  render() {
    return (
      <Host>
        <div class="mainPage">
         <ticket-modal 
          isOpen={this.isOpen} 
          type={this.modalType}
          modalTitle={this.modalTitle}>
            <div class="general-container">
              <div class="container">
                <div class="mainContainer">
                  <label> Status </label>
                  <select name="ticket-status" id="ticket-status" onChange={event => this.ticketStatusChange(event)}>
                    <option value="Open" selected={this.newTicketStatusName === "Open"}> Open </option>
                    <option value="Blocked" selected={this.newTicketStatusName === "Blocked"}> Blocked </option>
                    <option value="Closed" selected={this.newTicketStatusName === "Closed"}> Closed </option>
                  </select>
                  <label> Title: </label>
                  <div class="main-input">
                    <ion-item>
                      <ion-input clearInput value={this.newTicketTitle} onInput={event => this.ticketTitleChange(event)}></ion-input>
                    </ion-item><br/>
                  </div>
                  <label> Content </label>
                <div class="main-input">
                  <ion-item>
                    <ion-textarea value={this.newTicketContent} onInput={event => this.ticketContentChange(event)}></ion-textarea>
                  </ion-item><br/>
                </div>
                <div class="parentContainer">
                  <label>Ticket Parent</label>
                  <div class="topnav">
                    <div class="search-container">
                      <input type="text" placeholder="Search by title..." name="search" onInput={event => this.wordToSearchChange(event)}/>
                      <button type="submit" onClick={event => this.onPressSearchButton(event)}><i class="fa fa-search"></i><ion-icon name="search-outline"></ion-icon></button>
                    </div>
                  </div>
                  <ion-list>
                    <ion-radio-group value={this.newParent}>
                      {this.ticketsIdAndTitleResponse.tickets.map(ticket => (
                        <ion-item>
                          <ion-label>{ticket.title}</ion-label>
                          <ion-radio slot="start" onClick={event => this.parentChange(event)} value={ticket.id}></ion-radio>
                        </ion-item>
                      ))}
                    </ion-radio-group>
                  </ion-list>
                  <ul class="fetch-pagination">
                    {this.pagination()}
                  </ul>
                </div>
              <br/>
            </div>
            <div class="sideContainer">
            <label> Assign to: </label>
              <div class="topnav">
                <div class="search-container">
                  <input type="text" placeholder="Search by name..." name="search" onInput={event => this.wordToSearchChangePeople(event)}/>
                  <button type="submit" onClick={event => this.onPressSearchButtonPeople(event)}><i class="fa fa-search"></i><ion-icon name="search-outline"></ion-icon></button>
                </div>
              </div>
              <ion-list>
                <ion-radio-group value={this.newAssignee}>
                  {this.ticketsPeopleNameResponse.people.map(people => (
                    <ion-item>
                      <ion-label>{people.name}</ion-label>
                      <ion-radio slot="start"  onClick={event => this.assigneeChange(event)} value={people.id}></ion-radio>
                    </ion-item>
                  ))}
                </ion-radio-group>
              </ion-list>    
              <ul class="fetch-pagination">
                {this.paginationPeople()}
              </ul>
              </div>
            </div>
            <div class="ticket-estimates-work-container">
              <div class="ticket-estimates-container">
                <div class="generalTickets">
                  <div class="work-estimate-update-container">
                    <div>
                      <h2> Work Estimates </h2>
                      <span>
                        <label>Hours:</label>
                          <input type="number" id="work-estimates-hour-input" name="work-estimates-hour-input"
                          min="0" max="100" value={this.newHoursWorkEstimates} onInput={event => this.workEstimateHourChange(event)}/>
                      </span>
                      <span>
                        <label>Minutes:</label>
                        <input type="number" id="work-estimates-hour-input" name="work-estimates-hour-input"
                        min="0" max="59" value={this.newMinutesWorkEstimates} onInput={event => this.workEstimateMinuteChange(event)}/>
                      </span>
                      <button onClick={event => this.onClickSubmitUpdateWorkEstimates(event)}> Submit </button>
                      <button onClick={event => this.createNewWorkEstimate(event)}> Add </button>
                    </div>
                  </div>
                {this.CommonWorkEstimatesListResponse ? this.CommonWorkEstimatesListResponse.work_estimate.map(estimate =>(
                  <div class="main-estimates-container">  
                    <ion-grid>
                      <ion-row>
                        <ion-col>
                          <ion-card button={true} >
                            <ion-card-header>
                              <div class="fallbackTimePicker">
                                <div>
                                  <span>
                                    <label>Hours:</label>
                                    <input type="number" id="work-estimates-hour-input" name="work-estimates-hour-input"
                                    min="0" max="100"value={estimate.hours} disabled/>
                                  </span>
                                  <span>
                                    <label>Minutes:</label>
                                    <input type="number" id="work-estimates-hour-input" name="work-estimates-hour-input"
                                    min="0" max="59" value={estimate.minutes} disabled/>
                                  </span>
                                </div>
                              </div>
                            </ion-card-header>
                            <ion-card-content>
                              <span> Creator: </span>
                                {estimate.public_full_name}<br/><br/>
                              <button onClick={() => this.onClickUpdateWorkEstimates(estimate.id, estimate.hours, estimate.minutes)}> Update </button>
                              <button onClick={event => this.onClickSubmitDeleteWorkEstimates(estimate.id, event)}> Delete </button>
                            </ion-card-content>
                        </ion-card>
                      </ion-col>
                    </ion-row>
                  </ion-grid>
                </div>
              )) : '' }
            </div>
          </div>
          <div class="ticket-work-hours-container">
            <div class="generalTickets">
              <div class="work-estimate-update-container">
              <div>
              <h2> Work Hours </h2>
              <span>
                <label>Hours:</label>
                <input type="number" id="work-records-hour-input" name="work-records-hour-input"
                min="0" max="100" value={this.newHoursWorkRecords} onInput={event => this.workRecordHourChange(event)}/>
              </span>
              <span>
                <label>Minutes:</label>
                <input type="number" id="work-records-hour-input" name="work-records-hour-input"
                min="0" max="59" value={this.newMinutesWorkRecords} onInput={event => this.workRecordMinuteChange(event)}/>
              </span>
              <button onClick={event => this.onClickSubmitUpdateWorkRecords(event)}> Submit </button>
              <button onClick={event => this.createNewWorkRecord(event)}> Add </button>
            </div>
          </div>
          {this.CommonWorkRecordsListResponse ? this.CommonWorkRecordsListResponse.work_record.map(record =>(
            <div class="main-estimates-container">
              <ion-grid>
                <ion-row>
                  <ion-col>
                    <ion-card button={true} >
                      <ion-card-header>
                        <div class="fallbackTimePicker">
                        <div>
                        <span>
                          <label>Hours:</label>
                          <input type="number" id="work-records-hour-input" name="work-records-hour-input"
                          min="0" max="100"value={record.hours} disabled/>
                        </span>
                        <span>
                          <label>Minutes:</label>
                          <input type="number" id="work-records-hour-input" name="work-records-hour-input"
                          min="0" max="59" value={record.minutes} disabled/>
                        </span>
            </div>
          </div>
                      </ion-card-header>
                      <ion-card-content>
                        <span> Creator: </span>
                        {record.public_full_name}<br/><br/>
                        <button onClick={() => this.onClickUpdateWorkRecords(record.id, record.hours, record.minutes)}> Update </button>
                        <button onClick={event => this.onClickSubmitDeleteWorkRecords(record.id, event)}> Delete </button>
                      </ion-card-content>
                  </ion-card>
                </ion-col>
              </ion-row>
            </ion-grid>
          </div>
        )) : '' }
 
         </div>
              </div>

          </div>
             </div>
             
           
      </ticket-modal>
        <div>
          <button onClick={() => this.onInsertNew()} class="create-new-button"> Create New Ticket </button>
          <div class="generalTickets">
            {this.commonTicketsPageResponse.tickets.map(ticket =>(
              <div class="mainTicketContainer">
                <ion-grid>
                  <ion-row>
                    <ion-col>
                      <ion-card button={true} onClick={() => this.onUpdateTicket(ticket.id)}>
                        <ion-card-header>
                          <ion-card-subtitle>{ticket.ticket_status}</ion-card-subtitle>
                          <ion-card-title>{ticket.title}</ion-card-title>
                        </ion-card-header>
                        <ion-card-content>
                          {ticket.content}
                        </ion-card-content>
                      </ion-card>
                    </ion-col>
                  </ion-row>
                </ion-grid>
              </div>
            ))}
          <ul class="fetch-pagination">
            {this.paginationMainTickets()}
          </ul>
          </div> 
        </div>
      </div>
      </Host>
    );
  }
}

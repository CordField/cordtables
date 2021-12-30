import { Component, Host, h, State, Listen, Watch } from '@stencil/core';
import { globals } from '../../../core/global.store';
import { ErrorType, GenericResponse } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import '@ionic/core'


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
        ticket: number;
        person: number;
    }
}

class AssignTicketResponse{
    error: ErrorType;
    id: number;
}

class CreateTicketRequest{
    token: string;
    ticket:{
      title: string;
      ticket_status: string;
      assigned_to?: number;
      parent: number;
      content : string;
    }
  }

class UpdateTicketRequest {
  token: string;
  id: number;
  ticket:{
    title: string;
    ticket_status: string;
    parent: number;
    content : string;
  }
}

class ReadTicketRequest {
  token: string;
  id: number;
}

  interface User {
    id: number;
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
    id: number;
    title: string;
}

class PeopleIdNameRow {
    id: number;
    name: string;
}

class CommonTicketsRow{
    id : number;
    title: string;
    ticket_status : string;
    parent : number;
    content : string;
    created_at: string;
    created_by: number;
    modified_at: string;
    modified_by: number;
    owning_person: number;
    owning_group: number;
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
  @State() ticketsIdAndTitleResponse:  TicketsIdAndTitlesResponse;
  @State() ticketsPeopleNameResponse: PeopleIdNamesResponse;
  @State() users: User[] = [];
  @State() pages: TotalTicketsResponse;
  @State() pagesPeople: TotalPeopleResponse;
  @State() paginationPages : number;
  @State() page: number = 1;
  @State() limit: number = 5;
  @State() offset: number = 0;
  @State() peoplePaginationPages : number;
  @State() mainTicketsPaginationPages: number;
  @State() peoplePage: number = 1;
  @State() mainTicketsPage: number = 1;
  @State() peopleLimit: number = 5;
  @State() peopleOffset: number = 0;
  @State() mainTicketsOffset: number = 0;
  newTicketStatusName: string;
  newParent: number;
  newAssignee: number;
  newContent: string;
  @State() newTicketTitle: string;
  newTicketContent: string; 
  wordToSearch: string;
  wordToSearchPeople : string;
  modalType: string;
  modalTitle: string;
  ticketId: number;

  
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
    this.commonTicketsPageResponse = await fetchAs<CommonTicketsPageListRequest, ListTicketResponse>('common-tickets/list', {
      token: globals.globalStore.state.token,
      limit: this.limit,
      offset: this.mainTicketsOffset
    });
  }

  async readTicket(ticket_id) {
    this.CommonTicketsReadResponse = await fetchAs<ReadTicketRequest, ReadTicketResponse>('common-tickets/read', {
      token: globals.globalStore.state.token,
      id: ticket_id
    });
    if(this.CommonTicketsReadResponse){
      this.newTicketTitle = this.CommonTicketsReadResponse.ticket.title;
      this.newTicketStatusName = this.CommonTicketsReadResponse.ticket.ticket_status;
      this.newTicketContent = this.CommonTicketsReadResponse.ticket.content;
      this.isOpen = !this.isOpen;
      if (this.CommonTicketsReadResponse.ticket.parent)this.parentChange(this.CommonTicketsReadResponse.ticket.parent)

    }
  }

  async getListIdTitles(){
      this.ticketsIdAndTitleResponse = await fetchAs<CommonTicketsIdTitleRequest, TicketsIdAndTitlesResponse> ('common-tickets/list-id-and-title', {
        token: globals.globalStore.state.token,
        wordToSearch: this.wordToSearch,
        limit: this.limit,
        offset: this.offset
      });
  }

  async getNamesPeople(){
    this.ticketsPeopleNameResponse = await fetchAs<TicketsPeopleRequest, PeopleIdNamesResponse> ('common-tickets/list-people-names', {
      token: globals.globalStore.state.token,
      wordToSearch: this.wordToSearchPeople,
      limit: this.peopleLimit,
      offset: this.peopleOffset
    });
}

  async getTotalTickets(){
    let totalTickets = await fetchAs<CommonPagesNumberRequest, TotalTicketsResponse> ('common-tickets/count-tickets', {
      token: globals.globalStore.state.token,
      wordToSearch: this.wordToSearch
    });
    this.paginationPages = Math.ceil(totalTickets.total_tickets[0].total/5);
    this.mainTicketsPaginationPages = Math.ceil(totalTickets.total_tickets[0].total/5);
}


async getTotalPeople(){
    let totalPeople = await fetchAs<PagesNumberRequestPeople, TotalPeopleResponse> ('common-tickets/count-people-tickets', {
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
    
    
    const result = await fetchAs<CreateTicketRequest, CreateTicketResponse>('common-tickets/create-read', {
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

  updateTicket = async (event: MouseEvent) => {
    event.preventDefault(); 
    event.stopPropagation();
    
    
    const result = await fetchAs<UpdateTicketRequest, UpdateTicketResponse>('common-tickets/update-read', {
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
    
    console.log("UPDATE: ", result);
    await this.getList();
  
  };


  assignTicketToPerson = async (ticketId, personId) =>{
    const result = await fetchAs<AssignTicketRequest, AssignTicketResponse>('common-ticket-assignments/create-read', {
        token: globals.globalStore.state.token,
        ticket_assignment: {
          ticket: ticketId,
          person: personId
  
        },
      });
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
  }

  async onUpdateTicket(ticket_id) {
    this.modalType = 'Update';
    this.modalTitle = 'Update Ticket'
    this.ticketId = ticket_id;
    await this.readTicket(ticket_id);
  }

  ticketTitleChange(event) {
      this.newTicketTitle = event.target.value;
  }

  ticketStatusNameChange(event) {
    this.newTicketStatusName = event.target.value;
  }
  
  parentChange(event) {
    if(event.target && event.target.value){
      this.newParent = event.target.value;
    } else {
      this.newParent = event;
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
             <div class="container">
            <div class="mainContainer">
                <label> Status </label>
                <select name="ticket-status" id="ticket-status" onChange={event => this.ticketStatusChange(event)}>
                  <option value="Open" selected={this.newTicketStatusName === "Open"}> Open </option>
                  <option value="Blocked" selected={this.newTicketStatusName === "Blocked"}> Blocked </option>
                  <option value="Closed" selected={this.newTicketStatusName === "Closed"}> Closed </option>
                </select>
                <label> Title: </label>
              <ion-item>
                  <ion-input clearInput value={this.newTicketTitle} onInput={event => this.ticketTitleChange(event)}></ion-input>
              </ion-item><br/>
                <label> Content </label>
              <ion-item>
                  <ion-textarea value={this.newTicketContent} onInput={event => this.ticketContentChange(event)}></ion-textarea>
              </ion-item><br/>
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
            
         </ticket-modal>
         <div>
         <button onClick={() => this.onInsertNew()} class="create-new-button"> Create New Ticket </button>
         <div id="generalTickets">
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

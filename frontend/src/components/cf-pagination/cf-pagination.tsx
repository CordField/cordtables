import { Component, Host, h, State, Prop, Method,  Event, EventEmitter } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';


@Component({
  tag: 'cf-pagination',
  styleUrl: 'cf-pagination.css',
  shadow: true,
})

export class CfPagination {
  @Prop() history: RouterHistory;
  @Prop() currentPage: number;
  @Prop() totalRows: number;
  @Prop() resultsPerPage: number = 50;
  @Prop() pageUrl: string;
  @State() showMenu = false;

  @Event({ bubbles: true, composed: true }) pageChanged: EventEmitter<number>;

  currentPageUrl = window.location.href
  start: number =1;
  pages = [];


  async goToPage(page ){
    this.currentPage = page;
    this.history.replace(this.pageUrl+"?page="+page);
    this.pageChanged.emit(page);
  }

  render() {
    var totalPages = Math.ceil(this.totalRows/this.resultsPerPage);
    this.pages =  [...Array(totalPages - this.start + 1).keys()]

    return (
      <Host>
        <slot></slot>
        <div class="pagination-wrapper">
          <div class="pagination">
            <a onClick={p => this.goToPage(this.currentPage-1)} class={this.currentPage > 1 ? "show": "hide"}>&laquo;</a>
            {this.pages.map( page => (
              <a onClick={ p => this.goToPage(page+1)} class={page+1 == this.currentPage ? "active": ""}>{page+1}</a>
            ))}
            <a onClick={p => this.goToPage(this.currentPage+1)} class={this.currentPage < totalPages ? "show": "hide"}>&raquo;</a>
          </div>
        </div>
      </Host>
    );
  }
}

injectHistory(CfPagination);

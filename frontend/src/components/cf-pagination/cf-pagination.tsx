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

  getPageElement(index) {
    return (
      <a onClick={ p => this.goToPage(index+1)} class={index+1 == this.currentPage ? "active": ""}>{index+1}</a>
    );
  }

  pagination = () => {
    const items = [];
    let breakLabel: '...';
    const pageRangeDisplayed = 10;
    var totalPages = Math.ceil(this.totalRows/this.resultsPerPage);
    if(totalPages <= pageRangeDisplayed){
      for (let index = 0; index < totalPages; index++) {
        items.push(this.getPageElement(index));
      }
    }
    else{
      let leftSide = pageRangeDisplayed / 2;
      let rightSide = pageRangeDisplayed - leftSide;

      if (this.currentPage > totalPages - pageRangeDisplayed / 2) {
        rightSide = totalPages - this.currentPage;
        leftSide = pageRangeDisplayed - rightSide;
      } else if (this.currentPage < pageRangeDisplayed / 2) {
        leftSide = this.currentPage;
        rightSide = pageRangeDisplayed - leftSide;
      }

      console.log(leftSide)
      console.log(rightSide)

      let createPageView = (index) => this.getPageElement(index);
      let index;
      let breakView;

      const pagesBreaking = [];
      for (index = 0; index < totalPages; index++) {
        const page = index + 1;
        if (page <= 3) {
          pagesBreaking.push({
            type: 'page',
            index,
            display: createPageView(index),
          });
          continue;
        }
        const adjustedRightSide = this.currentPage === 0 && pageRangeDisplayed > 1 ? rightSide - 1 : rightSide;
        if(index >= this.currentPage - leftSide && index <= this.currentPage + adjustedRightSide){
          pagesBreaking.push({
            type: 'page',
            index,
            display: createPageView(index),
          });
          continue;
        }

        if(index >= totalPages - 3){
          pagesBreaking.push({
            type: 'page',
            index,
            display: createPageView(index),
          });
          continue;
        }

        if (pagesBreaking.length > 0 && pagesBreaking[pagesBreaking.length - 1].display !== breakView) {
          breakView = (
            <a>...</a>
          );
          pagesBreaking.push({ type: 'break', index, display: breakView });
        }
      }
      pagesBreaking.forEach((pageElement, i) => {
        let actualPageElement = pageElement;
        if (
          pageElement.type === 'break' &&
          pagesBreaking[i - 1] &&
          pagesBreaking[i - 1].type === 'page' &&
          pagesBreaking[i + 1] &&
          pagesBreaking[i + 1].type === 'page' &&
          pagesBreaking[i + 1].index - pagesBreaking[i - 1].index <= 2
        ) {
          actualPageElement = {
            type: 'page',
            index: pageElement.index,
            display: createPageView(pageElement.index),
          };
        }
        // We add the displayed elements in the same pass, to avoid another iteration.
        items.push(actualPageElement.display);
      });
    }
    return items;
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
            { this.pagination() }
            {/* {this.pages.map( page => (
              <a onClick={ p => this.goToPage(page+1)} class={page+1 == this.currentPage ? "active": ""}>{page+1}</a>
            ))} */}
            <a onClick={p => this.goToPage(this.currentPage+1)} class={this.currentPage < totalPages ? "show": "hide"}>&raquo;</a>
          </div>
        </div>
      </Host>
    );
  }
}

injectHistory(CfPagination);

import { Component, Host, h, State, Listen } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';

class SilIso6393NameListRequest {
  token: string;
  page: number;
  resultsPerPage: number;
}

class SilIso6393NameListResponse {
  error: ErrorType;
  size: number;
  iso6393Names: SilIso6393Name[];
}

@Component({
  tag: 'sil-iso-639-3-names',
  styleUrl: 'sil-iso-639-3-names.css',
  shadow: true,
})
export class SilIso6393Names {
  @State() iso6393NamesResponse: SilIso6393NameListResponse;
  @State() currentPage: number = 1;

  @Listen('pageChanged', { target: 'body' })
  async getChangedValue(event: CustomEvent) {
    console.log(event.detail);
    console.log('page changed');
    this.currentPage = event.detail;
    await this.getList(this.currentPage);
  }

  async getList(page) {
    this.iso6393NamesResponse = await fetchAs<SilIso6393NameListRequest, SilIso6393NameListResponse>('sil/iso-639-3-names/list', {
      token: globals.globalStore.state.token,
      page: page,
      resultsPerPage: 50,
    });
  }

  columnData: ColumnDescription[] = [
    {
      field: 'id',
      displayName: 'ID',
      width: 50,
      editable: false,
    },
    {
      field: '_id',
      displayName: '_ID',
      width: 200,
      editable: false,
    },
    {
      field: 'print_name',
      displayName: 'Print Name',
      width: 200,
      editable: false,
    },
    {
      field: 'inverted_name',
      displayName: 'Inverted Name',
      width: 200,
      editable: false,
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
      editable: false,
    },
    {
      field: 'owning_group',
      displayName: 'Owning Group ID',
      width: 100,
      editable: false,
    },
  ];

  async componentWillLoad() {
    var url = new URL(window.location.href);
    if (url.searchParams.has('page')) {
      this.currentPage = parseInt(url.searchParams.get('page')) > 0 ? parseInt(url.searchParams.get('page')) : 1;
    }
    await this.getList(this.currentPage);
  }

  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.iso6393NamesResponse && <cf-table rowData={this.iso6393NamesResponse.iso6393Names} columnData={this.columnData}></cf-table>}
        <cf-pagination current-page={this.currentPage} total-rows={this.iso6393NamesResponse.size} results-per-page="50" page-url="iso-639-3-names"></cf-pagination>
        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}
      </Host>
    );
  }
}

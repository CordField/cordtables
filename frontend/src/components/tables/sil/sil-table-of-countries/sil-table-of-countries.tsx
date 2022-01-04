import { Component, Host, h, State, Listen } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';

class SilTableOfCountryListRequest {
  token: string;
  page: number;
  resultsPerPage: number;
}

class SilTableOfCountryListResponse {
  error: ErrorType;
  size: number;
  tableOfCountries: SilTableOfCountry[];
}

@Component({
  tag: 'sil-table-of-countries',
  styleUrl: 'sil-table-of-countries.css',
  shadow: true,
})
export class SilTableOfCountrys {
  @State() tableOfCountriesResponse: SilTableOfCountryListResponse;
  @State() currentPage: number = 1;

  @Listen('pageChanged', { target: 'body' })
  async getChangedValue(event: CustomEvent) {
    console.log(event.detail);
    console.log('page changed');
    this.currentPage = event.detail;
    await this.getList(this.currentPage);
  }

  async getList(page) {
    this.tableOfCountriesResponse = await fetchAs<SilTableOfCountryListRequest, SilTableOfCountryListResponse>('sil/table-of-countries/list', {
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
      field: 'country_code',
      displayName: 'Country Code',
      width: 200,
      editable: false,
    },
    {
      field: 'country_name',
      displayName: 'Country Name',
      width: 200,
      editable: false,
    },
    {
      field: 'languages',
      displayName: 'Languages',
      width: 200,
      editable: false,
    },
    {
      field: 'indigenous',
      displayName: 'Indigenous',
      width: 200,
      editable: false,
    },
    {
      field: 'established',
      displayName: 'Established',
      width: 200,
      editable: false,
    },
    {
      field: 'unestablished',
      displayName: 'Unestablished',
      width: 200,
      editable: false,
    },
    {
      field: 'diversity',
      displayName: 'Diversity',
      width: 200,
      editable: false,
    },
    {
      field: 'included',
      displayName: 'Included',
      width: 200,
      editable: false,
    },
    {
      field: 'sum_of_populations',
      displayName: 'Sum Of Populations',
      width: 200,
      editable: false,
    },
    {
      field: 'mean',
      displayName: 'Mean',
      width: 200,
      editable: false,
    },
    {
      field: 'median',
      displayName: 'Median',
      width: 200,
      editable: false,
    },
    {
      field: 'population',
      displayName: 'Population',
      width: 200,
      editable: false,
    },
    {
      field: 'literacy_rate',
      displayName: 'Literacy Rate',
      width: 200,
      editable: false,
    },
    {
      field: 'conventions',
      displayName: 'Conventions',
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
        {this.tableOfCountriesResponse && <cf-table rowData={this.tableOfCountriesResponse.tableOfCountries} columnData={this.columnData}></cf-table>}
        <cf-pagination current-page={this.currentPage} total-rows={this.tableOfCountriesResponse.size} results-per-page="50" page-url="table-of-countries"></cf-pagination>
        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}
      </Host>
    );
  }
}

import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';


class SilTableOfLanguagesInCountryListRequest {
  token: string;
  page: number;
  resultsPerPage: number;
}

class SilTableOfLanguagesInCountryListResponse {
  error: ErrorType;
  size: number;
  tableOfLanguagesInCountries: SilTableOfLanguagesInCountry[];
}


@Component({
  tag: 'sil-table-of-languages-in-country',
  styleUrl: 'sil-table-of-languages-in-country.css',
  shadow: true,
})
export class SilTableOfLanguagesInCountrys {
  @State() tableOfLanguagesInCountriesResponse: SilTableOfLanguagesInCountryListResponse;
  @State() currentPage: number = 1;

  async getList(page) {
    this.tableOfLanguagesInCountriesResponse = await fetchAs<SilTableOfLanguagesInCountryListRequest, SilTableOfLanguagesInCountryListResponse>('sil-table-of-languages-in-country/list', {
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
      field: 'iso_639',
      displayName: 'ISO 639',
      width: 200,
      editable: false,
    },
    {
      field: 'language_name',
      displayName: 'Language Name',
      width: 200,
      editable: false,
    },
    {
        field: 'uninverted_name',
        displayName: 'Uninverted Name',
        width: 200,
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
        field: 'region_code',
        displayName: 'Region Code',
        width: 200,
        editable: false,
      },
      {
        field: 'region_name',
        displayName: 'Region Name',
        width: 200,
        editable: false,
      },
      {
        field: 'area',
        displayName: 'Area',
        width: 200,
        editable: false,
      },
      {
        field: 'is_primary',
        displayName: 'Is Primary',
        width: 200,
        editable: false,
      },
      {
        field: 'is_indigenous',
        displayName: 'Is Indigenous',
        width: 200,
        editable: false,
      },
      {
        field: 'is_established',
        displayName: 'Is Established',
        width: 200,
        editable: false,
    },
    {
        field: 'all_users',
        displayName: 'All Users',
        width: 200,
        editable: false,
    },
    {
        field: 'l1_users',
        displayName: 'L1 Users',
        width: 200,
        editable: false,
    },
    {
        field: 'l2_users',
        displayName: 'L2 Users',
        width: 200,
        editable: false,
    },
    {
        field: 'family',
        displayName: 'Family',
        width: 200,
        editable: false,
    },
    {
        field: 'egids',
        displayName: 'EG IDs',
        width: 200,
        editable: false,
    },
    {
      field: 'function_code',
      displayName: 'Function Code',
      width: 200,
      editable: false,
    },
    {
      field: 'function_label',
      displayName: 'Function Label',
      width: 200,
      editable: false,
    },
    {
        field: 'institutional',
        displayName: 'Institutional',
        width: 200,
        editable: false,
    },
    {
        field: 'developing',
        displayName: 'Developing',
        width: 200,
        editable: false,
    },
    {
        field: 'vigorous',
        displayName: 'Vigorous',
        width: 200,
        editable: false,
    },
    {
        field: 'in_trouble',
        displayName: 'In Trouble',
        width: 200,
        editable: false,
    },
    {
        field: 'dying',
        displayName: 'Dying',
        width: 200,
        editable: false,
    },
    {
        field: 'extinct',
        displayName: 'Extinct',
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
    await this.getList(this.currentPage);
    // await this.getFilesList();
  }


  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.tableOfLanguagesInCountriesResponse && <cf-table rowData={this.tableOfLanguagesInCountriesResponse.tableOfLanguagesInCountries} columnData={this.columnData}></cf-table>}
        <cf-pagination current-page={this.currentPage} total-rows={this.tableOfLanguagesInCountriesResponse.size} results-per-page="50" page-url="sil-table-of-languages-in-country"></cf-pagination>
        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

      </Host>
    );
  }

}

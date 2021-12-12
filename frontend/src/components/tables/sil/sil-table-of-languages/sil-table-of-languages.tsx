import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';


class SilTableOfLanguageListRequest {
  token: string;
  page: number;
  resultsPerPage: number;
}

class SilTableOfLanguageListResponse {
  error: ErrorType;
  size: number;
  tableOfLanguages: SilTableOfLanguage[];
}


@Component({
  tag: 'sil-table-of-languages',
  styleUrl: 'sil-table-of-languages.css',
  shadow: true,
})
export class SilTableOfLanguages {
  @State() tableOfLanguagesResponse: SilTableOfLanguageListResponse;
  @State() currentPage: number = 1;

  async getList(page) {
    this.tableOfLanguagesResponse = await fetchAs<SilTableOfLanguageListRequest, SilTableOfLanguageListResponse>('sil-table-of-languages/list', {
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
      field: 'l1_users',
      displayName: 'L1 Users',
      width: 200,
      editable: false,
    },
    {
      field: 'digits',
      displayName: 'Digits',
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
      field: 'countries',
      displayName: 'Countries',
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
      field: 'classification',
      displayName: 'Classification',
      width: 200,
      editable: false,
    },
    {
      field: 'latitude',
      displayName: 'Latitude',
      width: 200,
      editable: false,
    },
    {
      field: 'longitude',
      displayName: 'Longitude',
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
      field: 'is_written',
      displayName: 'Is Written',
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
        {this.tableOfLanguagesResponse && <cf-table rowData={this.tableOfLanguagesResponse.tableOfLanguages} columnData={this.columnData}></cf-table>}
        <cf-pagination current-page={this.currentPage} total-rows={this.tableOfLanguagesResponse.size} results-per-page="50" page-url="sil-table-of-languages"></cf-pagination>
        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

      </Host>
    );
  }

}

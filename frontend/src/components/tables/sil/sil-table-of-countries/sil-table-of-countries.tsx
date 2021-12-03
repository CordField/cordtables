import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';


class SilTableOfCountryListRequest {
  token: string;
}

class SilTableOfCountryListResponse {
  error: ErrorType;
  tableOfCountries: SilTableOfCountry[];
}


@Component({
  tag: 'sil-table-of-countries',
  styleUrl: 'sil-table-of-countries.css',
  shadow: true,
})
export class SilTableOfCountrys {

  @State() tableOfCountriesResponse: SilTableOfCountryListResponse;

  

  async getList() {
    this.tableOfCountriesResponse = await fetchAs<SilTableOfCountryListRequest, SilTableOfCountryListResponse>('sil-table-of-countries/list', {
      token: globals.globalStore.state.token,
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
    await this.getList();
    // await this.getFilesList();
  }


  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.tableOfCountriesResponse && <cf-table rowData={this.tableOfCountriesResponse.tableOfCountries} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

      </Host>
    );
  }

}

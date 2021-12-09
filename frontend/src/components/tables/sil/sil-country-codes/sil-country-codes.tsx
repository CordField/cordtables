import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';


class SilCountryCodeListRequest {
  token: string;
}

class SilCountryCodeListResponse {
  error: ErrorType;
  countryCodes: SilCountryCode[];
}


@Component({
  tag: 'sil-country-codes',
  styleUrl: 'sil-country-codes.css',
  shadow: true,
})
export class SilCountryCodes {

  @State() countryCodesResponse: SilCountryCodeListResponse;

  

  async getList() {
    this.countryCodesResponse = await fetchAs<SilCountryCodeListRequest, SilCountryCodeListResponse>('sil-country-codes/list', {
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
      field: 'country',
      displayName: 'country',
      width: 200,
      editable: false,
    },
    {
      field: 'name',
      displayName: 'Name',
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
        {this.countryCodesResponse && <cf-table rowData={this.countryCodesResponse.countryCodes} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

      </Host>
    );
  }

}

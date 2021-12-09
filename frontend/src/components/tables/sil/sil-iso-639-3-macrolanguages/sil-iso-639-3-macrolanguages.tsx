import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';


class SilIso6393MacrolanguageListRequest {
  token: string;
}

class SilIso6393MacrolanguageListResponse {
  error: ErrorType;
  iso6393Macrolanguages: SilIso6393Macrolanguage[];
}


@Component({
  tag: 'sil-iso-639-3-macrolanguages',
  styleUrl: 'sil-iso-639-3-macrolanguages.css',
  shadow: true,
})
export class SilIso6393Macrolanguages {

  @State() iso6393MacrolanguagesResponse: SilIso6393MacrolanguageListResponse;

  

  async getList() {
    this.iso6393MacrolanguagesResponse = await fetchAs<SilIso6393MacrolanguageListRequest, SilIso6393MacrolanguageListResponse>('sil-iso-639-3-macrolanguages/list', {
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
      field: 'm_id',
      displayName: 'M ID',
      width: 200,
      editable: false,
    },
    {
      field: 'i_id',
      displayName: 'I ID',
      width: 200,
      editable: false,
    },
    {
        field: 'i_status',
        displayName: 'I Status',
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
        {this.iso6393MacrolanguagesResponse && <cf-table rowData={this.iso6393MacrolanguagesResponse.iso6393Macrolanguages} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

      </Host>
    );
  }

}

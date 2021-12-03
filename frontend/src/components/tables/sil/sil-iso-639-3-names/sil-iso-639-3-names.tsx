import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';


class SilIso6393NameListRequest {
  token: string;
}

class SilIso6393NameListResponse {
  error: ErrorType;
  iso6393Names: SilIso6393Name[];
}


@Component({
  tag: 'sil-iso-639-3-names',
  styleUrl: 'sil-iso-639-3-names.css',
  shadow: true,
})
export class SilIso6393Names {

  @State() iso6393NamesResponse: SilIso6393NameListResponse;

  

  async getList() {
    this.iso6393NamesResponse = await fetchAs<SilIso6393NameListRequest, SilIso6393NameListResponse>('sil-iso-639-3-names/list', {
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
    await this.getList();
    // await this.getFilesList();
  }


  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.iso6393NamesResponse && <cf-table rowData={this.iso6393NamesResponse.iso6393Names} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

      </Host>
    );
  }

}

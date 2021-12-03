import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';


class SilIso6393ListRequest {
  token: string;
}

class SilIso6393ListResponse {
  error: ErrorType;
  iso6393s: SilIso6393[];
}


@Component({
  tag: 'sil-iso-639-3',
  styleUrl: 'sil-iso-639-3.css',
  shadow: true,
})
export class SilIso6393s {

  @State() iso6393sResponse: SilIso6393ListResponse;

  

  async getList() {
    this.iso6393sResponse = await fetchAs<SilIso6393ListRequest, SilIso6393ListResponse>('sil-iso-639-3/list', {
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
      field: 'part_2b',
      displayName: 'Part 2b',
      width: 200,
      editable: false,
    },
    {
        field: 'part_2t',
        displayName: 'Part 2t',
        width: 200,
        editable: false,
      },
      {
        field: 'part_1',
        displayName: 'Part 1',
        width: 200,
        editable: false,
      },
      {
        field: 'scope',
        displayName: 'Scope',
        width: 200,
        editable: false,
      },
      {
        field: 'type',
        displayName: 'Type',
        width: 200,
        editable: false,
      },
      {
        field: 'ref_name',
        displayName: 'Ref Name',
        width: 200,
        editable: false,
      },
      {
        field: 'comment',
        displayName: 'Comment',
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
        {this.iso6393sResponse && <cf-table rowData={this.iso6393sResponse.iso6393s} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

      </Host>
    );
  }

}

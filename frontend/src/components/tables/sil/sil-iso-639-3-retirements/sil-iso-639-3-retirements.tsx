import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';


class SilIso6393RetirementListRequest {
  token: string;
}

class SilIso6393RetirementListResponse {
  error: ErrorType;
  iso6393Retirements: SilIso6393Retirement[];
}


@Component({
  tag: 'sil-iso-639-3-retirements',
  styleUrl: 'sil-iso-639-3-retirements.css',
  shadow: true,
})
export class SilIso6393Retirements {

  @State() iso6393RetirementsResponse: SilIso6393RetirementListResponse;

  

  async getList() {
    this.iso6393RetirementsResponse = await fetchAs<SilIso6393RetirementListRequest, SilIso6393RetirementListResponse>('sil-iso-639-3-retirements/list', {
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
      field: 'ref_name',
      displayName: 'Ref Name',
      width: 200,
      editable: false,
    },
    {
        field: 'ret_reason',
        displayName: 'Ret Reason',
        width: 200,
        editable: false,
      },
      {
        field: 'change_to',
        displayName: 'Change To',
        width: 200,
        editable: false,
      },
      {
        field: 'ret_remedy',
        displayName: 'Ret Remedy',
        width: 200,
        editable: false,
      },
      {
        field: 'effective',
        displayName: 'Effective',
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
        {this.iso6393RetirementsResponse && <cf-table rowData={this.iso6393RetirementsResponse.iso6393Retirements} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

      </Host>
    );
  }

}

import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';


class SilLanguageCodeListRequest {
  token: string;
}

class SilLanguageCodeListResponse {
  error: ErrorType;
  languageCodes: SilLanguageCode[];
}


@Component({
  tag: 'sil-language-codes',
  styleUrl: 'sil-language-codes.css',
  shadow: true,
})
export class SilLanguageCodes {

  @State() languageCodesResponse: SilLanguageCodeListResponse;

  

  async getList() {
    this.languageCodesResponse = await fetchAs<SilLanguageCodeListRequest, SilLanguageCodeListResponse>('sil-language-codes/list', {
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
      field: 'lang',
      displayName: 'Lang',
      width: 200,
      editable: false,
    },
    {
      field: 'country',
      displayName: 'Country',
      width: 200,
      editable: false,
    },
    {
      field: 'lang_status',
      displayName: 'Lang Status',
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
        {this.languageCodesResponse && <cf-table rowData={this.languageCodesResponse.languageCodes} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

      </Host>
    );
  }

}

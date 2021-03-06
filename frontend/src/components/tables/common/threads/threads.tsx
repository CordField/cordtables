import { loadingController } from '@ionic/core';
import { Component, Host, h, State, Listen } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { AutocompleteRequest, AutocompleteResponse, ErrorType, GenericResponse } from '../../../../common/types';
import { convertToSnakeCase, fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import {
  CommonThread,
  CommonThreadsListRequest,
  CommonThreadsListResponse,
  CommonThreadsUpdateRequest,
  CommonThreadsUpdateResponse,
  CreateCommonThreadsRequest,
  CreateCommonThreadsResponse,
  DeleteCommonThreadsRequest,
  DeleteCommonThreadsResponse,
} from './types';

@Component({
  tag: 'common-threads',
  styleUrl: 'threads.css',
  shadow: true,
})
export class Threads {
  @State() commonThreadsResponse: CommonThreadsListResponse;
  @State() threads: CommonThread[] = [];
  @State() applicationState: 'loading' | 'initialResponse' | 'autocompleteResponse' = 'loading';
  newContent: string;
  newChannel: string;
  @Listen('searchResults')
  async showSearchResults(event: CustomEvent<any>) {
    this.commonThreadsResponse = { error: ErrorType.NoError, threads: event.detail as CommonThread[] };
  }

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonThreadsUpdateRequest, CommonThreadsUpdateResponse>('common/threads/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    if (updateResponse.error == ErrorType.NoError) {
      this.commonThreadsResponse = {
        error: ErrorType.NoError,
        threads: this.commonThreadsResponse.threads.map(thread => (thread.id === id ? updateResponse.thread : thread)),
      };
      return true;
    } else {
      alert(updateResponse.error);
      return false;
    }
  };

  handleDelete = async id => {
    const result = await fetchAs<DeleteCommonThreadsRequest, DeleteCommonThreadsResponse>('common/threads/delete', {
      id,
      token: globals.globalStore.state.token,
    });
    if (result.error === ErrorType.NoError) {
      this.getList();
      return true;
    } else {
      return false;
    }
  };

  async componentWillLoad() {
    await this.getList();
  }

  async getList() {
    this.commonThreadsResponse = await fetchAs<CommonThreadsListRequest, CommonThreadsListResponse>('common/threads/list', {
      token: globals.globalStore.state.token,
    });
    if (this.commonThreadsResponse.error === ErrorType.NoError) {
      this.applicationState = 'initialResponse';
      // this.threads = this.commonThreadsResponse.threads;
      await this.updateForeignKeys();
    }
  }

  // async getForeignKeyColumns() {
  //   let foreignKeyValue;
  //   this.commonThreadsResponse.threads.map(thread => {
  //     this.columnData.map(column => {
  //       console.log(column);
  //       if (column.foreignKey !== undefined && column.foreignKey !== null)
  //         fetchAs<AutocompleteRequest, AutocompleteResponse>('admin/autocomplete', {
  //           searchColumnName: 'id',
  //           resultColumnName: column.foreignTableColumn,
  //           // convert resultColumnName to CamelCase
  //           token: globals.globalStore.state.token,
  //           searchKeyword: thread[column.field],
  //           tableName: column.foreignKey.split('/').join('.'),
  //         }).then(foreignKeyValue => {
  //           if (foreignKeyValue.error === ErrorType.NoError) {
  //             this.commonThreadsResponse = {
  //               error: ErrorType.NoError,
  //               threads: this.commonThreadsResponse.threads.map(thread2 => {
  //                 if (thread2.id === thread.id) {
  //                   thread2[column.displayName] = foreignKeyValue.data;
  //                 }
  //                 return thread2;
  //               }),
  //             };
  //           }
  //         });
  //     });
  //   });
  // }

  contentChange(event) {
    this.newContent = event.target.value;
  }

  channelChange(event) {
    this.newChannel = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const result = await fetchAs<CreateCommonThreadsRequest, CreateCommonThreadsResponse>('common/threads/create-read', {
      token: globals.globalStore.state.token,
      thread: {
        content: this.newContent,
        channel: this.newChannel,
      },
    });

    if (result.error === ErrorType.NoError) {
      globals.globalStore.state.editMode = false;
      this.getList();
    }
  };

  columnData: ColumnDescription[] = [
    {
      field: 'id',
      displayName: 'ID',
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'content',
      displayName: 'Content',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'channel',
      displayName: 'Channel',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
      foreignKey: 'common/discussion-channels',
      foreignTableColumn: 'name',
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
      foreignKey: 'admin/people',
      foreignTableColumn: 'public_first_name',
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
      foreignKey: 'admin/people',
      foreignTableColumn: 'public_first_name',
    },
    {
      field: 'owning_person',
      displayName: 'Owning Person ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
      foreignKey: 'admin/people',
      foreignTableColumn: 'public_first_name',
    },
    {
      field: 'owning_group',
      displayName: 'Owning Group ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
      foreignKey: 'admin/groups',
      foreignTableColumn: 'name',
    },
  ];
  async updateForeignKeys() {
    for (const thread of this.commonThreadsResponse.threads) {
      for (const column of this.columnData) {
        if (column.foreignKey !== null && column.foreignKey !== undefined) {
          const autocompleteData = await fetchAs<AutocompleteRequest, AutocompleteResponse>('admin/autocomplete', {
            token: globals.globalStore.state.token,
            searchColumnName: 'id',
            resultColumnName: column.foreignTableColumn,
            tableName: column.foreignKey.split('/').join('.').replace('-', '_'),
            searchKeyword: thread[column.field],
          });
          console.log(autocompleteData);
          if (autocompleteData.error === ErrorType.NoError) {
            this.threads = this.commonThreadsResponse.threads.map(thread2 => {
              if (thread.id === thread2.id) {
                thread2[column.field] = {
                  value: thread[column.field],
                  displayValue: autocompleteData.data,
                };
              }
              return thread2;
            });
          }
        }
      }
    }
    this.applicationState = 'autocompleteResponse';
    console.log(this.applicationState, this.threads);
  }
  render() {
    return (
      <Host>
        <slot></slot>
        <search-form columnNames={['id', 'content', 'channel', 'created_at', 'created_by', 'modified_at', 'modified_by', 'owning_person', 'owning_group']}></search-form>
        {this.applicationState === 'initialResponse' ? (
          <cf-table rowData={this.commonThreadsResponse.threads} columnData={this.columnData}></cf-table>
        ) : this.applicationState === 'autocompleteResponse' ? (
          <cf-table rowData={this.threads} columnData={this.columnData}></cf-table>
        ) : (
          'loading...'
        )}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <label>
              {' '}
              <strong>New Thread:</strong>
            </label>
            <div id="table-name-holder" class="form-input-item form-thing">
              <br />
              <span class="form-thing">
                <label htmlFor="">Channel:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="channel-table-name" name="channel-table-name" onInput={event => this.channelChange(event)} />
              </span>
            </div>
            <div id="column-name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="column-name">Content:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="column-name" name="column-name" onInput={event => this.contentChange(event)} />
              </span>
            </div>
            <span class="form-thing">
              <input id="create-button" type="submit" value="Create" onClick={this.handleInsert} />
            </span>
          </form>
        )}
      </Host>
    );
  }
}

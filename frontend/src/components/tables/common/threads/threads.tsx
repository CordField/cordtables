import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import {
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
  newContent: string;
  newChannel: number;

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
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
  }

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
      width: 50,
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
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'owning_group',
      displayName: 'Owning Group ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
  ];

  render() {
    return (
      <Host>
        <slot></slot>
        {this.commonThreadsResponse && <cf-table rowData={this.commonThreadsResponse.threads} columnData={this.columnData}></cf-table>}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <label>
              {' '}
              <strong> New Post: </strong>
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

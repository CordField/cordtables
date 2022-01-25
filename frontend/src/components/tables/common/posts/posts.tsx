import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { AutocompleteRequest, AutocompleteResponse, ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import {
  CommonPost,
  CommonPostsListRequest,
  CommonPostsListResponse,
  CommonPostsUpdateRequest,
  CommonPostsUpdateResponse,
  CreateCommonPostsRequest,
  CreateCommonPostsResponse,
  DeleteCommonPostsRequest,
  DeleteCommonPostsResponse,
} from './types';

@Component({
  tag: 'common-posts',
  styleUrl: 'posts.css',
  shadow: true,
})
export class Posts {
  @State() commonPostsResponse: CommonPostsListResponse;
  newContent: string;
  newThread: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonPostsUpdateRequest, CommonPostsUpdateResponse>('common/posts/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    if (updateResponse.error == ErrorType.NoError) {
      this.commonPostsResponse = {
        error: ErrorType.NoError,
        posts: this.commonPostsResponse.posts.map(post => (post.id === id ? updateResponse.post : post)),
      };
      return true;
    } else {
      alert(updateResponse.error);
      return false;
    }
  };

  handleDelete = async id => {
    const result = await fetchAs<DeleteCommonPostsRequest, DeleteCommonPostsResponse>('common/posts/delete', {
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
    this.commonPostsResponse = await fetchAs<CommonPostsListRequest, CommonPostsListResponse>('common/posts/list', {
      token: globals.globalStore.state.token,
    });
    if (this.commonPostsResponse.error === ErrorType.NoError) {
      await this.updateForeignKeys();
    }
  }

  contentChange(event) {
    this.newContent = event.target.value;
  }

  threadChange(event) {
    this.newThread = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const result = await fetchAs<CreateCommonPostsRequest, CreateCommonPostsResponse>('common/posts/create-read', {
      token: globals.globalStore.state.token,
      post: {
        content: this.newContent,
        thread: this.newThread,
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
      field: 'thread',
      displayName: 'Thread',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
      foreignKey: 'common.threads',
      foreignTableColumn: 'content',
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
    for (const post of this.commonPostsResponse.posts) {
      for (const column of this.columnData) {
        if (column.foreignKey !== null && column.foreignKey !== undefined) {
          const autocompleteData = await fetchAs<AutocompleteRequest, AutocompleteResponse>('admin/autocomplete', {
            token: globals.globalStore.state.token,
            searchColumnName: 'id',
            resultColumnName: column.foreignTableColumn,
            tableName: column.foreignKey.split('/').join('.').replace('-', '_'),
            searchKeyword: post[column.field],
          });
          console.log(autocompleteData);
          if (autocompleteData.error === ErrorType.NoError) {
            this.commonPostsResponse.posts.map(post2 => {
              if (post.id === post2.id) {
                post2[column.field] = {
                  value: post[column.field],
                  displayValue: autocompleteData.data,
                };
              }
              return post2;
            });
          }
        }
      }
    }
  }

  render() {
    return (
      <Host>
        <slot></slot>
        {this.commonPostsResponse && <cf-table rowData={this.commonPostsResponse.posts} columnData={this.columnData}></cf-table>}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <label>
              {' '}
              <strong> New Post: </strong>
            </label>
            <div id="table-name-holder" class="form-input-item form-thing">
              <br />
              <span class="form-thing">
                <label htmlFor="">Thread:</label>
              </span>
              <span class="form-thing">
                <input type="text" id="channel-table-name" name="channel-table-name" onInput={event => this.threadChange(event)} />
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

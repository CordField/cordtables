import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';

class CommonPost {
  id?: number | undefined;
  content?: string | undefined;
  thread?: number | undefined;
  created_at?: string | undefined;
  created_by?: number | undefined;
  modified_at?: string | undefined;
  modified_by?: number | undefined;
  owning_person?: number | undefined;
  owning_group?: number | undefined;
}

class CreateCommonPostsRequest {
  token: string;
  post: {
    content: string;
    thread: number;
  };
}

class CreateCommonPostsResponse extends GenericResponse {
  post: CommonPost;
}

class CommonPostsListRequest {
  token: string;
}

class CommonPostsListResponse {
  error: ErrorType;
  posts: CommonPost[];
}

class CommonPostsUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class CommonPostsUpdateResponse {
  error: ErrorType;
  post: CommonPost | null = null;
}

class DeleteCommonPostsRequest {
  id: number;
  token: string;
}

class DeleteCommonPostsResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'common-posts',
  styleUrl: 'posts.css',
  shadow: true,
})
export class Posts {
  @State() commonPostsResponse: CommonPostsListResponse;
  newContent: string;
  newThread: number;

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonPostsUpdateRequest, CommonPostsUpdateResponse>('common-posts/update-read', {
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
    const result = await fetchAs<DeleteCommonPostsRequest, DeleteCommonPostsResponse>('common-posts/delete', {
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
    this.commonPostsResponse = await fetchAs<CommonPostsListRequest, CommonPostsListResponse>('common-posts/list', {
      token: globals.globalStore.state.token,
    });
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

    const result = await fetchAs<CreateCommonPostsRequest, CreateCommonPostsResponse>('common-cell-channels/create-read', {
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
      field: 'thread',
      displayName: 'Thread',
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

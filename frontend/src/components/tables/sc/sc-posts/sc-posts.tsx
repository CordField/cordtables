import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreatePostExRequest {
  token: string;
  post: {
    directory: number;
    type: string;
    shareability: string;
    body: string;
  };
}
class CreatePostExResponse extends GenericResponse {
  post: ScPost;
}

class ScPostListRequest {
  token: string;
}

class ScPostListResponse {
  error: ErrorType;
  posts: ScPost[];
}

class ScPostUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScPostUpdateResponse {
  error: ErrorType;
  post: ScPost | null = null;
}

class DeletePostExRequest {
  id: string;
  token: string;
}

class DeletePostExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-posts',
  styleUrl: 'sc-posts.css',
  shadow: true,
})
export class ScPosts {
  @State() postsResponse: ScPostListResponse;

  newDirectory: number;
  newType: string;
  newShareability: string;
  newBody: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScPostUpdateRequest, ScPostUpdateResponse>('sc/posts/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.postsResponse = { error: ErrorType.NoError, posts: this.postsResponse.posts.map(post => (post.id === id ? updateResponse.post : post)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeletePostExRequest, DeletePostExResponse>('sc/posts/delete', {
      id,
      token: globals.globalStore.state.token,
    });
    if (deleteResponse.error === ErrorType.NoError) {
      this.getList();
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item deleted successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: deleteResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  async getList() {
    this.postsResponse = await fetchAs<ScPostListRequest, ScPostListResponse>('sc/posts/list', {
      token: globals.globalStore.state.token,
    });
  }

  directoryChange(event) {
    this.newDirectory = event.target.value;
  }

  typeChange(event) {
    this.newType = event.target.value;
  }

  shareabilityChange(event) {
    this.newShareability = event.target.value;
  }

  bodyChange(event) {
    this.newBody = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreatePostExRequest, CreatePostExResponse>('sc/posts/create-read', {
      token: globals.globalStore.state.token,
      post: {
        directory: this.newDirectory,
        type: this.newType,
        shareability: this.newShareability,
        body: this.newBody,
      },
    });

    if (createResponse.error === ErrorType.NoError) {
      globals.globalStore.state.editMode = false;
      this.getList();
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item inserted successfully', id: uuidv4(), type: 'success' });
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: createResponse.error, id: uuidv4(), type: 'error' });
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
      field: 'directory',
      displayName: 'Directory',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'type',
      displayName: 'Type',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'Note', value: 'Note' },
        { display: 'Story', value: 'Story' },
        { display: 'Prayer', value: 'Prayer' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'shareability',
      displayName: 'Shareability',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'Project Team', value: 'Project Team' },
        { display: 'Internal', value: 'Internal' },
        { display: 'Ask to Share Externally', value: 'Ask to Share Externally' },
        { display: 'External', value: 'External' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'body',
      displayName: 'body',
      width: 300,
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

  async componentWillLoad() {
    await this.getList();
    // await this.getFilesList();
  }

  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.postsResponse && <cf-table rowData={this.postsResponse.posts} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="directory-holder" class="form-input-item form-thing">
              <span class="directory-thing">
                <label htmlFor="directory">Directory</label>
              </span>
              <span class="form-thing">
                <input type="text" id="directory" name="directory" onInput={event => this.directoryChange(event)} />
              </span>
            </div>

            <div id="type-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="type">Type</label>
              </span>
              <span class="form-thing">
                <select id="type" name="type" onInput={event => this.typeChange(event)}>
                  <option value="">Select Type</option>
                  <option value="Note" selected={this.newType === 'Note'}>
                    Note
                  </option>
                  <option value="Story" selected={this.newType === 'Story'}>
                    Story
                  </option>
                  <option value="Prayer" selected={this.newType === 'Prayer'}>
                    Prayer
                  </option>
                </select>
              </span>
            </div>

            <div id="shareability-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="shareability">Shareability</label>
              </span>
              <span class="form-thing">
                <select id="shareability" name="shareability" onInput={event => this.shareabilityChange(event)}>
                  <option value="">Select Type</option>
                  <option value="Project Team" selected={this.newShareability === 'Project Team'}>
                    Project Team
                  </option>
                  <option value="Internal" selected={this.newShareability === 'Internal'}>
                    Internal
                  </option>
                  <option value="Ask to Share Externally" selected={this.newShareability === 'Ask to Share Externally'}>
                    Ask to Share Externally
                  </option>
                  <option value="External" selected={this.newShareability === 'External'}>
                    External
                  </option>
                </select>
              </span>
            </div>

            <div id="body-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="body">Body</label>
              </span>
              <span class="form-thing">
                <textarea id="body" name="body" onInput={event => this.bodyChange(event)}></textarea>
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

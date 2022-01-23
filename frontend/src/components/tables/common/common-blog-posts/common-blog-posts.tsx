import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { AutocompleteRequest, AutocompleteResponse, ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateBlogPostExRequest {
  token: string;
  blogPost: {
    blog: string;
    content: string;
  };
}
class CreateBlogPostExResponse extends GenericResponse {
  blogPost: CommonBlogPost;
}

class CommonBlogPostListRequest {
  token: string;
}

class CommonBlogPostListResponse {
  error: ErrorType;
  blogPosts: CommonBlogPost[];
}

class CommonBlogPostUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class CommonBlogPostUpdateResponse {
  error: ErrorType;
  blogPost: CommonBlogPost | null = null;
}

class DeleteBlogPostExRequest {
  id: string;
  token: string;
}

class DeleteBlogPostExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'common-blog-posts',
  styleUrl: 'common-blog-posts.css',
  shadow: true,
})
export class CommonBlogPosts {
  @State() blogPostsResponse: CommonBlogPostListResponse;

  newBlog: string;
  newPerson: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonBlogPostUpdateRequest, CommonBlogPostUpdateResponse>('common/blog-posts/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.blogPostsResponse = {
        error: ErrorType.NoError,
        blogPosts: this.blogPostsResponse.blogPosts.map(blogPost => (blogPost.id === id ? updateResponse.blogPost : blogPost)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteBlogPostExRequest, DeleteBlogPostExResponse>('common/blog-posts/delete', {
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
    this.blogPostsResponse = await fetchAs<CommonBlogPostListRequest, CommonBlogPostListResponse>('common/blog-posts/list', {
      token: globals.globalStore.state.token,
    });
    if (this.blogPostsResponse.error === ErrorType.NoError) {
      await this.updateForeignKeys();
    }
  }

  async updateForeignKeys() {
    for (const thread of this.blogPostsResponse.blogPosts) {
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
            this.blogPostsResponse.blogPosts.map(thread2 => {
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
  }
  blogChange(event) {
    this.newBlog = event.target.value;
  }

  contentChange(event) {
    this.newPerson = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateBlogPostExRequest, CreateBlogPostExResponse>('common/blog-posts/create-read', {
      token: globals.globalStore.state.token,
      blogPost: {
        blog: this.newBlog,
        content: this.newPerson,
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
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'blog',
      displayName: 'Blog ID',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
      foreignKey: 'common/blogs',
      foreignTableColumn: 'title',
    },
    {
      field: 'content',
      displayName: 'content',
      width: 250,
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

  async componentWillLoad() {
    await this.getList();
    // await this.getFilesList();
  }

  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.blogPostsResponse && <cf-table rowData={this.blogPostsResponse.blogPosts} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="blog-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="blog">Blog ID</label>
              </span>
              <span class="form-thing">
                <input type="text" id="director" name="director" onInput={event => this.blogChange(event)} />
              </span>
            </div>

            <div id="content-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="content">Content</label>
              </span>
              <span class="form-thing">
                <textarea id="content" name="content" onInput={event => this.contentChange(event)}></textarea>
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

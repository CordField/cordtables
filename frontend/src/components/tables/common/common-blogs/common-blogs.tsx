import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { AutocompleteRequest, AutocompleteResponse, ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateBlogExRequest {
  token: string;
  blog: {
    title: string;
  };
}
class CreateBlogExResponse extends GenericResponse {
  blog: CommonBlog;
}

class CommonBlogListRequest {
  token: string;
}

class CommonBlogListResponse {
  error: ErrorType;
  blogs: CommonBlog[];
}

class CommonBlogUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class CommonBlogUpdateResponse {
  error: ErrorType;
  blog: CommonBlog | null = null;
}

class DeleteBlogExRequest {
  id: string;
  token: string;
}

class DeleteBlogExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'common-blogs',
  styleUrl: 'common-blogs.css',
  shadow: true,
})
export class CommonBlogs {
  @State() blogsResponse: CommonBlogListResponse;

  newTitle: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonBlogUpdateRequest, CommonBlogUpdateResponse>('common/blogs/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.blogsResponse = {
        error: ErrorType.NoError,
        blogs: this.blogsResponse.blogs.map(blog => (blog.id === id ? updateResponse.blog : blog)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteBlogExRequest, DeleteBlogExResponse>('common/blogs/delete', {
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
    this.blogsResponse = await fetchAs<CommonBlogListRequest, CommonBlogListResponse>('common/blogs/list', {
      token: globals.globalStore.state.token,
    });
    if (this.blogsResponse.error === ErrorType.NoError) {
      await this.updateForeignKeys();
    }
  }

  async updateForeignKeys() {
    for (const blog of this.blogsResponse.blogs) {
      for (const column of this.columnData) {
        if (column.foreignKey !== null && column.foreignKey !== undefined) {
          const autocompleteData = await fetchAs<AutocompleteRequest, AutocompleteResponse>('admin/autocomplete', {
            token: globals.globalStore.state.token,
            searchColumnName: 'id',
            resultColumnName: column.foreignTableColumn,
            tableName: column.foreignKey.split('/').join('.').replace('-', '_'),
            searchKeyword: blog[column.field],
          });
          console.log(autocompleteData);
          if (autocompleteData.error === ErrorType.NoError) {
            this.blogsResponse.blogs.map(blog2 => {
              if (blog.id === blog2.id) {
                blog2[column.field] = {
                  value: blog[column.field],
                  displayValue: autocompleteData.data,
                };
              }
              return blog2;
            });
          }
        }
      }
    }
  }

  titleChange(event) {
    this.newTitle = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateBlogExRequest, CreateBlogExResponse>('common/blogs/create-read', {
      token: globals.globalStore.state.token,
      blog: {
        title: this.newTitle,
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
      field: 'title',
      displayName: 'Title',
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
        {this.blogsResponse && <cf-table rowData={this.blogsResponse.blogs} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="title-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="title">Blog Title</label>
              </span>
              <span class="form-thing">
                <input type="text" id="title" name="title" onInput={event => this.titleChange(event)} />
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

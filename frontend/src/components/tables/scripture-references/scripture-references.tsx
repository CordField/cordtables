import { Component, Host, h, State } from '@stencil/core';
import { ActionType, ErrorType, ScriptureReference } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';

type MutableScriptureReferenceFields = Partial<ScriptureReference>;

class ScriptureReferenceListRequest {
  token: string;
}

class ScriptureReferenceListResponse {
  error: ErrorType;
  response?: Array<ScriptureReference>;
}

class ScriptureReferenceCreateRequest {
  token: string;
  book_start: string;
  book_end: string;
  chapter_start: number;
  chapter_end: number;
  verse_start: number;
  verse_end: number;
}

class ScriptureReferenceCreateResponse {
  error: ErrorType;
  response: ScriptureReference;
}

class ScriptureReferenceUpdateRequest {
  token: string;
  id: number;
  updatedFields: MutableScriptureReferenceFields;
}

class ScriptureReferenceUpdateResponse {
  error: ErrorType;
  response: ScriptureReference;
}

class ScriptureReferenceDeleteRequest {
  token: string;
  id: number;
}

class ScriptureReferenceDeleteResponse {
  error: ErrorType;
}

@Component({
  tag: 'scripture-references',
  styleUrl: 'scripture-references.css',
  shadow: true,
})
export class ScriptureReferences {
  defaultFields = {
    id: null,
    book_start: 'Genesis',
    book_end: 'Genesis',
    chapter_start: null,
    chapter_end: null,
    verse_start: null,
    verse_end: null,
  };

  nonEditableColumns = ['id'];

  @State() list: Array<ScriptureReference>;
  @State() showNewForm = false;
  @State() insertedFields: ScriptureReference = this.defaultFields;
  @State() updatedFields: MutableScriptureReferenceFields = {};
  @State() message: string;
  createResponse: ScriptureReferenceCreateResponse;
  deleteResponse: ScriptureReferenceDeleteResponse;

  newRowName: string;

  editableKeys = ['book_start', 'book_end', 'chapter_start', 'chapter_end', 'verse_start', 'verse_end'];

  bookNames = ['Genesis', 'Matthew', 'Revelation'];

  componentWillLoad() {
    this.getList();
  }

  async getList() {
    const result = await fetchAs<ScriptureReferenceListRequest, ScriptureReferenceListResponse>('table/common-scripture-references/list', {
      token: globals.globalStore.state.token,
    });
    if (result.error === ErrorType.NoError) {
      this.list = result.response;
    } else {
      this.message = result.error;
    }
  }

  toggleNewForm = () => {
    this.showNewForm = !this.showNewForm;
  };

  handleInsert = async () => {
    const result = await fetchAs<ScriptureReferenceCreateRequest, ScriptureReferenceCreateResponse>('table/common-scripture-references/create', {
      token: globals.globalStore.state.token,
      ...this.insertedFields,
    });

    this.showNewForm = false;
    this.insertedFields = this.defaultFields;

    if (result.error === ErrorType.NoError) {
      this.list = this.list.concat(result.response);
      this.message = `New Row with id ${result.response.id} inserted successfully`;
    } else {
      this.message = result.error;
    }
  };

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    this.updatedFields[columnName] = value;
    const result = await fetchAs<ScriptureReferenceUpdateRequest, ScriptureReferenceUpdateResponse>('table/common-scripture-references/update', {
      token: globals.globalStore.state.token,
      updatedFields: this.updatedFields,
      id,
    });

    if (result.error == ErrorType.NoError) {
      this.list = this.list.map(item => {
        if (item.id === id) {
          return result.response;
        }
        return item;
      });
      this.message = `New Row with id ${result.response.id} updated successfully`;
      return true;
    } else {
      this.message = result.error;
      return false;
    }
  };

  handleDelete = async (id: number): Promise<boolean> => {
    const result = await fetchAs<ScriptureReferenceDeleteRequest, ScriptureReferenceDeleteResponse>('table/common-scripture-references/delete', {
      id,
      token: globals.globalStore.state.token,
    });
    if (result.error === ErrorType.NoError) {
      this.list = this.list.filter(item => item.id !== id);
      this.message = `Row with id ${id} deleted successfully!`;
      return true;
    } else {
      this.message = result.error;
      return false;
    }
  };

  insertFieldChange(event, fieldName) {
    console.log('event.target.value', event.target.value);
    this.insertedFields[fieldName] = event.target.value;
  }

  getInputCell(fieldName) {
    if (this.nonEditableColumns.includes(fieldName)) {
      return <td>&nbsp;</td>;
    }
    return (
      <td>
        {fieldName === 'book_start' || fieldName === 'book_end' ? (
          <select onInput={event => this.insertFieldChange(event, fieldName)}>
            {this.bookNames.map(option => (
              <option value={option}>{option}</option>
            ))}
          </select>
        ) : (
          <input type="text" id={`input-${fieldName}`} name={fieldName} onInput={event => this.insertFieldChange(event, fieldName)} />
        )}
      </td>
    );
  }

  getEditableCell(columnName: string, item: ScriptureReference) {
    return (
      <td>
        <cf-cell
          key={columnName}
          rowId={item.id}
          propKey={columnName}
          type={columnName === 'book_start' || columnName === 'book_end' ? 'select' : 'text'}
          options={columnName === 'book_start' || columnName === 'book_end' ? this.bookNames : []}
          value={item[columnName]}
          isEditable={!this.nonEditableColumns.includes(columnName)}
          updateFn={!this.nonEditableColumns.includes(columnName) ? this.handleUpdate : null}
        ></cf-cell>
      </td>
    );
  }

  render() {
    return (
      <Host>
        <slot></slot>
        {this.message && <div>{this.message}</div>}
        <header>
          <h1>Scripture References</h1>
        </header>
        <main>
          <div id="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>*</th>
                  {Object.keys(this.defaultFields).map(key => (
                    <th>{key}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {this.list &&
                  this.list.map(item => (
                    <tr>
                      <div class="button-parent">
                        <button class="delete-button" onClick={() => this.handleDelete(item.id)}>
                          Delete
                        </button>
                      </div>
                      {Object.keys(item).map(key => this.getEditableCell(key, item))}
                    </tr>
                  ))}
              </tbody>
              {this.showNewForm && (
                <tr>
                  <td>&nbsp;</td>
                  {Object.keys(this.defaultFields).map(key => this.getInputCell(key))}
                </tr>
              )}
            </table>
          </div>
          <div id="button-group">
            {!this.showNewForm && (
              <button
                id="new-button"
                onClick={() => {
                  this.showNewForm = !this.showNewForm;
                }}
              >
                Create New Scripture Reference
              </button>
            )}

            {this.showNewForm && (
              <div>
                <button
                  id="cancel-button"
                  onClick={() => {
                    this.showNewForm = !this.showNewForm;
                  }}
                >
                  Cancel
                </button>
                <button id="submit-button" onClick={this.handleInsert}>
                  Submit
                </button>
              </div>
            )}
          </div>
        </main>
      </Host>
    );
  }
}

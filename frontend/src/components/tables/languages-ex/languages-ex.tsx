import { Component, Host, h, State } from '@stencil/core';
import { ErrorType, GenericResponse } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { languageEx } from '../../../common/types';
import './languages-ex.css';

type MutableLanguageExFields = Omit<languageEx, 'id' | 'createdAt' | 'createdBy' | 'modifiedAt' | 'modifiedBy'>;

class CreateLanguageExRequest {
  insertedFields: MutableLanguageExFields;
  token: string;
}
class CreateLanguageExResponse extends GenericResponse {
  data: languageEx;
}

class UpdateLanguageExRequest {
  token: string;
  updatedFields: MutableLanguageExFields;
  id: number;
}

class UpdateLanguageExResponse extends GenericResponse {
  data: languageEx;
}

class DeleteLanguageExRequest {
  id: number;
}

class DeleteLanguageExResponse extends GenericResponse {
  data: { id: number };
}

class ReadLanguageExResponse extends GenericResponse {
  data: languageEx[];
}

class ReadLanguageExRequest {
  token: string;
}

@Component({
  tag: 'languages-ex',
  styleUrl: 'languages-ex.css',
  shadow: true,
})
export class LanguagesEx {
  defaultFields = {
    id: null,
    created_at: null,
    created_by: null,
    modified_at: null,
    modified_by: null,
    lang_name: null,
    lang_code: null,
    location: null,
    first_lang_population: null,
    population: null,
    egids_level: null,
    egids_value: null,
    least_reached_progress_jps_scale: null,
    least_reached_value: null,
    partner_interest: null,
    partner_interest_description: null,
    partner_interest_source: null,
    multi_lang_leverage: null,
    multi_lang_leverage_description: null,
    multi_lang_leverage_source: null,
    community_interest: null,
    community_interest_description: null,
    community_interest_source: null,
    community_interest_value: null,
    community_interest_scripture_description: null,
    community_interest_scripture_source: null,
    lwc_scripture_access: null,
    lwc_scripture_description: null,
    lwc_scripture_source: null,
    access_to_begin: null,
    access_to_begin_description: null,
    access_to_begin_source: null,
    suggested_strategies: null,
    comments: null,
    prioritization: null,
    progress_bible: null,
  };
  @State() languagesEx: languageEx[] = [];
  @State() insertedFields: MutableLanguageExFields = this.defaultFields;
  @State() updatedFields: MutableLanguageExFields = {};
  @State() error: string;
  @State() success: string;
  @State() showNewForm = false;
  insertFieldChange(event, fieldName) {
    console.log(fieldName, event.target.value);
    this.insertedFields[fieldName] = event.target.value;
  }
  getInputCell(fieldName) {
    return (
      <td>
        <input type="text" id={`input-${fieldName}`} name={fieldName} onInput={event => this.insertFieldChange(event, fieldName)}></input>
      </td>
    );
  }
  getEditableCell(columnName: string, languageEx: languageEx) {
    return (
      <td
        contentEditable
        onInput={event => this.updateFieldChange(event, columnName)}
        // onKeyPress={this.disableNewlines}
        // onPaste={this.pasteAsPlainText}
        // onFocus={this.highlightAll}
      >
        {languageEx[columnName]}
      </td>
    );
  }
  updateFieldChange(event, columnName) {
    console.log(this.updatedFields[columnName], event.currentTarget.textContent);
    this.updatedFields[columnName] = event.currentTarget.textContent;
    // this.languagesEx = this.languagesEx.map(languageEx => (languageEx.id === id ? { ...languageEx, ...this.updatedFields } : languageEx));
  }
  handleUpdate = async id => {
    console.log(this.updatedFields);
    const result = await fetchAs<UpdateLanguageExRequest, UpdateLanguageExResponse>('language_ex/update', {
      updatedFields: this.updatedFields,
      token: globals.globalStore.state.token,
      id,
    });
    this.updatedFields = this.defaultFields;
    if (result.error === ErrorType.NoError) {
      this.languagesEx = this.languagesEx.map(languageEx => (languageEx.id === result.data.id ? result.data : languageEx));
      this.success = `Row with id ${result.data.id} updated successfully!`;
    } else {
      console.error('Failed to update row');
      this.error = result.error;
      alert(result.error);
    }
  };
  handleDelete = async id => {
    const result = await fetchAs<DeleteLanguageExRequest, DeleteLanguageExResponse>('language_ex/delete', {
      id,
    });
    if (result.error === ErrorType.NoError) {
      this.success = `Row with id ${result.data.id} deleted successfully!`;
      this.languagesEx = this.languagesEx.filter(globalRole => globalRole.id !== result.data.id);
    } else {
      this.error = result.error;
    }
  };

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();
    console.log(this.insertedFields);
    const result = await fetchAs<CreateLanguageExRequest, CreateLanguageExResponse>('language_ex/create', {
      insertedFields: this.insertedFields,
      token: globals.globalStore.state.token,
    });

    console.log(result);
    this.showNewForm = false;
    this.insertedFields = this.defaultFields;
    if (result.error === ErrorType.NoError) {
      this.languagesEx = this.languagesEx.concat(result.data);
      this.success = `New Row with id ${result.data.id} inserted successfully`;
    } else {
      console.error('Failed to create global role');
      this.error = result.error;
    }
  };

  componentWillLoad() {
    fetchAs<ReadLanguageExRequest, ReadLanguageExResponse>('language_ex/read', {
      token: globals.globalStore.state.token,
    }).then(res => {
      this.languagesEx = res.data.sort((a, b) => a.id - b.id);
    });
  }
  render() {
    return (
      <Host>
        <div>{this.error}</div>
        <header>
          <h1>Language Ex</h1>
        </header>
        {/* add flexbox to main -> create and update form should be to the side */}
        <main>
          {/* <form class="form insert-form">
            <div class="form-row">
              <label htmlFor="name" class="label insert-form__label">
                Name
              </label>
              <input type="text" value={this.insertedFields.lang_name} onInput={event => this.insertFieldChange(event, 'langName')} class="input insert-form__input" />
            </div>

            <div class="form form-row">
              <label htmlFor="org" class="label insert-form__label">
                Org
              </label>
              <input type="text" value={this.insertedFields.lang_code} onInput={event => this.insertFieldChange(event, 'langCode')} class="insert-form__input" />
            </div>

            <button onClick={this.handleInsert}>Submit</button>
          </form> */}
          <table>
            <thead>
              {/* this will be fixed -> on a shared component, this will be passed in and use Map to preserve order */}
              <tr>
                <th>id </th>
                <th>created_at</th>
                <th>created_by</th>
                <th>modified_at</th>
                <th>modified_by</th>
                <th>lang_name </th>
                <th>lang_code </th>
                <th>location </th>
                <th>first_lang_population </th>
                <th>population </th>
                <th>egids_level </th>
                <th>egids_value </th>
                <th>least_reached_progress_jps_scale </th>
                <th>least_reached_value </th>
                <th>partner_interest </th>
                <th>partner_interest_description </th>
                <th>partner_interest_source </th>
                <th>multi_lang_leverage </th>
                <th>multi_lang_leverage_description </th>
                <th>multi_lang_leverage_source </th>
                <th>community_interest </th>
                <th>community_interest_description </th>
                <th>community_interest_source </th>
                <th>community_interest_value </th>
                <th>community_interest_scripture_description </th>
                <th>community_interest_scripture_source </th>
                <th>lwc_scripture_access </th>
                <th>lwc_scripture_description </th>
                <th>lwc_scripture_source </th>
                <th>access_to_begin </th>
                <th>access_to_begin_description </th>
                <th>access_to_begin_source </th>
                <th>suggested_strategies </th>
                <th>comments </th>
                <th>prioritization </th>
                <th>progress_bible</th>
              </tr>
            </thead>
            <tbody>
              {this.languagesEx.map(languageEx => (
                <tr>
                  {/* can loop over these as well (using Map to preserve order) */}
                  <td>{languageEx.id}</td>
                  <td>{languageEx.created_at}</td>
                  <td>{languageEx.created_by}</td>
                  <td>{languageEx.modified_at}</td>
                  <td>{languageEx.modified_by}</td>
                  {this.getEditableCell('lang_name', languageEx)}
                  {this.getEditableCell('lang_code', languageEx)}
                  {this.getEditableCell('location', languageEx)}
                  {this.getEditableCell('first_lang_population', languageEx)}
                  {this.getEditableCell('population', languageEx)}
                  {this.getEditableCell('egids_level', languageEx)}
                  {this.getEditableCell('egids_value', languageEx)}
                  {this.getEditableCell('least_reached_progress_jps_scale', languageEx)}
                  {this.getEditableCell('least_reached_value', languageEx)}
                  {this.getEditableCell('partner_interest', languageEx)}
                  {this.getEditableCell('partner_interest_description', languageEx)}
                  {this.getEditableCell('partner_interest_source', languageEx)}
                  {this.getEditableCell('multi_lang_leverage', languageEx)}
                  {this.getEditableCell('multi_lang_leverage_description', languageEx)}
                  {this.getEditableCell('multi_lang_leverage_source', languageEx)}
                  {this.getEditableCell('community_interest', languageEx)}
                  {this.getEditableCell('community_interest_description', languageEx)}
                  {this.getEditableCell('community_interest_source', languageEx)}
                  {this.getEditableCell('community_interest_value', languageEx)}
                  {this.getEditableCell('community_interest_scripture_description', languageEx)}
                  {this.getEditableCell('community_interest_scripture_source', languageEx)}
                  {this.getEditableCell('lwc_scripture_access', languageEx)}
                  {this.getEditableCell('lwc_scripture_description', languageEx)}
                  {this.getEditableCell('lwc_scripture_source', languageEx)}
                  {this.getEditableCell('access_to_begin', languageEx)}
                  {this.getEditableCell('access_to_begin_description', languageEx)}
                  {this.getEditableCell('access_to_begin_source', languageEx)}
                  {this.getEditableCell('suggested_strategies', languageEx)}
                  {this.getEditableCell('comments', languageEx)}
                  {this.getEditableCell('prioritization', languageEx)}
                  {this.getEditableCell('progress_bible', languageEx)}
                  <button onClick={() => this.handleUpdate(languageEx.id)}>Update</button>
                  <button onClick={() => this.handleDelete(languageEx.id)}>Delete</button>
                </tr>
              ))}
            </tbody>
            {this.showNewForm && (
              <tr>
                <td class="disabled">&nbsp;</td>
                <td class="disabled">&nbsp;</td>
                <td class="disabled">&nbsp;</td>
                <td class="disabled">&nbsp;</td>
                <td class="disabled">&nbsp;</td>
                {this.getInputCell('lang_name')}
                {this.getInputCell('lang_code')}
                {this.getInputCell('location')}
                {this.getInputCell('first_lang_population')}
                {this.getInputCell('population')}
                {this.getInputCell('egids_level')}
                {this.getInputCell('egids_value')}
                {this.getInputCell('least_reached_progress_jps_scale')}
                {this.getInputCell('least_reached_value')}
                {this.getInputCell('partner_interest')}
                {this.getInputCell('partner_interest_description')}
                {this.getInputCell('partner_interest_source')}
                {this.getInputCell('multi_lang_leverage')}
                {this.getInputCell('multi_lang_leverage_description')}
                {this.getInputCell('multi_lang_leverage_source')}
                {this.getInputCell('community_interest')}
                {this.getInputCell('community_interest_description')}
                {this.getInputCell('community_interest_source')}
                {this.getInputCell('community_interest_value')}
                {this.getInputCell('community_interest_scripture_description')}
                {this.getInputCell('community_interest_scripture_source')}
                {this.getInputCell('lwc_scripture_access')}
                {this.getInputCell('lwc_scripture_description')}
                {this.getInputCell('lwc_scripture_source')}
                {this.getInputCell('access_to_begin')}
                {this.getInputCell('access_to_begin_description')}
                {this.getInputCell('access_to_begin_source')}
                {this.getInputCell('suggested_strategies')}
                {this.getInputCell('comments')}
                {this.getInputCell('prioritization')}
                {this.getInputCell('progress_bible')}
              </tr>
            )}
          </table>
          <div id="button-group">
            {!this.showNewForm && (
              <button
                id="new-button"
                onClick={() => {
                  this.showNewForm = !this.showNewForm;
                }}
              >
                Create New Language Ex
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

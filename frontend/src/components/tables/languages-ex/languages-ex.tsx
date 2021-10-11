import { Component, Host, h, State } from '@stencil/core';
import { ErrorType, GenericResponse } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { languageEx } from '../../../common/types';
import './languages-ex.css';

type MutableLanguageExFields = Omit<
  languageEx,
  | 'id'
  | 'createdAt'
  | 'createdBy'
  | 'modifiedAt'
  | 'modifiedBy'
  | 'egids_value'
  | 'least_reached_value'
  | 'partner_interest_value'
  | 'multiple_languages_leverage_linguistic_value'
  | 'multiple_languages_leverage_joint_training_value'
  | 'lang_comm_int_in_language_development_value'
  | 'lang_comm_int_in_scripture_translation_value'
  | 'access_to_scripture_in_lwc_value'
  | 'begin_work_geo_challenges_value'
  | 'begin_work_rel_pol_obstacles_value'
  | 'prioritization'
>;

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
  token: string;
}

class DeleteLanguageExResponse extends GenericResponse {
  id: number;
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
    language_name: null,
    iso: null,
    prioritization: null,
    progress_bible: null,
    island: null,
    province: null,
    first_language_population: null,
    population_value: null,
    egids_level: null,
    egids_value: null,
    least_reached_progress_jps_level: null,
    least_reached_value: null,
    partner_interest_level: null,
    partner_interest_value: null,
    partner_interest_description: null,
    partner_interest_source: null,
    multiple_languages_leverage_linguistic_level: null,
    multiple_languages_leverage_linguistic_value: null,
    multiple_languages_leverage_linguistic_description: null,
    multiple_languages_leverage_linguistic_source: null,
    multiple_languages_leverage_joint_training_level: null,
    multiple_languages_leverage_joint_training_value: null,
    multiple_languages_leverage_joint_training_description: null,
    multiple_languages_leverage_joint_training_source: null,
    lang_comm_int_in_language_development_level: null,
    lang_comm_int_in_language_development_value: null,
    lang_comm_int_in_language_development_description: null,
    lang_comm_int_in_language_development_source: null,
    lang_comm_int_in_scripture_translation_level: null,
    lang_comm_int_in_scripture_translation_value: null,
    lang_comm_int_in_scripture_translation_description: null,
    lang_comm_int_in_scripture_translation_source: null,
    access_to_scripture_in_lwc_level: null,
    access_to_scripture_in_lwc_value: null,
    access_to_scripture_in_lwc_description: null,
    access_to_scripture_in_lwc_source: null,
    begin_work_geo_challenges_level: null,
    begin_work_geo_challenges_value: null,
    begin_work_geo_challenges_description: null,
    begin_work_geo_challenges_source: null,
    begin_work_rel_pol_obstacles_scale: null,
    begin_work_rel_pol_obstacles_value: null,
    begin_work_rel_pol_obstacles_description: null,
    begin_work_rel_pol_obstacles_source: null,
    suggested_strategies: null,
    comments: null,
    created_at: null,
    created_by: null,
    modified_at: null,
    modified_by: null,
    owning_person: null,
    owning_group: null,
  };
  nonEditableColumns = [
    'id',
    'modified_at',
    'created_at',
    'created_by',
    'modified_by',
    'egids_value',
    'least_reached_value',
    'partner_interest_value',
    'multiple_languages_leverage_linguistic_value',
    'multiple_languages_leverage_joint_training_value',
    'lang_comm_int_in_language_development_value',
    'lang_comm_int_in_scripture_translation_value',
    'access_to_scripture_in_lwc_value',
    'begin_work_geo_challenges_value',
    'begin_work_rel_pol_obstacles_value',
    'prioritization',
  ];
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
    if (this.nonEditableColumns.includes(fieldName)) {
      return <td>&nbsp;</td>;
    }
    return (
      <td>
        <input type="text" id={`input-${fieldName}`} name={fieldName} onInput={event => this.insertFieldChange(event, fieldName)}></input>
      </td>
    );
  }
  getEditableCell(columnName: string, languageEx: languageEx) {
    return (
      <td
      // onKeyPress={this.disableNewlines}
      // onPaste={this.pasteAsPlainText}
      // onFocus={this.highlightAll}
      >
        <cf-cell
          key={columnName}
          rowId={languageEx.id}
          propKey={columnName}
          value={languageEx[columnName]}
          isEditable={!this.nonEditableColumns.includes(columnName)}
          updateFn={!this.nonEditableColumns.includes(columnName) ? this.handleUpdate : null}
        ></cf-cell>
      </td>
    );
  }

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    this.updatedFields[columnName] = value;
    const updateResponse = await fetchAs<UpdateLanguageExRequest, UpdateLanguageExResponse>('language_ex/update', {
      token: globals.globalStore.state.token,
      updatedFields: this.updatedFields,
      id,
    });

    if (updateResponse.error == ErrorType.NoError) {
      const result = await fetchAs<ReadLanguageExRequest, ReadLanguageExResponse>('language_ex/read', { token: globals.globalStore.state.token });
      this.languagesEx = result.data.sort((a, b) => a.id - b.id);
      return true;
    } else {
      alert(updateResponse.error);
    }
  };

  handleDelete = async id => {
    const result = await fetchAs<DeleteLanguageExRequest, DeleteLanguageExResponse>('language_ex/delete', {
      id,
      token: globals.globalStore.state.token,
    });
    if (result.error === ErrorType.NoError) {
      this.success = `Row with id ${result.id} deleted successfully!`;
      this.languagesEx = this.languagesEx.filter(globalRole => globalRole.id !== result.id);
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
          <div id="table-wrap">
            <table>
              <thead>
                {/* this will be fixed -> on a shared component, this will be passed in and use Map to preserve order */}
                <tr>
                  <th>*</th>
                  {Object.keys(this.defaultFields).map(key => (
                    <th>{key}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {this.languagesEx.map(languageEx => (
                  <tr>
                    <div class="button-parent">
                      <button class="delete-button" onClick={() => this.handleDelete(languageEx.id)}>
                        Delete
                      </button>
                    </div>
                    {Object.keys(languageEx).map(key => this.getEditableCell(key, languageEx))}
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

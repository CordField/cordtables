import { Component, Host, h, State } from '@stencil/core';
import { ErrorType, GenericResponse } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { LanguageEx } from '../../../common/types';
import './languages-ex.css';

type MutableLanguageExFields = Omit<
  LanguageEx,
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
  data: LanguageEx;
}

class UpdateLanguageExRequest {
  token: string;
  columnToUpdate: string;
  updatedColumnValue: string | number;
  id: number;
}

class UpdateLanguageExResponse extends GenericResponse {
  data: LanguageEx;
}

class DeleteLanguageExRequest {
  id: number;
  token: string;
}

class DeleteLanguageExResponse extends GenericResponse {
  id: number;
}

class ReadLanguageExResponse extends GenericResponse {
  data: LanguageEx[];
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
    begin_work_rel_pol_obstacles_level: null,
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
  columnDropdownOptions = {
    progress_bible: [`true`, `false`],
    egids_level: [`0`, `1`, `2`, `3`, `4`, `5`, `6a`, `6b`, `7`, `8a`, `8b`, `9`, `10`],
    least_reached_progress_jps_level: [`0`, `1`, `2`, `3`, `4`, `5`],
    partner_interest_level: [`No Partner Interest`, `Some`, `Significant`, `Considerable`],
    multiple_languages_leverage_linguistic_level: [`None`, `Some`, `Significant`, `Considerable`, `Large`, `Vast`],
    multiple_languages_leverage_joint_training_level: [`None`, `Some`, `Significant`, `Considerable`, `Large`, `Vast`],
    lang_comm_int_in_language_development_level: [`No Interest`, `Some`, `Expressed Need`, `Significant`, `Considerable`],
    lang_comm_int_in_scripture_translation_level: [`No Interest`, `Some`, `Expressed Need`, `Significant`, `Considerable`],
    access_to_scripture_in_lwc_level: [`Full Access`, `Vast Majority`, `Large Majority`, `Majority`, `Significant`, `Some`, `Few`],
    begin_work_geo_challenges_level: [`None`, `Very Difficult`, `Difficult`, `Moderate`, `Easy`],
    begin_work_rel_pol_obstacles_level: [`None`, `Very Difficult`, `Difficult`, `Moderate`, `Easy`],
  };
  @State() languagesEx: LanguageEx[] = [];
  @State() insertedFields: MutableLanguageExFields = this.defaultFields;
  @State() error: string;
  @State() success: string;
  @State() showNewForm = false;
  insertFieldChange(event, fieldName) {
    console.log(fieldName, event.target.value);
    this.insertedFields[fieldName] = event.target.value;
  }
  selectFieldChange(event, fieldName) {
    console.log(fieldName, event.target.value);
    this.insertedFields[fieldName] = event.target.value;
  }

  getSelectCell(fieldName) {
    return (
      <select onInput={event => this.selectFieldChange(event, fieldName)}>
        {['-', ...this.columnDropdownOptions[fieldName]].map(option => (
          <option value={option !== '-' ? option : ''} selected={option !== '-' ? this.insertedFields[fieldName] === option : this.insertedFields[fieldName] === ''}>
            {option}
          </option>
        ))}
      </select>
    );
  }

  getInputCell(fieldName) {
    return (
      <td>
        <input type="text" id={`input-${fieldName}`} name={fieldName} onInput={event => this.insertFieldChange(event, fieldName)}></input>
      </td>
    );
  }
  getEditableCell(columnName: string, LanguageEx: LanguageEx) {
    return (
      <td
      // onKeyPress={this.disableNewlines}
      // onPaste={this.pasteAsPlainText}
      // onFocus={this.highlightAll}
      >
        <cf-cell
          key={columnName}
          rowId={LanguageEx.id}
          propKey={columnName}
          value={typeof LanguageEx[columnName] === 'string' ? LanguageEx[columnName] : LanguageEx[columnName]?.toString()}
          type={this.columnDropdownOptions.hasOwnProperty(columnName) ? 'select' : 'text'}
          options={this.columnDropdownOptions.hasOwnProperty(columnName) ? this.columnDropdownOptions[columnName] : []}
          isEditable={!this.nonEditableColumns.includes(columnName)}
          updateFn={!this.nonEditableColumns.includes(columnName) ? this.handleUpdate : null}
        ></cf-cell>
      </td>
    );
  }

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<UpdateLanguageExRequest, UpdateLanguageExResponse>('sc-languages/update', {
      token: globals.globalStore.state.token,
      updatedColumnValue: value,
      columnToUpdate: columnName,
      id,
    });

    if (updateResponse.error == ErrorType.NoError) {
      const result = await fetchAs<ReadLanguageExRequest, ReadLanguageExResponse>('sc-languages/read', { token: globals.globalStore.state.token });
      this.languagesEx = result.data?.sort((a, b) => a.id - b.id);
      return true;
    } else {
      alert(updateResponse.error);
    }
  };

  handleDelete = async id => {
    const result = await fetchAs<DeleteLanguageExRequest, DeleteLanguageExResponse>('sc-languages/delete', {
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
    const result = await fetchAs<CreateLanguageExRequest, CreateLanguageExResponse>('sc-languages/create-read', {
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
    fetchAs<ReadLanguageExRequest, ReadLanguageExResponse>('sc-languages/read', {
      token: globals.globalStore.state.token,
    }).then(res => {
      this.languagesEx = res.data?.sort((a, b) => a.id - b.id);
    });
  }

  render() {
    return (
      <Host>
        <div>{this.error}</div>
        <header>
          <h1>Language Ex</h1>
        </header>

        <main>
          <div id="table-wrap">
            <table>
              <thead>
                <tr>
                  {globals.globalStore.state.editMode && <th>*</th>}
                  {Object.keys(this.defaultFields).map(key => (
                    <th>{key.replaceAll('_', ' ')}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {this.languagesEx.map(LanguageEx => (
                  <tr>
                    {globals.globalStore.state.editMode && (
                      <div class="button-parent">
                        <button class="delete-button" onClick={() => this.handleDelete(LanguageEx.id)}>
                          Delete
                        </button>
                      </div>
                    )}
                    {Object.keys(LanguageEx).map(key => this.getEditableCell(key, LanguageEx))}
                  </tr>
                ))}
              </tbody>
              {this.showNewForm && (
                <tr>
                  {globals.globalStore.state.editMode && <td>&nbsp;</td>}

                  {Object.keys(this.defaultFields).map(key => {
                    if (this.nonEditableColumns.includes(key)) {
                      return <td>&nbsp;</td>;
                    } else if (this.columnDropdownOptions.hasOwnProperty(key)) {
                      return this.getSelectCell(key);
                    } else {
                      return this.getInputCell(key);
                    }
                  })}
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
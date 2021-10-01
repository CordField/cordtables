import { Component, Host, h, State } from '@stencil/core';
import { ErrorType, GenericResponse, globalRole } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { languageEx } from '../../../common/types';

type MutableLanguageExFields = Omit<languageEx, 'id' | 'createdAt' | 'createdBy' | 'modifiedAt' | 'modifiedBy'>;

class CreateLanguageExRequest {
  insertedFields: MutableLanguageExFields;
  email: string;
}
class CreateLanguageExResponse extends GenericResponse {
  data: languageEx;
}

class UpdateLanguageExRequest {
  email: string;
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

@Component({
  tag: 'languages-ex',
  styleUrl: 'languages-ex.css',
  shadow: true,
})
export class LanguagesEx {
  defaultFields = {
    langName: null,
    langCode: null,
    location: null,
    firstLangPopulation: null,
    population: null,
    egidsLevel: null,
    egidsValue: null,
    leastReachedProgressJpsScale: null,
    leastReachedValue: null,
    partnerInterest: null,
    partnerInterestDescription: null,
    partnerInterestSource: null,
    multiLangLeverage: null,
    multiLangLeverageDescription: null,
    multiLangLeverageSource: null,
    communityInterest: null,
    communityInterestDescription: null,
    communityInterestSource: null,
    communityInterestValue: null,
    communityInterestScriptureDescription: null,
    communityInterestScriptureSource: null,
    lwcScriptureAccess: null,
    lwcScriptureDescription: null,
    lwcScriptureSource: null,
    accessToBegin: null,
    accessToBeginDescription: null,
    accessToBeginSource: null,
    suggestedStrategies: null,
    comments: null,
    prioritization: null,
    progressBible: null,
  };
  @State() languagesEx: languageEx[] = [];
  @State() insertedFields: MutableLanguageExFields = this.defaultFields;
  @State() updatedFields: MutableLanguageExFields = this.defaultFields;
  @State() error: string;
  @State() success: string;
  insertFieldChange(event, fieldName) {
    this.insertedFields[fieldName] = event.target.value;
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
  }
  handleUpdate = async id => {
    console.log(this.updatedFields);
    const result = await fetchAs<UpdateLanguageExRequest, UpdateLanguageExResponse>('language_ex/update', {
      updatedFields: this.updatedFields,
      email: globals.globalStore.state.email,
      id,
    });
    if (result.error === ErrorType.NoError) {
      this.updatedFields = this.defaultFields;
      this.languagesEx = this.languagesEx.map(globalRole => (globalRole.id === result.data.id ? result.data : globalRole));
      this.success = `Row with id ${result.data.id} updated successfully!`;
    } else {
      console.error('Failed to update row');
      this.error = result.error;
      this.languagesEx = this.languagesEx.map(globalRole => (globalRole.id === result.data?.id ? result.data : globalRole));
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
    const result = await fetchAs<CreateLanguageExRequest, CreateLanguageExResponse>('language_ex/create', {
      insertedFields: this.insertedFields,
      email: globals.globalStore.state.email,
    });

    console.log(result);

    if (result.error === ErrorType.NoError) {
      this.insertedFields = this.defaultFields;
      this.languagesEx = this.languagesEx.concat(result.data);
      this.success = `New Row with id ${result.data.id} inserted successfully`;
    } else {
      console.error('Failed to create global role');
      this.error = result.error;
    }
  };

  componentWillLoad() {
    fetchAs<null, ReadLanguageExResponse>('language_ex/read', null).then(res => {
      this.languagesEx = res.data;
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
          <form class="form insert-form">
            <div class="form-row">
              <label htmlFor="name" class="label insert-form__label">
                Name
              </label>
              <input type="text" value={this.insertedFields.langName} onInput={event => this.insertFieldChange(event, 'langName')} class="input insert-form__input" />
            </div>

            <div class="form form-row">
              <label htmlFor="org" class="label insert-form__label">
                Org
              </label>
              <input type="text" value={this.insertedFields.langCode} onInput={event => this.insertFieldChange(event, 'langCode')} class="insert-form__input" />
            </div>

            <button onClick={this.handleInsert}>Submit</button>
          </form>
          <table>
            <thead>
              {/* this will be fixed -> on a shared component, this will be passed in and use Map to preserve order */}
              <tr>
                <th>id </th>
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
                  <td>{languageEx.createdAt}</td>
                  <td>{languageEx.createdBy}</td>
                  <td>{languageEx.modifiedAt}</td>
                  <td>{languageEx.modifiedBy}</td>
                  {this.getEditableCell('langName', languageEx)}
                  {this.getEditableCell('langCode', languageEx)}
                  {this.getEditableCell('location', languageEx)}
                  {this.getEditableCell('firstLangPopulation', languageEx)}
                  {this.getEditableCell('population', languageEx)}
                  {this.getEditableCell('egidsLevel', languageEx)}
                  {this.getEditableCell('egidsValue', languageEx)}
                  {this.getEditableCell('leastReachedProgressJpsScale', languageEx)}
                  {this.getEditableCell('leastReachedValue', languageEx)}
                  {this.getEditableCell('partnerInterest', languageEx)}
                  {this.getEditableCell('partnerInterestDescription', languageEx)}
                  {this.getEditableCell('partnerInterestSource', languageEx)}
                  {this.getEditableCell('multiLangLeverage', languageEx)}
                  {this.getEditableCell('multiLangLeverageDescription', languageEx)}
                  {this.getEditableCell('multiLangLeverageSource', languageEx)}
                  {this.getEditableCell('communityInterest', languageEx)}
                  {this.getEditableCell('communityInterestDescription', languageEx)}
                  {this.getEditableCell('communityInterestSource', languageEx)}
                  {this.getEditableCell('communityInterestValue', languageEx)}
                  {this.getEditableCell('communityInterestScriptureDescription', languageEx)}
                  {this.getEditableCell('communityInterestScriptureSource', languageEx)}
                  {this.getEditableCell('lwcScriptureAccess', languageEx)}
                  {this.getEditableCell('lwcScriptureDescription', languageEx)}
                  {this.getEditableCell('lwcScriptureSource', languageEx)}
                  {this.getEditableCell('accessToBegin', languageEx)}
                  {this.getEditableCell('accessToBeginDescription', languageEx)}
                  {this.getEditableCell('accessToBeginSource', languageEx)}
                  {this.getEditableCell('suggestedStrategies', languageEx)}
                  {this.getEditableCell('comments', languageEx)}
                  {this.getEditableCell('prioritization', languageEx)}
                  {this.getEditableCell('progressBible', languageEx)}

                  <button onClick={() => this.handleUpdate(languageEx.id)}>Update</button>
                  <button onClick={() => this.handleDelete(languageEx.id)}>Delete</button>
                </tr>
              ))}
            </tbody>
          </table>
        </main>
      </Host>
    );
  }
}

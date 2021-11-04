import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateLanguageExRequest {
  token: string;
  language: {
    name: string;
    display_name: string;
  };
}
class CreateLanguageExResponse extends GenericResponse {
  langauge: ScLanguage;
}

class ScLanguagesListRequest {
  token: string;
}

class ScLanguagesListResponse {
  error: ErrorType;
  languages: ScLanguage[];
}

class ScLanguagesUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class ScLanguageUpdateResponse {
  error: ErrorType;
  language: ScLanguage | null = null;
}

class DeleteLanguageExRequest {
  id: number;
  token: string;
}

class DeleteLanguageExResponse extends GenericResponse {
  id: number;
}
@Component({
  tag: 'sc-languages',
  styleUrl: 'sc-languages.css',
  shadow: true,
})
export class ScLanguages {
  @State() languagesResponse: ScLanguagesListResponse;
  newLanguageName: string;
  newDisplayName: string;

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScLanguagesUpdateRequest, ScLanguageUpdateResponse>('sc-languages/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.languagesResponse = { error: ErrorType.NoError, languages: this.languagesResponse.languages.map(language => (language.id === id ? updateResponse.language : language)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteLanguageExRequest, DeleteLanguageExResponse>('sc-languages/delete', {
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
    this.languagesResponse = await fetchAs<ScLanguagesListRequest, ScLanguagesListResponse>('sc-languages/list', {
      token: globals.globalStore.state.token,
    });
  }

  languageNameChange(event) {
    this.newLanguageName = event.target.value;
  }

  displayNameChange(event) {
    this.newDisplayName = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateLanguageExRequest, CreateLanguageExResponse>('sc-languages/create-read', {
      token: globals.globalStore.state.token,
      language: {
        name: this.newLanguageName,
        display_name: this.newDisplayName,
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
      field: 'name',
      displayName: 'Language Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },

    {
      field: 'coordinates',
      displayName: 'Coordinates',
      width: 400,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'prioritization',
      displayName: 'Prioritization',
      width: 100,
      editable: false,
    },
    {
      field: 'ethnologue',
      displayName: 'Ethnologue',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'display_name',
      displayName: 'Display Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'display_name_pronunciation',
      displayName: 'Display Name Pronunciation',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'progress_bible',
      displayName: 'On progress.Bible list',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `true`, value: true },
        { display: 'false', value: false },
      ],
    },
    {
      field: 'tags',
      displayName: 'Tags',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'preset_inventory',
      displayName: 'Preset Inventory',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `true`, value: true },
        { display: 'false', value: false },
      ],
    },
    {
      field: 'is_sign_language',
      displayName: 'Sign Language?',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `true`, value: true },
        { display: 'false', value: false },
      ],
    },
    {
      field: 'is_least_of_these',
      displayName: 'Least of These?',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `true`, value: true },
        { display: 'false', value: false },
      ],
    },
    {
      field: 'least_of_these_reason',
      displayName: 'Least of These reason',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'population_override',
      displayName: 'Population Override',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'registry_of_dialects_code',
      displayName: 'Registry of Dialects Code',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'sensitivity',
      displayName: 'Sensitivity',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `Low`, value: 'Low' },
        { display: 'Medium', value: 'Medium' },
        { display: 'High', value: 'High' },
      ],
    },
    {
      field: 'sign_language_code',
      displayName: 'Sign Language Code',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'sponsor_estimated_end_date',
      displayName: 'Estimated Date for the Sponsor to Engage',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },

    {
      field: 'island',
      displayName: 'Island',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'province',
      displayName: 'Province',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'first_language_population',
      displayName: 'First Language Population',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'population_value',
      displayName: 'Value',
      width: 50,
      editable: false,
    },
    {
      field: 'egids_level',
      displayName: 'EGIDS Level',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `0`, value: '0' },
        { display: `1`, value: '1' },
        { display: `2`, value: '2' },
        { display: `3`, value: '3' },
        { display: `4`, value: '4' },
        { display: `5`, value: '5' },
        { display: `6a`, value: '6a' },
        { display: `6b`, value: '6b' },
        { display: `7`, value: '7' },
        { display: `8a`, value: '8a' },
        { display: `8b`, value: '8b' },
        { display: `9`, value: '9' },
        { display: `10`, value: '10' },
      ],
    },
    {
      field: 'egids_value',
      displayName: 'Value',
      width: 50,
      editable: false,
    },
    {
      field: 'least_reached_progress_jps_level',
      displayName: 'Least Reached Progress JPS',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `1`, value: '1' },
        { display: `2`, value: '2' },
        { display: `3`, value: '3' },
        { display: `4`, value: '4' },
        { display: `5`, value: '5' },
      ],
    },
    {
      field: 'least_reached_value',
      displayName: 'Value',
      width: 50,
      editable: false,
    },
    {
      field: 'partner_interest_level',
      displayName: 'Partner Interest',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `No Partner Interest`, value: 'NoPartnerInterest' },
        { display: `Some`, value: 'Some' },
        { display: `Significant`, value: 'Significant' },
        { display: `Considerable`, value: 'Considerable' },
      ],
    },
    {
      field: 'partner_interest_value',
      displayName: 'Value',
      width: 50,
      editable: false,
    },
    {
      field: 'partner_interest_description',
      displayName: 'Description',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'partner_interest_source',
      displayName: 'Source',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'multiple_languages_leverage_linguistic_level',
      displayName: 'Leverage from using multiple languages at the linguistic',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `None`, value: 'None' },
        { display: `Some`, value: 'Some' },
        { display: `Significant`, value: 'Significant' },
        { display: `Considerable`, value: 'Considerable' },
        { display: `Large`, value: 'Large' },
        { display: `Vast`, value: 'Vast' },
      ],
    },
    {
      field: 'multiple_languages_leverage_linguistic_value',
      displayName: 'Value',
      width: 50,
      editable: false,
    },
    {
      field: 'multiple_languages_leverage_linguistic_description',
      displayName: 'Description',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'multiple_languages_leverage_linguistic_source',
      displayName: 'Source',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'multiple_languages_leverage_joint_training_level',
      displayName: 'Leverage from joint training',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `None`, value: 'None' },
        { display: `Some`, value: 'Some' },
        { display: `Significant`, value: 'Significant' },
        { display: `Considerable`, value: 'Considerable' },
        { display: `Large`, value: 'Large' },
        { display: `Vast`, value: 'Vast' },
      ],
    },
    {
      field: 'multiple_languages_leverage_joint_training_value',
      displayName: 'Value',
      width: 50,
      editable: false,
    },
    {
      field: 'multiple_languages_leverage_joint_training_description',
      displayName: 'Description',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'multiple_languages_leverage_joint_training_source',
      displayName: 'Source',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'lang_comm_int_in_language_development_level',
      displayName: 'Language community interest in language development',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `No Interest`, value: 'NoInterest' },
        { display: `Some`, value: 'Some' },
        { display: `Significant`, value: 'Significant' },
        { display: `Considerable`, value: 'Considerable' },
      ],
    },
    {
      field: 'lang_comm_int_in_language_development_value',
      displayName: 'Value',
      width: 50,
      editable: false,
    },
    {
      field: 'lang_comm_int_in_language_development_description',
      displayName: 'Description',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'lang_comm_int_in_language_development_source',
      displayName: 'Source',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'lang_comm_int_in_scripture_translation_level',
      displayName: 'Language community interest in scripture translation',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `No Interest`, value: 'NoInterest' },
        { display: `Some`, value: 'Some' },
        { display: `Expressed Need`, value: 'ExpressedNeed' },
        { display: `Significant`, value: 'Significant' },
        { display: `Considerable`, value: 'Considerable' },
      ],
    },
    {
      field: 'lang_comm_int_in_scripture_translation_value',
      displayName: 'Value',
      width: 50,
      editable: false,
    },
    {
      field: 'lang_comm_int_in_scripture_translation_description',
      displayName: 'Description',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'lang_comm_int_in_scripture_translation_source',
      displayName: 'Source',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'access_to_scripture_in_lwc_level',
      displayName: 'Access to scripture in a LWC',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `Full Access`, value: 'FullAccess' },
        { display: `Vast Majority`, value: 'VastMajority' },
        { display: `Large Majority`, value: 'LargeMajority' },
        { display: `Majority`, value: 'Majority' },
        { display: `Significant`, value: 'Significant' },
        { display: `Some`, value: 'Some' },
        { display: `Few`, value: 'Few' },
      ],
    },
    {
      field: 'access_to_scripture_in_lwc_value',
      displayName: 'Value',
      width: 50,
      editable: false,
    },
    {
      field: 'access_to_scripture_in_lwc_description',
      displayName: 'Description',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'access_to_scripture_in_lwc_source',
      displayName: 'Source',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'begin_work_geo_challenges_level',
      displayName: 'Geographical challenges to begin work',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `None`, value: 'None' },
        { display: `Very Difficult`, value: 'VeryDifficult' },
        { display: `Difficult`, value: 'Difficult' },
        { display: `Moderate`, value: 'Moderate' },
        { display: `Easy`, value: 'Easy' },
      ],
    },
    {
      field: 'begin_work_geo_challenges_value',
      displayName: 'Value',
      width: 50,
      editable: false,
    },
    {
      field: 'begin_work_geo_challenges_description',
      displayName: 'Description',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'begin_work_geo_challenges_source',
      displayName: 'Source',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'begin_work_rel_pol_obstacles_level',
      displayName: 'Religious and political obstacles to begin work',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
      selectOptions: [
        { display: `-`, value: '' },
        { display: `None`, value: 'None' },
        { display: `Very Difficult`, value: 'VeryDifficult' },
        { display: `Difficult`, value: 'Difficult' },
        { display: `Moderate`, value: 'Moderate' },
        { display: `Easy`, value: 'Easy' },
      ],
    },
    {
      field: 'begin_work_rel_pol_obstacles_value',
      displayName: 'Value',
      width: 50,
      editable: false,
    },
    {
      field: 'begin_work_rel_pol_obstacles_description',
      displayName: 'Description',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'begin_work_rel_pol_obstacles_source',
      displayName: 'Source',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'suggested_strategies',
      displayName: 'Suggested Strategies',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'comments',
      displayName: 'Comments',
      width: 150,
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
  }

  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.languagesResponse && <cf-table rowData={this.languagesResponse.languages} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="language-name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="language-name">New Language Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="language-name" name="language-name" onInput={event => this.languageNameChange(event)} />
              </span>
            </div>
            <div id="display-name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="display-name">Display Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="display-name" name="display-name" onInput={event => this.displayNameChange(event)} />
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

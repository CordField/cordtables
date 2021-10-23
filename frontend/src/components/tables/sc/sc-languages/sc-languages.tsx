import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';

class ScLanguagesListRequest {
  token: string;
}

class ScLanguagesListResponse {
  error: ErrorType;
  languages: ScLanguage[];
}
@Component({
  tag: 'sc-languages',
  styleUrl: 'sc-languages.css',
  shadow: true,
})
export class ScLanguages {
  @State() languagesResponse: ScLanguagesListResponse;

  columnData: ColumnDescription[] = [
    {
      field: 'id',
      displayName: 'ID',
      width: 50,
      editable: false,
    },
    {
      field: 'language_name',
      displayName: 'Language Name',
      width: 200,
      editable: true,
    },
    {
      field: 'iso',
      displayName: 'ISO code',
      width: 50,
      editable: false,
    },
    {
      field: 'prioritization',
      displayName: 'Prioritization',
      width: 100,
      editable: false,
    },
    {
      field: 'progress_bible',
      displayName: 'On progress.Bible list',
      width: 150,
      editable: false,
    },
    {
      field: 'island',
      displayName: 'Island',
      width: 150,
      editable: false,
    },
    {
      field: 'province',
      displayName: 'Province',
      width: 150,
      editable: false,
    },
    {
      field: 'first_language_population',
      displayName: 'First Language Population',
      width: 150,
      editable: false,
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
      editable: false,
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
      editable: false,
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
      editable: false,
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
      editable: false,
    },
    {
      field: 'partner_interest_source',
      displayName: 'Source',
      width: 150,
      editable: false,
    },
    {
      field: 'multiple_languages_leverage_linguistic_level',
      displayName: 'Leverage from using multiple languages at the linguistic',
      width: 150,
      editable: false,
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
      editable: false,
    },
    {
      field: 'multiple_languages_leverage_linguistic_source',
      displayName: 'Source',
      width: 150,
      editable: false,
    },
    {
      field: 'multiple_languages_leverage_joint_training_level',
      displayName: 'Leverage from joint training',
      width: 150,
      editable: false,
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
      editable: false,
    },
    {
      field: 'multiple_languages_leverage_joint_training_source',
      displayName: 'Source',
      width: 150,
      editable: false,
    },
    {
      field: 'lang_comm_int_in_language_development_level',
      displayName: 'Language community interest in language development',
      width: 150,
      editable: false,
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
      editable: false,
    },
    {
      field: 'lang_comm_int_in_language_development_source',
      displayName: 'Source',
      width: 150,
      editable: false,
    },
    {
      field: 'lang_comm_int_in_scripture_translation_level',
      displayName: 'Language community interest in scripture translation',
      width: 150,
      editable: false,
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
      editable: false,
    },
    {
      field: 'lang_comm_int_in_scripture_translation_source',
      displayName: 'Source',
      width: 150,
      editable: false,
    },
    {
      field: 'access_to_scripture_in_lwc_level',
      displayName: 'Access to scripture in a LWC',
      width: 150,
      editable: false,
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
      editable: false,
    },
    {
      field: 'access_to_scripture_in_lwc_source',
      displayName: 'Source',
      width: 150,
      editable: false,
    },
    {
      field: 'begin_work_geo_challenges_level',
      displayName: 'Geographical challenges to begin work',
      width: 150,
      editable: false,
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
      editable: false,
    },
    {
      field: 'begin_work_geo_challenges_source',
      displayName: 'Source',
      width: 150,
      editable: false,
    },
    {
      field: 'begin_work_rel_pol_obstacles_level',
      displayName: 'Religious and political obstacles to begin work',
      width: 150,
      editable: false,
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
      editable: false,
    },
    {
      field: 'begin_work_rel_pol_obstacles_source',
      displayName: 'Source',
      width: 150,
      editable: false,
    },
    {
      field: 'suggested_strategies',
      displayName: 'Suggested Strategies',
      width: 200,
      editable: false,
    },
    {
      field: 'comments',
      displayName: 'Comments',
      width: 150,
      editable: false,
    },
    {
      field: 'created_at',
      displayName: 'Created At',
      width: 150,
      editable: false,
    },
    {
      field: 'created_by',
      displayName: 'Created By',
      width: 150,
      editable: false,
    },
    {
      field: 'modified_at',
      displayName: 'Last Modified',
      width: 150,
      editable: false,
    },
    {
      field: 'modified_by',
      displayName: 'Last Modified By',
      width: 150,
      editable: false,
    },
    {
      field: 'owning_person',
      displayName: 'Owning Person ID',
      width: 150,
      editable: false,
    },
    {
      field: 'owning_group',
      displayName: 'Owning Group ID',
      width: 150,
      editable: false,
    },
    {
      field: 'peer',
      displayName: 'Peer ID',
      width: 150,
      editable: false,
    },
  ];

  async componentWillLoad() {
    await this.getList();
  }

  async getList() {
    this.languagesResponse = await fetchAs<ScLanguagesListRequest, ScLanguagesListResponse>('sc-languages/list', {
      token: globals.globalStore.state.token,
    });

    console.log(this.languagesResponse.languages);
  }

  render() {
    return (
      <Host>
        <slot></slot>
        {this.languagesResponse && <cf-table rowData={this.languagesResponse.languages} columnData={this.columnData}></cf-table>}
      </Host>
    );
  }
}

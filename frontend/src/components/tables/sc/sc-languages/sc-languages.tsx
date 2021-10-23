import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/cf-table/types';
import { ErrorType } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';

class ScLanguage {
  id: number;
  language_name: string;
  iso: string;
  prioritization: number;
  progress_bible: boolean;
  island: string;
  province: string;
  first_language_population: string;
  population_value: number;
  egids_level: string;
  egids_value: number;
  least_reached_progress_jps_level: string;
  least_reached_value: number;
  partner_interest_level: string;
  partner_interest_value: number;
  partner_interest_description: string;
  partner_interest_source: string;
  multiple_languages_leverage_linguistic_level: string;
  multiple_languages_leverage_linguistic_value: number;
  multiple_languages_leverage_linguistic_description: string;
  multiple_languages_leverage_linguistic_source: string;
  multiple_languages_leverage_joint_training_level: string;
  multiple_languages_leverage_joint_training_value: number;
  multiple_languages_leverage_joint_training_description: string;
  multiple_languages_leverage_joint_training_source: string;
  lang_comm_int_in_language_development_level: string;
  lang_comm_int_in_language_development_value: number;
  lang_comm_int_in_language_development_description: string;
  lang_comm_int_in_language_development_source: string;
  lang_comm_int_in_scripture_translation_level: string;
  lang_comm_int_in_scripture_translation_value: number;
  lang_comm_int_in_scripture_translation_description: string;
  lang_comm_int_in_scripture_translation_source: string;
  access_to_scripture_in_lwc_level: string;
  access_to_scripture_in_lwc_value: number;
  access_to_scripture_in_lwc_description: string;
  access_to_scripture_in_lwc_source: string;
  begin_work_geo_challenges_level: string;
  begin_work_geo_challenges_value: number;
  begin_work_geo_challenges_description: string;
  begin_work_geo_challenges_source: string;
  begin_work_rel_pol_obstacles_level: string;
  begin_work_rel_pol_obstacles_value: number;
  begin_work_rel_pol_obstacles_description: string;
  begin_work_rel_pol_obstacles_source: string;
  suggested_strategies: string;
  comments: string;
  created_at: string;
  created_by: number;
  modified_at: string;
  modified_by: number;
  owning_person: number;
  owning_group: number;
  peer: number;
}

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
    },
    {
      field: 'language_name',
      displayName: 'Language Name',
      width: 200,
    },
    {
      field: 'iso',
      displayName: 'ISO code',
      width: 50,
    },
    {
      field: 'prioritization',
      displayName: 'Prioritization',
      width: 100,
    },
    {
      field: 'progress_bible',
      displayName: 'On progress.Bible list',
      width: 150,
    },
    {
      field: 'island',
      displayName: 'Island',
      width: 150,
    },
    {
      field: 'province',
      displayName: 'Province',
      width: 150,
    },
    {
      field: 'first_language_population',
      displayName: 'First Language Population',
      width: 150,
    },
    {
      field: 'population_value',
      displayName: 'Value',
      width: 50,
    },
    {
      field: 'egids_level',
      displayName: 'EGIDS Level',
      width: 150,
    },
    {
      field: 'egids_value',
      displayName: 'Value',
      width: 50,
    },
    {
      field: 'least_reached_progress_jps_level',
      displayName: 'Least Reached Progress JPS',
      width: 150,
    },
    {
      field: 'least_reached_value',
      displayName: 'Value',
      width: 50,
    },
    {
      field: 'partner_interest_level',
      displayName: 'Partner Interest',
      width: 150,
    },
    {
      field: 'partner_interest_value',
      displayName: 'Value',
      width: 50,
    },
    {
      field: 'partner_interest_description',
      displayName: 'Description',
      width: 150,
    },
    {
      field: 'partner_interest_source',
      displayName: 'Source',
      width: 150,
    },
    {
      field: 'multiple_languages_leverage_linguistic_level',
      displayName: 'Leverage from using multiple languages at the linguistic',
      width: 150,
    },
    {
      field: 'multiple_languages_leverage_linguistic_value',
      displayName: 'Value',
      width: 50,
    },
    {
      field: 'multiple_languages_leverage_linguistic_description',
      displayName: 'Description',
      width: 150,
    },
    {
      field: 'multiple_languages_leverage_linguistic_source',
      displayName: 'Source',
      width: 150,
    },
    {
      field: 'multiple_languages_leverage_joint_training_level',
      displayName: 'Leverage from joint training',
      width: 150,
    },
    {
      field: 'multiple_languages_leverage_joint_training_value',
      displayName: 'Value',
      width: 50,
    },
    {
      field: 'multiple_languages_leverage_joint_training_description',
      displayName: 'Description',
      width: 150,
    },
    {
      field: 'multiple_languages_leverage_joint_training_source',
      displayName: 'Source',
      width: 150,
    },
    {
      field: 'lang_comm_int_in_language_development_level',
      displayName: 'Language community interest in language development',
      width: 150,
    },
    {
      field: 'lang_comm_int_in_language_development_value',
      displayName: 'Value',
      width: 50,
    },
    {
      field: 'lang_comm_int_in_language_development_description',
      displayName: 'Description',
      width: 150,
    },
    {
      field: 'lang_comm_int_in_language_development_source',
      displayName: 'Source',
      width: 150,
    },
    {
      field: 'lang_comm_int_in_scripture_translation_level',
      displayName: 'Language community interest in scripture translation',
      width: 150,
    },
    {
      field: 'lang_comm_int_in_scripture_translation_value',
      displayName: 'Value',
      width: 50,
    },
    {
      field: 'lang_comm_int_in_scripture_translation_description',
      displayName: 'Description',
      width: 150,
    },
    {
      field: 'lang_comm_int_in_scripture_translation_source',
      displayName: 'Source',
      width: 150,
    },
    {
      field: 'access_to_scripture_in_lwc_level',
      displayName: 'Access to scripture in a LWC',
      width: 150,
    },
    {
      field: 'access_to_scripture_in_lwc_value',
      displayName: 'Value',
      width: 50,
    },
    {
      field: 'access_to_scripture_in_lwc_description',
      displayName: 'Description',
      width: 150,
    },
    {
      field: 'access_to_scripture_in_lwc_source',
      displayName: 'Source',
      width: 150,
    },
    {
      field: 'begin_work_geo_challenges_level',
      displayName: 'Geographical challenges to begin work',
      width: 150,
    },
    {
      field: 'begin_work_geo_challenges_value',
      displayName: 'Value',
      width: 50,
    },
    {
      field: 'begin_work_geo_challenges_description',
      displayName: 'Description',
      width: 150,
    },
    {
      field: 'begin_work_geo_challenges_source',
      displayName: 'Source',
      width: 150,
    },
    {
      field: 'begin_work_rel_pol_obstacles_level',
      displayName: 'Religious and political obstacles to begin work',
      width: 150,
    },
    {
      field: 'begin_work_rel_pol_obstacles_value',
      displayName: 'Value',
      width: 50,
    },
    {
      field: 'begin_work_rel_pol_obstacles_description',
      displayName: 'Description',
      width: 150,
    },
    {
      field: 'begin_work_rel_pol_obstacles_source',
      displayName: 'Source',
      width: 150,
    },
    {
      field: 'suggested_strategies',
      displayName: 'Suggested Strategies',
      width: 200,
    },
    {
      field: 'comments',
      displayName: 'Comments',
      width: 150,
    },
    {
      field: 'created_at',
      displayName: 'Created At',
      width: 150,
    },
    {
      field: 'created_by',
      displayName: 'Created By',
      width: 150,
    },
    {
      field: 'modified_at',
      displayName: 'Last Modified',
      width: 150,
    },
    {
      field: 'modified_by',
      displayName: 'Last Modified By',
      width: 150,
    },
    {
      field: 'owning_person',
      displayName: 'Owning Person ID',
      width: 150,
    },
    {
      field: 'owning_group',
      displayName: 'Owning Group ID',
      width: 150,
    },
    {
      field: 'peer',
      displayName: 'Peer ID',
      width: 150,
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

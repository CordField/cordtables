export enum ErrorType {
  NoError = 'NoError',
  UnknownError = 'UnknownError',
  BadCredentials = 'BadCredentials',
  TokenNotFound = 'TokenNotFound',
  SessionNotFound = 'SessionNotFound',
  InvalidEmail = 'InvalidEmail',
  PasswordTooShort = 'PasswordTooShort',
  PasswordTooLong = 'PasswordTooLong',
  DuplicateEmail = 'DuplicateEmail',
}
export interface globalRole {
  id: number;
  createdAt: string;
  createdBy: number;
  modifiedAt: string;
  modifiedBy: number;
  name: string;
  org: number;
}

export interface languageEx {
  id?: number;
  language_name?: string;
  iso?: string;
  prioritization?: number;
  progress_bible?: Boolean;
  island?: string;
  province?: string;
  first_language_population?: number;
  population_value?: number;
  egids_level?: string;
  egids_value?: number;
  least_reached_progress_jps_level?: string;
  least_reached_value?: number;
  partner_interest_level?: string;
  partner_interest_value?: number;
  partner_interest_description?: string;
  partner_interest_source?: string;
  multiple_languages_leverage_linguistic_level?: string;
  multiple_languages_leverage_linguistic_value?: number;
  multiple_languages_leverage_linguistic_description?: string;
  multiple_languages_leverage_linguistic_source?: string;
  multiple_languages_leverage_joint_training_level?: string;
  multiple_languages_leverage_joint_training_value?: number;
  multiple_languages_leverage_joint_training_description?: string;
  multiple_languages_leverage_joint_training_source?: string;
  lang_comm_int_in_language_development_level?: string;
  lang_comm_int_in_language_development_value?: number;
  lang_comm_int_in_language_development_description?: string;
  lang_comm_int_in_language_development_source?: string;
  lang_comm_int_in_scripture_translation_level?: string;
  lang_comm_int_in_scripture_translation_value?: number;
  lang_comm_int_in_scripture_translation_description?: string;
  lang_comm_int_in_scripture_translation_source?: string;
  access_to_scripture_in_lwc_level?: string;
  access_to_scripture_in_lwc_value?: number;
  access_to_scripture_in_lwc_description?: string;
  access_to_scripture_in_lwc_source?: string;
  begin_work_geo_challenges_level?: string;
  begin_work_geo_challenges_value?: number;
  begin_work_geo_challenges_description?: string;
  begin_work_geo_challenges_source?: string;
  begin_work_rel_pol_obstacles_scale?: string;
  begin_work_rel_pol_obstacles_value?: number;
  begin_work_rel_pol_obstacles_description?: string;
  begin_work_rel_pol_obstacles_source?: string;
  suggested_strategies?: string;
  comments?: string;
  created_at?: string;
  created_by?: number;
  modified_at?: string;
  modified_by?: number;
  owning_person?: number;
  owning_group?: number;
}
export interface globalRoleTablePermissions {
  id: number;
  createdAt: string;
  createdBy: number;
  globalRole: number;
  modifiedAt: string;
  modifiedBy: number;
  tableName: string;
  tablePermission: string;
}

export class GenericResponse {
  public error: ErrorType;
}

export enum ActionType {
  Delete = 'Delete',
}

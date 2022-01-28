// import { AnyLayer } from 'mapbox-gl';

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

export enum AppState {
  Init = 'Init',
  Loaded = 'Loaded',
  TranslationLoaded = 'TranslationLoaded',
}
export interface foreignKeyClickedObject {
  id: string;
  tableUrl: string;
}

//export type EgidsScale = `0` | `1` | `2` | `3` | `4` | `5` | `6a` | `6b` | `7` | `8a` | `8b` | `9` | `10`;

export interface GlobalRole {
  id: string;
  created_at: string;
  created_by: string;
  modified_at: string;
  modified_by: string;
  name: string;
  owning_group: string;
  owning_person: string;
  chat: number;
}

export interface LanguageEx {
  id?: string;
  language_name?: string;
  iso?: string;
  prioritization?: number;
  progress_bible?: boolean;
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
  begin_work_rel_pol_obstacles_level?: string;
  begin_work_rel_pol_obstacles_value?: number;
  begin_work_rel_pol_obstacles_description?: string;
  begin_work_rel_pol_obstacles_source?: string;
  suggested_strategies?: string;
  comments?: string;
  created_at?: string;
  created_by?: string;
  modified_at?: string;
  modified_by?: string;
  owning_person?: string;
  owning_group?: string;
}
export interface globalRoleTablePermissions {
  id: string;
  createdAt: string;
  createdBy: string;
  globalRole: string;
  modifiedAt: string;
  modifiedBy: string;
  tableName: string;
  tablePermission: string;
}

export class GenericResponse {
  public error: ErrorType;
}
export class GenericRequest {}

export enum ActionType {
  Delete = 'Delete',
}

export interface ScriptureReference {
  id: string;
  book_start: string;
  book_end: string;
  chapter_start: number;
  chapter_end: number;
  verse_start: number;
  verse_end: number;
}

export interface AdminUser {
  id?: string;
  person?: string;
  email?: string;
  chat?: number;
  created_at?: string;
  created_by?: string;
  modified_at?: string;
  modified_by?: string;
  owning_person?: string;
  owning_group?: string;
}

export type SiteTextLanguage = {
  language: string;
  language_name: string;
};

export type SiteTextString = {
  id: string;
  english: string;
  comment: string;
  created_at?: string | undefined;
  created_by?: string | undefined;
  modified_at?: string | undefined;
  modified_by?: string | undefined;
  owning_person?: string | undefined;
  owning_group?: string | undefined;
}

export type SiteTextTranslation = {
  id: string;
  site_text: string;
  translation: string;
  language?: string | undefined;
  created_at?: string | undefined;
  created_by?: string | undefined;
  modified_at?: string | undefined;
  modified_by?: string | undefined;
  owning_person?: string | undefined;
  owning_group?: string | undefined;
};

export type SiteTextTranslationList = {
  language: string;
  translations: Array<SiteTextTranslation>;
};

export type AutocompleteRequest = {
  searchColumnName: string;
  token: string;
  resultColumnName: string;
  searchKeyword: string;
  tableName: string;
};

export type AutocompleteResponse = {
  error: ErrorType;
  data: any;
};

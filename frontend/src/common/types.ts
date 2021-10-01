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
  created_at?: string;
  created_by?: number;
  modified_at?: string;
  modified_by?: number;
  lang_name?: string;
  lang_code?: string;
  location?: string;
  first_lang_population?: number;
  population?: number;
  egids_level?: number;
  egids_value?: number;
  least_reached_progress_jps_scale?: number;
  least_reached_value?: number;
  partner_interest?: number;
  partner_interest_description?: string;
  partner_interest_source?: string;
  multi_lang_leverage?: number;
  multi_lang_leverage_description?: string;
  multi_lang_leverage_source?: string;
  community_interest?: number;
  community_interest_description?: string;
  community_interest_source?: string;
  community_interest_value?: number;
  community_interest_scripture_description?: string;
  community_interest_scripture_source?: string;
  lwc_scripture_access?: number;
  lwc_scripture_description?: string;
  lwc_scripture_source?: string;
  access_to_begin?: number;
  access_to_begin_description?: string;
  access_to_begin_source?: string;
  suggested_strategies?: string;
  comments?: string;
  prioritization?: number;
  progress_bible?: number;
}
export class GenericResponse {
  public error: ErrorType;
}

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
  id: number;
  createdAt: string;
  createdBy: number;
  modifiedAt: string;
  modifiedBy: number;
  langName: string;
  langCode: string;
  location: string;
  firstLangPopulation: number;
  population: number;
  egidsLevel: number;
  egidsValue: number;
  leastReachedProgressJpsScale: number;
  leastReachedValue: number;
  partnerInterest: number;
  partnerInterestDescription: string;
  partnerInterestSource: string;
  multiLangLeverage: number;
  multiLangLeverageDescription: string;
  multiLangLeverageSource: string;
  communityInterest: number;
  communityInterestDescription: string;
  communityInterestSource: string;
  communityInterestValue: number;
  communityInterestScriptureDescription: string;
  communityInterestScriptureSource: string;
  lwcScriptureAccess: number;
  lwcScriptureDescription: string;
  lwcScriptureSource: string;
  accessToBegin: number;
  accessToBeginDescription: string;
  accessToBeginSource: string;
  suggestedStrategies: string;
  comments: string;
  prioritization: number;
  progressBible: number;
}
export class GenericResponse {
  public error: ErrorType;
}

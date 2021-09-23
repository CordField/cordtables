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
export class GenericResponse {
  public error: ErrorType;
}

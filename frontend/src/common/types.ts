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

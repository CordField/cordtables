package com.seedcompany.cordtables.common

class Types {
}

enum class ErrorType {
    NoError,
    AdminOnly,
    UnknownError,
    DoesNotHaveCreatePermission,
    BadCredentials,
    TokenNotFound,
    SessionNotFound,
    InvalidEmail,
    PasswordTooShort,
    PasswordTooLong,
    DuplicateEmail,
    UserNotFound,
    SQLInsertError,
    SQLReadError,
    SQLUpdateError,
    SQLDeleteError,
    InputMissingName,
    InputMissingGroup,
    InputMissingPerson,
    InputMissingTableName,
    InputMissingRow,
    MembershipAlreadyExists,
    NameTooShort,
    NameTooLong,
    NameAlreadyExists,
    MissingId,
    UserTokenNotFound,
    emptyReadResult,
    CannotUpdateAdminGroup,
    DoesNotHaveUpdatePermission,
    DoesNotHaveDeletePermission,
    InputMissingUrl,
    UrlTooShort,
    UrlTooLong,
    UrlAlreadyExists,
    InputMissingSecret,
    SecretNotValid,
}

data class GenericResponse (
    val error: ErrorType,
)
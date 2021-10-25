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
    PeerAlreadyExists,
    PeerFailedToInitialize,
    PeerNotPresent,
    PeerFailedToConfirm,
    PeerFailedToLogin,
    InputMissingToken,
    InputMissingTable,
    InputMissingColumn,
    ValueDoesNotMap,
}


enum class CommonSensitivity {
    Low,
    Medium,
    High,
}

data class GenericResponse (
    val error: ErrorType,
)

enum class LocationType {
    City,
    County,
    State,
    Country,
    CrossBorderArea,
}
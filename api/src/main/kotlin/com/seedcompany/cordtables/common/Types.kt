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
    DoesNotHaveDeletePermission
}
enum class access_level (val accessType: String){
    Read("Read"),
    Write("Write"),
    Admin("Admin")
}

enum class CommonSensitivity (val accessType: String){
    Low("Low"),
    Medium("Medium"),
    High("High")
}
data class GenericResponse (
    val error: ErrorType,
)
package com.seedcompany.cordspringstencil.common

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
    NameTooShort,
    NameTooLong,
    NameAlreadyExists,
    MissingId,
    DoesNotHaveUpdatePermission
}

data class GenericResponse (
    val error: ErrorType,
)
package com.seedcompany.cordspringstencil.common

class Types {
}

enum class ErrorType {
    NoError,
    AdminOnly,
    UnknownError,
    BadCredentials,
    TokenNotFound,
    SessionNotFound,
    InvalidEmail,
    PasswordTooShort,
    PasswordTooLong,
    DuplicateEmail,
    InputMissingName,
    NameTooShort,
    NameTooLong,
    NameAlreadyExists,
    MissingId,
}

data class GenericResponse (
    val error: ErrorType,
)
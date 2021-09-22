package com.seedcompany.cordspringstencil.common

class Types {
}

enum class ErrorType {
    NoError,
    UnknownError,
    BadCredentials,
    TokenNotFound,
    SessionNotFound,
    InvalidEmail,
    PasswordTooShort,
    PasswordTooLong,
    DuplicateEmail,
}

data class GenericResponse (
    val error: ErrorType,
)
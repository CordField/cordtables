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
    emptyReadResult,
    UserEmailNotFound,
    SQLInsertError,
    SQLUpdateError,
    SQLDeleteError,
    UserTokenNotFound,
}

enum class access_level (val accessType: String){
    Read("Read"),
    Write("Write"),
    Admin("Admin")
}

data class GenericResponse (
    val error: ErrorType,
)
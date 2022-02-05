package com.cordtables.v2.common

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Component

@Component
class Utility {
    val encoder = Argon2PasswordEncoder(16, 32, 1, 4096, 3)
}
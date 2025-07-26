package com.fesi.flowit.common.auth

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordEncryptor(
    private val passwordEncoder: PasswordEncoder
) {
    fun encrypt(password: String): String {
        return passwordEncoder.encode(password)
    }

    fun matches(raw: String, hashed: String): Boolean {
        return passwordEncoder.matches(raw, hashed)
    }
}

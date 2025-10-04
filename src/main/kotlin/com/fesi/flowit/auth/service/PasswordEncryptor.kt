package com.fesi.flowit.auth.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordEncryptor(
    private val passwordEncoder: PasswordEncoder
) {
    fun encrypt(password: String): String {
        return passwordEncoder.encode(password)
    }
}
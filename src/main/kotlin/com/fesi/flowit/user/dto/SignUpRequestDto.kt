package com.fesi.flowit.user.dto

data class SignUpRequestDto (
    val email: String,
    val name: String,
    val password: String
)
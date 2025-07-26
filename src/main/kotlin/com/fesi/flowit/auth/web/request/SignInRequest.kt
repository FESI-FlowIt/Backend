package com.fesi.flowit.auth.web.request

data class SignInRequest(
    val email: String,
    val password: String
)

package com.fesi.flowit.auth.service.dto

import com.fesi.flowit.auth.web.request.SignInRequest

data class SignInDto(
    val email: String,
    val password: String
) {
    companion object {
        fun from(signInRequest: SignInRequest): SignInDto {
            return SignInDto(signInRequest.email, signInRequest.password)
        }
    }
}

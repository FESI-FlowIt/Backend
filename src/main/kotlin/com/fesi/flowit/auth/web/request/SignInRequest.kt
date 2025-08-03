package com.fesi.flowit.auth.web.request

import io.swagger.v3.oas.annotations.media.Schema

data class SignInRequest(
    @field:Schema(
        description = "회원 이메일",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val email: String,

    @field:Schema(
        description = "회원 비밀번호",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val password: String
)

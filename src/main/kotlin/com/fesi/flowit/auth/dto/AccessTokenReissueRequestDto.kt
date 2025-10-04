package com.fesi.flowit.auth.dto

import io.swagger.v3.oas.annotations.media.Schema

data class AccessTokenReissueRequestDto(
    @field:Schema(
        example = "refresh_token",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val grantType: String,

    @field:Schema(
        description = "리프레시 토큰",
        example = "2YotnFZFEjr1zCsicMWpAA",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val refreshToken: String,
)
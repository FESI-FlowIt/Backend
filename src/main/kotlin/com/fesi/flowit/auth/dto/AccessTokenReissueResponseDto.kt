package com.fesi.flowit.auth.dto

import io.swagger.v3.oas.annotations.media.Schema

data class AccessTokenReissueResponseDto(
    @field:Schema(
        description = "새로 생성된 액세스 토큰",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val accessToken: String,

    @field:Schema(
        example = "Bearer",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val tokenType: String = "Bearer",

    @field:Schema(
        example = "액세스 토큰 유효 기간",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val expiresIn: Long,
) {
    @field:Schema(
        description = "새로 생성된 리프레시 토큰",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    var refreshToken: String? = null

    companion object {
        fun withRefreshToken(accessToken: String, expiresIn: Long, refreshToken: String): AccessTokenReissueResponseDto {
            return AccessTokenReissueResponseDto(
                accessToken = accessToken,
                tokenType = "Bearer",
                expiresIn = expiresIn
            ).apply { this.refreshToken = refreshToken }
        }
    }
}
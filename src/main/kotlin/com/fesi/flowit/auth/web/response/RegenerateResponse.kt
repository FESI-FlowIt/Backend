package com.fesi.flowit.auth.web.response

import io.swagger.v3.oas.annotations.media.Schema

data class RegenerateResponse(
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
        fun of(accessToken: String, expiresIn: Long): RegenerateResponse {
            return RegenerateResponse(
                accessToken = accessToken,
                tokenType = "Bearer",
                expiresIn = expiresIn
            )
        }
    }

    fun with(refreshToken: String): RegenerateResponse {
        this.refreshToken = refreshToken
        return this
    }
}

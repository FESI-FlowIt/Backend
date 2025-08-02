package com.fesi.flowit.auth.web.response

import io.swagger.v3.oas.annotations.media.Schema

data class RegenerateResponse(
    @field:Schema(
        description = "새로 생성된 액세스 토큰",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val accessToken: String,
) {
    @field:Schema(
        description = "새로 생성된 리프레시 토큰",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    var refreshToken: String = ""

    companion object {
        fun of(accessToken: String): RegenerateResponse {
            return RegenerateResponse(accessToken)
        }
    }

    fun with(refreshToken: String): RegenerateResponse {
        this.refreshToken = refreshToken
        return this
    }
}

package com.fesi.flowit.auth.web.response

import io.swagger.v3.oas.annotations.media.Schema

class RegenerateResponse(
    @field:Schema(
        description = "새로 생성된 액세스 토큰",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val accessToken: String
) {
    companion object {
        fun of(accessToken: String): RegenerateResponse {
            return RegenerateResponse(accessToken)
        }
    }
}

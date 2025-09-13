package com.fesi.flowit.auth.local.web.response

import com.fesi.flowit.user.entity.User
import io.swagger.v3.oas.annotations.media.Schema

class SignInResponse(
    @field:Schema(
        description = "회원 이메일",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val id: Long,

    @field:Schema(
        description = "회원 비밀번호",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val email: String,

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
    val expiresIn: Long
) {
    @field:Schema(
        description = "새로 생성된 리프레시 토큰",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    var refreshToken: String? = null

    companion object {
        fun of(user: User, accessToken: String, expiresIn: Long): SignInResponse {
            return SignInResponse(
                id = user.id,
                email = user.email,
                accessToken = accessToken,
                expiresIn = expiresIn
            )
        }
    }

    fun with(refreshToken: String): SignInResponse {
        this.refreshToken = refreshToken
        return this
    }
}

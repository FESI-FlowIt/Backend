package com.fesi.flowit.auth.dto

import com.fesi.flowit.user.entity.User
import io.swagger.v3.oas.annotations.media.Schema

class SignInResponseDto(
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
        fun withRefreshToken(user: User, accessToken: String, expiresIn: Long, refreshToken: String): SignInResponseDto {
            return SignInResponseDto(
                id = user.id,
                email = user.email,
                accessToken = accessToken,
                expiresIn = expiresIn
            ).apply { this.refreshToken = refreshToken }
        }
    }
}
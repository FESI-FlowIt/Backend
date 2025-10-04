package com.fesi.flowit.auth.vo

import io.jsonwebtoken.Claims
import java.util.Date

data class TokenInfo(
    val email: String,
    val userId: Long,
    val issuedAt: Date,
    val expiration: Date
) {
    companion object {
        fun fromRefreshTokenClaims(claims: Claims): TokenInfo {
            return TokenInfo(
                email = "",
                userId = (claims["userId"] as String).toLong(),
                issuedAt = claims.issuedAt,
                expiration = claims.expiration
            )
        }
    }
}
package com.fesi.flowit.common.auth.dto

import io.jsonwebtoken.Claims
import java.util.*

data class TokenInfo(
    val email: String,
    val userId: Long,
    val issuedAt: Date,
    val expiration: Date
) {
    companion object {
        fun fromClaims(claims: Claims): TokenInfo {
            return TokenInfo(
                email = claims.subject,
                userId = (claims["userId"] as String).toLong(), // String으로 저장된 userId를 Long으로 복원
                issuedAt = claims.issuedAt,
                expiration = claims.expiration
            )
        }
    }
}
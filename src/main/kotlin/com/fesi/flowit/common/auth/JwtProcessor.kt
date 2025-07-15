package com.fesi.flowit.common.auth

import com.fesi.flowit.user.repository.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtProcessor(
    @Value("\${auth.secret-key}")
    private val secretKey: String,
    private val userRepository: UserRepository
) {
    val key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

    fun handle(token: String): TokenInfo {
        val tokenInfo = unpack(token)

        if (isTokenExpired(tokenInfo)) {
            throw TokenExpiredException()
        }
        if (!isTokenStored(tokenInfo)) {
            throw InvalidUserException()
        }

        return tokenInfo
    }

    fun unpack(token: String): TokenInfo {
        var unpacked: Jws<Claims>

        try {
            unpacked = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)

            val claims = unpacked.payload

            return TokenInfo(
                email = claims.subject,
                userId = (claims["userId"] as String).toLong(),
                issuedAt = claims.issuedAt,
                expiration = claims.expiration
            )
        } catch (ex: JwtException) {
            throw FailToParseJwtException()
        }
    }

    fun isTokenExpired(tokenInfo: TokenInfo): Boolean {
        return tokenInfo.expiration.before(Date())
    }

    fun isTokenStored(tokenInfo: TokenInfo): Boolean {
        return userRepository.findById(tokenInfo.userId).isPresent
    }
}

class FailToParseJwtException : RuntimeException() {}
class TokenExpiredException : RuntimeException() {}
class InvalidUserException : RuntimeException() {}

data class TokenInfo(
    val email: String,
    val userId: Long,
    val issuedAt: Date,
    val expiration: Date
) {
    companion object
}
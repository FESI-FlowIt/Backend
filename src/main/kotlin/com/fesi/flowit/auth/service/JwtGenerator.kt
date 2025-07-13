package com.fesi.flowit.auth.service

import com.fesi.flowit.auth.entity.RefreshToken
import com.fesi.flowit.auth.repository.TokenRepository
import com.fesi.flowit.user.entity.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class JwtGenerator(
    @Value("\${auth.secret-key}")
    private val secretKey: String,
    private val tokenRepository: TokenRepository
) {
    fun generateToken(user: User): String {
        val now = Instant.now()
        val expiration = now.plus(15, ChronoUnit.MINUTES)

        return Jwts.builder()
            .subject(user.email)
            .claim("userId", user.id)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), Jwts.SIG.HS512)
            .compact()
    }

    /**
     * refresh token의 상태에 따라 처리한다
     */
    fun handleRefreshToken(user: User) {
        if (!isRefreshTokenExists(user)) {
            val refreshToken = generateRefreshToken(user)
            storeRefreshToken(refreshToken)
            return
        }
        if (!isRefreshTokenExpired(user)) {
            return
        }

        revokeRefreshToken(user)
        val refreshToken = generateRefreshToken(user)
        storeRefreshToken(refreshToken)
    }

    /**
     * refresh token이 존재하는지 확인한다
     */
    private fun isRefreshTokenExists(user: User): Boolean {
        val existingToken = tokenRepository.findByUserIdAndRevoked(user.id, false)

        if (existingToken == null) {
            return false
        }

        return true
    }

    /**
     * refresh token의 유효 기간이 지났는지 확인한다
     * refresh token이 존재함을 가정한다
     *
     * @visibleForTesting
     */
    internal fun isRefreshTokenExpired(user: User): Boolean {
        val now = Instant.now()
        val nowLocal = instantToLocalDateTime(now)

        val existingToken = tokenRepository.findByUserIdAndRevoked(user.id, false)

        if (existingToken!!.expiresAt >= nowLocal) {
            return false
        }

        return true
    }

    /**
     * 새로운 refresh token을 만든다
     */
    fun generateRefreshToken(user: User): RefreshToken {
        val now = Instant.now()
        val expiration = now.plus(30, ChronoUnit.DAYS)
        val jwtRefreshToken = Jwts.builder()
            .subject(user.email)
            .claim("userId", user.id)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), Jwts.SIG.HS512)
            .compact()

        return RefreshToken.of(
            userId = user.id,
            token = jwtRefreshToken,
            expiresAt = instantToLocalDateTime(expiration),
            revoked = false
        )
    }

    /**
     * refresh token의 유효 기간이 지났음을 표시한다
     */
    fun revokeRefreshToken(user: User) {
        val existingToken = tokenRepository.findByUserIdAndRevoked(user.id, false)
        tokenRepository.updateRevoked(existingToken!!.userId, true)
    }

    /**
     * refresh token을 db에 저장한다
     */
    fun storeRefreshToken(refreshToken: RefreshToken) {
        tokenRepository.save(refreshToken)
    }

    fun instantToLocalDateTime(instant: Instant): LocalDateTime {
        val zoneId = ZoneId.of("Asia/Seoul")
        return instant.atZone(zoneId).toLocalDateTime()
    }
}

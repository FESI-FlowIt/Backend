package com.fesi.flowit.auth.service

import com.fesi.flowit.auth.vo.RefreshToken
import com.fesi.flowit.auth.repository.TokenRepository
import com.fesi.flowit.user.entity.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
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
    /**
     * access token을 생성한다
     */
    fun generateToken(user: User): String {
        val now = Instant.now()
        val expiration = now.plus(15, ChronoUnit.MINUTES)

        return Jwts.builder()
            .subject(user.email)
            .claim(
                "userId",
                user.id.toString()
            ) // Long을 String으로 저장 (JWT에서 Long이 Integer로 변환되어 값 손실 방지)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), Jwts.SIG.HS512)
            .compact()
    }

    fun generateTokenWith(authentication: Authentication): String {
        val principal = authentication.principal as User

        val now = Instant.now()
        val expiration = now.plus(15, ChronoUnit.MINUTES)

        return Jwts.builder()
            .subject(principal.email)
            .claim(
                "userId",
                principal.id.toString()
            ) // Long을 String으로 저장 (JWT에서 Long이 Integer로 변환되어 값 손실 방지)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), Jwts.SIG.HS512)
            .compact()
    }

    /**
     * refresh token의 상태에 따라 처리한다
     * refresh token이 없으면 새 토큰을 생성하고 저장한다
     * 유효한 refresh token이 이미 있으면 토큰 생성이나 갱신을 하지 않는다
     * 만료된 refresh token이 있으면 revoke 후 새 토큰을 생성한다
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

    fun handleRefreshTokenWith(authentication: Authentication) {
        val principal = authentication.principal as User

        if (!isRefreshTokenExists(principal)) {
            val refreshToken = generateRefreshToken(principal)
            storeRefreshToken(refreshToken)
            return
        }
        if (!isRefreshTokenExpired(principal)) {
            return
        }

        revokeRefreshToken(principal)
        val refreshToken = generateRefreshToken(principal)
        storeRefreshToken(refreshToken)
    }

    /**
     * refresh token이 존재하는지 확인한다
     */
    private fun isRefreshTokenExists(user: User): Boolean {
        tokenRepository.findByUserIdAndRevoked(user.id, false) ?: return false

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
            .claim(
                "userId",
                user.id.toString()
            ) // Long을 String으로 저장 (JWT에서 Long이 Integer로 변환되어 값 손실 방지)
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

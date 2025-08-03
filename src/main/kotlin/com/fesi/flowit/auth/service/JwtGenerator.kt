package com.fesi.flowit.auth.service

import com.fesi.flowit.auth.repository.TokenRepository
import com.fesi.flowit.auth.vo.RefreshToken
import com.fesi.flowit.common.auth.JwtProcessor
import com.fesi.flowit.user.entity.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class JwtGenerator(
    @Value("\${auth.secret-key}")
    private val secretKey: String,
    private val tokenRepository: TokenRepository,
    private val jwtProcessor: JwtProcessor
) {
    private val REFRESH_TOKEN_REGENERATE_BASIS = 3

    /**
     * access token을 생성한다
     */
    fun generateToken(authentication: Authentication): String {
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
     * 로그인 시 리프레시 토큰 처리
     * 리프레시 토큰이 있을 경우, 토큰 재발급 시 쓰는 handleRefreshTokenWith()에 처리를 위임한다
     */
    fun handleRefreshToken(authentication: Authentication): String? {
        val principal = authentication.principal as User

        if (!isRefreshTokenExists(principal.id)) {
            val refreshToken = generateRefreshToken(principal.id)
            storeRefreshToken(refreshToken)
            return refreshToken.token
        }

        val refreshToken = findRefreshToken(principal.id).token
        return handleRefreshTokenWith(refreshToken)
    }

    /**
     * 토큰 재발급 시 리프레시 토큰 처리
     */
    fun handleRefreshTokenWith(oldRefreshToken: String): String? {
        val unpack = jwtProcessor.unpackRefreshToken(oldRefreshToken)

        if (!isRefreshTokenExistsWith(oldRefreshToken)) {
            val refreshToken = generateRefreshToken(unpack.userId)
            storeRefreshToken(refreshToken)
            return refreshToken.token
        }

        if (!isRefreshTokenIsAboutTobeExpired(oldRefreshToken)) {
            return null
        }

        if (isRefreshTokenExpired(oldRefreshToken)) {
            revokeRefreshToken(oldRefreshToken)
        }
        val refreshToken = generateRefreshToken(unpack.userId)
        storeRefreshToken(refreshToken)
        return refreshToken.token
    }

    internal fun findRefreshToken(id: Long): RefreshToken {
        val token = tokenRepository.findByUserIdAndRevoked(id, false)
        return token!!
    }

    /**
     * refresh token이 존재하는지 확인한다
     */
    internal fun isRefreshTokenExists(id: Long): Boolean {
        tokenRepository.findByUserIdAndRevoked(id, false) ?: return false

        return true
    }

    internal fun isRefreshTokenExistsWith(refreshToken: String): Boolean {
        tokenRepository.findByTokenAndRevoked(refreshToken, false) ?: return false

        return true
    }

    /**
     * refresh token의 유효 기간이 지났는지 확인한다
     * refresh token이 존재함을 가정한다
     *
     * @visibleForTesting
     */
    internal fun isRefreshTokenExpired(refreshToken: String): Boolean {
        val now = Instant.now()
        val nowLocal = instantToLocalDateTime(now)
        val unpackRefreshToken = jwtProcessor.unpackRefreshToken(refreshToken)
        val tokenExpiresAt = instantToLocalDateTime(unpackRefreshToken.expiration.toInstant())

        if (tokenExpiresAt >= nowLocal) {
            return false
        }

        return true
    }

    internal fun isRefreshTokenIsAboutTobeExpired(refreshToken: String): Boolean {
        val now = Instant.now()
        val nowLocal = instantToLocalDateTime(now)
        val unpackRefreshToken = jwtProcessor.unpackRefreshToken(refreshToken)
        val tokenExpiresAt = instantToLocalDateTime(unpackRefreshToken.expiration.toInstant())

        val daysLeft: Long = Duration.between(nowLocal, tokenExpiresAt).toDays()

        return daysLeft < REFRESH_TOKEN_REGENERATE_BASIS
    }

    /**
     * 새로운 refresh token을 만든다
     */
    fun generateRefreshToken(id: Long): RefreshToken {
        val now = Instant.now()
        val expiration = now.plus(30, ChronoUnit.DAYS)
        val jwtRefreshToken = Jwts.builder()
            .claim(
                "userId",
                id.toString()
            ) // Long을 String으로 저장 (JWT에서 Long이 Integer로 변환되어 값 손실 방지)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), Jwts.SIG.HS512)
            .compact()

        return RefreshToken.of(
            userId = id,
            token = jwtRefreshToken,
            expiresAt = instantToLocalDateTime(expiration),
            revoked = false
        )
    }

    /**
     * refresh token의 유효 기간이 지났음을 표시한다
     */
    fun revokeRefreshToken(refreshToken: String) {
        tokenRepository.updateRevoked(refreshToken, true)
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

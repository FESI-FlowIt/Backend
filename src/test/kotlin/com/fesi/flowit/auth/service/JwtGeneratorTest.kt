package com.fesi.flowit.auth.service

import com.fesi.flowit.auth.vo.RefreshToken
import com.fesi.flowit.auth.repository.TokenRepository
import com.fesi.flowit.auth.service.JwtProcessor
import com.fesi.flowit.auth.vo.TokenInfo
import com.fesi.flowit.user.entity.User
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.core.Authentication
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class JwtGeneratorTest : StringSpec({

    lateinit var repository: TokenRepository
    lateinit var jwtGenerator: JwtGenerator
    lateinit var jwtProcessor: JwtProcessor

    val secretKey =
        "40e96460271ece5c44153b6c90bccdc1965cfc8b21568dcb92ade8064173226b4509b3c31faa83d20b53ad35a6b529d42a4e98f967a072bbbde244b9af8236ff"
    val testUser = User.forTest(
        id = 1L,
        email = "test@example.com",
        password = "encrypted_password"
    )

    beforeEach {
        repository = mockk<TokenRepository>(relaxUnitFun = true)
        jwtProcessor = mockk<JwtProcessor>(relaxed = true)
        jwtGenerator = JwtGenerator(secretKey, repository, jwtProcessor)
    }

    "로그인 시 refresh token이 없으면 새 토큰을 생성하고 저장한다" {
        val authentication = mockk<Authentication>(relaxed = true)
        hasUserDetails(authentication)

        every { repository.findByUserIdAndRevoked(any(), any()) } returns null
        every { repository.save(any()) } returns mockk<RefreshToken>()

        jwtGenerator.handleRefreshToken(authentication)

        verify { repository.save(any()) }
    }

    "로그인 시 유효한 refresh token이 이미 있으면 새로 생성하는 대신 이걸 반환한다" {
        val authentication = mockk<Authentication>(relaxed = true)
        hasUserDetails(authentication)

        val refreshToken = RefreshToken.valid(testUser)
        every { repository.findByUserIdAndRevoked(any(), any()) } returns refreshToken

        every { jwtProcessor.unpackRefreshToken(any()) } returns RefreshToken.toInfo(refreshToken)
        every { repository.findByTokenAndRevoked(any(), any()) } returns refreshToken

        jwtGenerator.handleRefreshToken(authentication) shouldNotBe null
    }

    "재발급 시 refresh token이 없으면 새 토큰을 생성하고 저장한다" {
        every { repository.findByTokenAndRevoked(any(), any()) } returns null
        every { repository.save(any()) } returns mockk<RefreshToken>()

        jwtGenerator.handleRefreshTokenWith("old_refresh_token")

        verify { repository.save(any()) }
    }

    "재발급이 필요하지 않은 refresh token이면 토큰 생성이나 갱신을 하지 않는다" {
        val refreshToken = RefreshToken.valid(testUser)
        every { repository.findByTokenAndRevoked(any(), any()) } returns refreshToken
        every { jwtProcessor.unpackRefreshToken(any()) } returns RefreshToken.toInfo(refreshToken)

        jwtGenerator.handleRefreshTokenWith(refreshToken.token)

        verify(exactly = 0) { repository.save(any()) }
        verify(exactly = 0) { repository.updateRevoked(any(), any()) }
    }

    "재발급이 필요하지 않은 refresh token이면 자신을 그대로 돌려준다" {
        val refreshToken = RefreshToken.valid(testUser)
        every { repository.findByTokenAndRevoked(any(), any()) } returns refreshToken
        every { jwtProcessor.unpackRefreshToken(any()) } returns RefreshToken.toInfo(refreshToken)

        jwtGenerator.handleRefreshTokenWith(refreshToken.token) shouldNotBe null
    }

    "만료된 refresh token이면 revoke 후 새 토큰을 생성한다" {
        val expiredToken = RefreshToken.expired(testUser)
        every { repository.findByTokenAndRevoked(any(), any()) } returns expiredToken
        every { repository.save(any()) } returns mockk()
        every { jwtProcessor.unpackRefreshToken(any()) } returns RefreshToken.toInfo(expiredToken)

        jwtGenerator.handleRefreshTokenWith(expiredToken.token)

        verify { repository.updateRevoked(any(), any()) }
    }

    "refresh 토큰 만료 여부를 확인할 수 있다" {
        val expiredToken = RefreshToken.expired(testUser)
        every { jwtProcessor.unpackRefreshToken(any()) } returns RefreshToken.toInfo(expiredToken)

        val result = jwtGenerator.isRefreshTokenExpired(expiredToken.token)

        result shouldBe true
    }

    "refresh 토큰을 재발급해야 하는지 확인할 수 있다" {
        val expiredToken = RefreshToken.aboutTobeExpired(testUser)
        every { jwtProcessor.unpackRefreshToken(any()) } returns RefreshToken.toInfo(expiredToken)

        val result = jwtGenerator.isRefreshTokenIsAboutTobeExpired(expiredToken.token)

        result shouldBe true
    }

    "유효 기간이 지난 refresh 토큰을 revoke 처리한다" {
        val existingToken = RefreshToken.expired(testUser)

        jwtGenerator.revokeRefreshToken(existingToken.token)

        verify { repository.updateRevoked(existingToken.token, true) }
    }

    "refresh 토큰을 데이터베이스에 저장한다" {
        val refreshToken = RefreshToken.created(testUser)
        every { repository.save(refreshToken) } returns refreshToken

        jwtGenerator.storeRefreshToken(refreshToken)

        verify { repository.save(refreshToken) }
    }

    "instant를 localDateTime으로 변환한다" {
        val instant = Instant.parse("2024-01-01T12:00:00Z")

        val result = jwtGenerator.instantToLocalDateTime(instant)

        result shouldBe LocalDateTime.of(2024, 1, 1, 21, 0, 0) // UTC+9
    }

    "액세스 토큰 유효기간을 초 단위로 나타낸다" {
        val accessTokenExpiresInSeconds: Long = 60 * 60
        val accessTokenExpiresInMinutes: Long = 60
        val now = Instant.parse("2007-12-03T10:15:30.00Z")
        val expirationInSeconds = now.plus(accessTokenExpiresInSeconds, ChronoUnit.SECONDS)
        val expirationInMinutes = now.plus(accessTokenExpiresInMinutes, ChronoUnit.MINUTES)
        expirationInSeconds shouldBe expirationInMinutes
    }
})

private fun User.Companion.forTest(
    id: Long,
    email: String,
    password: String
): User {
    val user = User(
        email = email,
        name = "testName",
        password = password,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        deletedAt = null
    )
    user.id = id
    return user
}

private fun RefreshToken.Companion.created(user: User): RefreshToken {
    return RefreshToken.forTest(
        user = user,
        expiresAt = LocalDateTime.now().plusDays(30),
    )
}

private fun RefreshToken.Companion.toInfo(refreshToken: RefreshToken): TokenInfo {
    return TokenInfo(
        email = "",
        userId = refreshToken.userId,
        issuedAt = Date(),
        expiration = Timestamp.valueOf(refreshToken.expiresAt)
    )
}

private fun RefreshToken.Companion.valid(user: User): RefreshToken {
    return RefreshToken.forTest(
        user = user,
        expiresAt = LocalDateTime.now().plusDays(15),
    )
}

private fun RefreshToken.Companion.aboutTobeExpired(user: User): RefreshToken {
    return RefreshToken.forTest(
        user = user,
        token = "expired_token",
        expiresAt = LocalDateTime.now().plusDays(2),
    )
}

private fun RefreshToken.Companion.expired(user: User): RefreshToken {
    return RefreshToken.forTest(
        user = user,
        token = "expired_token",
        expiresAt = LocalDateTime.now().minusDays(1),
    )
}

private fun RefreshToken.Companion.forTest(
    user: User,
    id: Long = 1L,
    token: String = "valid_token",
    expiresAt: LocalDateTime,
    revoked: Boolean = false
): RefreshToken {
    val refreshToken = RefreshToken(
        userId = user.id,
        token = token,
        expiresAt = expiresAt,
        revoked = revoked
    )
    refreshToken.id = id
    return refreshToken
}

private fun hasUserDetails(authentication: Authentication) {
    every { authentication.principal } returns mockk<User>(relaxed = true)
}

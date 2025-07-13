package com.fesi.flowit.auth.service

import com.fesi.flowit.auth.entity.RefreshToken
import com.fesi.flowit.auth.repository.TokenRepository
import com.fesi.flowit.user.entity.User
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Instant
import java.time.LocalDateTime

class JwtGeneratorTest : StringSpec({

    lateinit var repository: TokenRepository
    lateinit var jwtGenerator: JwtGenerator

    val secretKey =
        "40e96460271ece5c44153b6c90bccdc1965cfc8b21568dcb92ade8064173226b4509b3c31faa83d20b53ad35a6b529d42a4e98f967a072bbbde244b9af8236ff"
    val testUser = User.forTest(
        id = 1L,
        email = "test@example.com",
        password = "encrypted_password"
    )

    beforeEach {
        repository = mockk<TokenRepository>(relaxUnitFun = true)
        jwtGenerator = JwtGenerator(secretKey, repository)
    }

    "refresh token이 없으면 새 토큰을 생성하고 저장한다" {
        every { repository.findByUserIdAndRevoked(testUser.id, false) } returns null
        every { repository.save(any()) } returns mockk<RefreshToken>()

        jwtGenerator.handleRefreshToken(testUser)

        verify { repository.save(any()) }
    }

    "유효한 refresh token이 이미 있으면 토큰 생성이나 갱신을 하지 않는다" {
        val validToken = RefreshToken.valid(testUser)
        every { repository.findByUserIdAndRevoked(testUser.id, false) } returns validToken

        jwtGenerator.handleRefreshToken(testUser)

        verify(exactly = 0) { repository.save(any()) }
        verify(exactly = 0) { repository.updateRevoked(any(), any()) }
    }

    "만료된 refresh token이 있으면 revoke 후 새 토큰을 생성한다" {
        val expiredToken = RefreshToken.expired(testUser)
        every {
            repository.findByUserIdAndRevoked(
                testUser.id,
                false
            )
        } returns expiredToken
        every { repository.save(any()) } returns mockk()

        jwtGenerator.handleRefreshToken(testUser)

        verify { repository.updateRevoked(testUser.id, true) }
    }

    "refresh 토큰 만료 여부를 확인할 수 있다" {
        val expiredToken = RefreshToken.expired(testUser)
        every {
            repository.findByUserIdAndRevoked(
                testUser.id,
                false
            )
        } returns expiredToken

        val result = jwtGenerator.isRefreshTokenExpired(testUser)

        result shouldBe true
    }

    "유효 기간이 지난 refresh 토큰을 revoke 처리한다" {
        val existingToken = RefreshToken.valid(testUser)
        every {
            repository.findByUserIdAndRevoked(
                testUser.id,
                false
            )
        } returns existingToken

        jwtGenerator.revokeRefreshToken(testUser)

        verify { repository.updateRevoked(testUser.id, true) }
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
})

private fun User.Companion.forTest(
    id: Long,
    email: String,
    password: String
): User {
    return User(
        id = id,
        email = email,
        name = "testName",
        password = password,
        createdAt = LocalDateTime.now(),
        updatedAt = null,
        deletedAt = null
    )
}

private fun RefreshToken.Companion.created(user: User): RefreshToken {
    return RefreshToken.forTest(
        user = user,
        expiresAt = LocalDateTime.now().plusDays(30),
    )
}

private fun RefreshToken.Companion.valid(user: User): RefreshToken {
    return RefreshToken.forTest(
        user = user,
        expiresAt = LocalDateTime.now().plusDays(1),
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
    return RefreshToken(
        id = id,
        userId = user.id,
        token = token,
        expiresAt = expiresAt,
        revoked = revoked
    )
}

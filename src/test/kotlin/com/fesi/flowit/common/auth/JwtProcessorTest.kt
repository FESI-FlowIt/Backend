package com.fesi.flowit.common.auth

import com.fesi.flowit.common.auth.dto.TokenInfo
import com.fesi.flowit.common.response.exceptions.FailToParseJwtException
import com.fesi.flowit.common.response.exceptions.TokenExpiredException
import com.fesi.flowit.user.repository.UserRepository
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.util.*

class JwtProcessorTest : StringSpec({

    val secretKey =
        "40e96460271ece5c44153b6c90bccdc1965cfc8b21568dcb92ade8064173226b4509b3c31faa83d20b53ad35a6b529d42a4e98f967a072bbbde244b9af8236ff"
    lateinit var userRepository: UserRepository
    lateinit var jwtProcessor: JwtProcessor

    beforeEach {
        userRepository = mockk<UserRepository>()
        jwtProcessor = JwtProcessor(secretKey, userRepository)
    }

    "유효한 토큰일 경우" {
        val tokenInfo = TokenInfo.valid()

        canFindUser(userRepository)

        val token = Jwts.builder()
            .subject(tokenInfo.email)
            .claim("userId", tokenInfo.userId.toString())
            .issuedAt(tokenInfo.issuedAt)
            .expiration(tokenInfo.expiration)
            .signWith(jwtProcessor.key)
            .compact()

        jwtProcessor.handle(token)
    }

    "토큰 파싱에 실패할 경우" {
        val wrongKey = Keys.hmacShaKeyFor("wrong-secret-key-for-testing-purpose-only".toByteArray())
        val token = Jwts.builder()
            .subject("test@example.com")
            .claim("userId", 1L)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + 3600000))
            .signWith(wrongKey)
            .compact()

        shouldThrow<FailToParseJwtException> {
            jwtProcessor.unpack(token)
        }
    }

    "토큰이 만료되었을 때" {
        val expiredTokenInfo = TokenInfo.expired()

        val result = jwtProcessor.isTokenExpired(expiredTokenInfo)

        result shouldBe true
    }

    "토큰이 만료되지 않았을 때" {
        val validTokenInfo = TokenInfo.valid()

        val result = jwtProcessor.isTokenExpired(validTokenInfo)

        result shouldBe false
    }

    "토큰 정보로 등록된 사용자가 있을 때" {
        val tokenInfo = TokenInfo.valid()

        canFindUser(userRepository)

        val result = jwtProcessor.isTokenStored(tokenInfo)

        result shouldBe true
    }

    "토큰 정보로 등록된 사용자가 없을 때" {
        val tokenInfo = TokenInfo.notexists()

        cannotFindUser(userRepository)

        jwtProcessor.isTokenStored(tokenInfo) shouldBe false
    }

    "토큰의 유효 기간이 만료되었더라도 재발급한다" {
        canFindUser(userRepository)

        val tokenInfo = TokenInfo.expired()
        val token = jwtProcessor.pack(tokenInfo)

        shouldNotThrow<ExpiredJwtException> {
            val verifyForRegenerate = jwtProcessor.verifyForRegenerate(token)
            verifyForRegenerate shouldBe tokenInfo.email
        }
    }
})

private fun canFindUser(userRepository: UserRepository) {
    every { userRepository.findById(any()) } returns Optional.of(mockk())
}

private fun cannotFindUser(userRepository: UserRepository) {
    every { userRepository.findById(any()) } returns Optional.empty()
}

private fun TokenInfo.Companion.valid(): TokenInfo {
    return TokenInfo.forTest()
}

private fun TokenInfo.Companion.notexists(): TokenInfo {
    return TokenInfo.forTest(userId = 999L)
}

private fun TokenInfo.Companion.expired(): TokenInfo {
    return TokenInfo.forTest(
        issuedAt = Date(System.currentTimeMillis() - 7200000),
        expiration = Date(System.currentTimeMillis() - 3600000) // 1시간 전 만료
    )
}

private fun TokenInfo.Companion.forTest(
    email: String = "test@example.com",
    userId: Long = 1L,
    issuedAt: Date = Date(System.currentTimeMillis() - 3600000), // 1시간 전
    expiration: Date = Date(System.currentTimeMillis() + 3600000) // 1시간 후
): TokenInfo {
    return TokenInfo(
        email = email,
        userId = userId,
        issuedAt = issuedAt,
        expiration = expiration
    )
}

private fun JwtProcessor.pack(tokenInfo: TokenInfo): String {
     return Jwts.builder()
        .subject(tokenInfo.email)
        .claim("userId", tokenInfo.userId.toString())
        .issuedAt(tokenInfo.issuedAt)
        .expiration(tokenInfo.expiration)
        .signWith(this.key)
        .compact()
}
package com.fesi.flowit.common.auth

import com.fesi.flowit.common.auth.dto.*
import com.fesi.flowit.common.auth.dto.expired
import com.fesi.flowit.common.response.exceptions.AuthException
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
    lateinit var customUserDetailsService: CustomUserDetailsService
    lateinit var jwtProcessor: JwtProcessor

    beforeEach {
        userRepository = mockk<UserRepository>()
        customUserDetailsService = mockk<CustomUserDetailsService>()
        jwtProcessor = JwtProcessor(secretKey, userRepository, customUserDetailsService)
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
        val expiredToken = jwtProcessor.pack(TokenInfo.expired())

        shouldThrow<TokenExpiredException> {
            jwtProcessor.unpack(expiredToken)
        }
    }

    "토큰이 만료되지 않았을 때" {
        val freshToken = jwtProcessor.pack(TokenInfo.valid())

        shouldNotThrow<TokenExpiredException> {
            jwtProcessor.unpack(freshToken)
        }
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

    "재발급 시 만료되지 않은 리프레시 토큰으로부터 인증 객체를 가져올 수 있다" {
        val validRefreshToken = jwtProcessor.pack(TokenInfo.validRefresh())

        shouldNotThrow<AuthException> {
            jwtProcessor.unpackRefreshToken(validRefreshToken)
        }

        val expiredRefreshToken = jwtProcessor.pack(TokenInfo.expiredRefresh())

        shouldThrow<AuthException> {
            jwtProcessor.unpackRefreshToken(expiredRefreshToken)
        }
    }
})

private fun canFindUser(userRepository: UserRepository) {
    every { userRepository.findById(any()) } returns Optional.of(mockk())
}

private fun cannotFindUser(userRepository: UserRepository) {
    every { userRepository.findById(any()) } returns Optional.empty()
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
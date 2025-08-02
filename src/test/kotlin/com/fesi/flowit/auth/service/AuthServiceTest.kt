package com.fesi.flowit.auth.service

import com.fesi.flowit.auth.repository.TokenRepository
import com.fesi.flowit.auth.service.dto.SignInDto
import com.fesi.flowit.auth.vo.RefreshToken
import com.fesi.flowit.auth.web.response.RegenerateResponse
import com.fesi.flowit.common.auth.JwtProcessor
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication

class AuthServiceTest : StringSpec({
    lateinit var service: AuthService
    lateinit var jwtGenerator: JwtGenerator
    lateinit var jwtProcessor: JwtProcessor
    lateinit var authenticationManager: AuthenticationManager
    lateinit var refreshTokenRepository: TokenRepository

    beforeTest() {
        jwtGenerator = mockk<JwtGenerator>(relaxed = true)
        jwtProcessor = mockk<JwtProcessor>(relaxed = true)
        authenticationManager = mockk<AuthenticationManager>()
        refreshTokenRepository = mockk<TokenRepository>()
        service =
            AuthService(
                jwtGenerator,
                jwtProcessor,
                authenticationManager,
                refreshTokenRepository
            )
    }

    "로그인 시 입력한 정보로 인증에 실패하면 로그인에 실패한다" {
        every { authenticationManager.authenticate(any()) } throws RuntimeException()

        shouldThrow<RuntimeException> {
            service.signIn(SignInDto("user@gmail.com", "password"))
        }
    }

    "로그인 성공 시 jwt 토큰을 생성한다" {
        val authentication = mockk<Authentication>(relaxed = true)
        every { authentication.principal } returns mockk<User>(relaxed = true)
        every { authenticationManager.authenticate(any()) } returns authentication

        service.signIn(SignInDto("user@gmail.com", "password"))

        verify { jwtGenerator.generateToken(ofType<Authentication>()) }
    }

    "로그인 성공 시 상황에 맞게 jwt refresh 토큰을 처리한다" {
        val authentication = mockk<Authentication>(relaxed = true)
        every { authentication.principal } returns mockk<User>(relaxed = true)
        every { authenticationManager.authenticate(any()) } returns authentication

        service.signIn(SignInDto("user@gmail.com", "password"))

        verify { jwtGenerator.handleRefreshToken(ofType<Authentication>()) }
    }

    "토큰 재발급 시 refresh token이 유효한지 확인한다" {
        every { jwtProcessor.verify(any()) } returns true
        val authentication = mockk<Authentication>(relaxed = true)
        every { jwtProcessor.getAuthenticationFromId(any()) } returns authentication
        hasUserDetails(authentication)
        refreshTokenRepository.canFindTokenByUserId(tokenValue = "refresh_token")

        service.regenerate("access_token", "refresh_token")

        verify { jwtProcessor.verify(any()) }
    }

    "토큰 재발급 시 새로운 access token과 refresh token을 생성한다" {
        every { jwtProcessor.verify(any()) } returns true
        val authentication = mockk<Authentication>(relaxed = true)
        every { jwtProcessor.getAuthenticationFromId(any()) } returns authentication
        hasUserDetails(authentication)
        refreshTokenRepository.canFindTokenByUserId(tokenValue = "refresh_token")
        every { jwtGenerator.generateToken(any()) } returns "newAccessToken"
        every { jwtGenerator.handleRefreshToken(authentication) } returns "newRefreshToken"

        service.regenerate(
            "access_token",
            "refresh_token"
        ) shouldBe instanceOf<RegenerateResponse>()
    }
})

private fun UserRepository.canFindUserByEmail() {
    every { findByEmail(any()) } returns mockk<User>(relaxed = true)
}

private fun TokenRepository.canFindTokenByUserId(tokenValue: String) {
    val refreshToken = mockk<RefreshToken>(relaxed = true)
    every { refreshToken.token } returns tokenValue
    every { findByUserId(any()) } returns refreshToken
}

private fun hasUserDetails(authentication: Authentication) {
    every { authentication.principal } returns mockk<User>(relaxed = true)
}

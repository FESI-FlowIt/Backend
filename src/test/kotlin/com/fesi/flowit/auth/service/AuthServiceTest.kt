package com.fesi.flowit.auth.service

import com.fesi.flowit.auth.service.dto.SignInDto
import com.fesi.flowit.common.auth.JwtProcessor
import com.fesi.flowit.common.auth.PasswordEncryptor
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.mockk.*
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication

class AuthServiceTest : StringSpec({
    lateinit var repository: UserRepository
    lateinit var encryptor: PasswordEncryptor
    lateinit var service: AuthService
    lateinit var jwtGenerator: JwtGenerator
    lateinit var jwtProcessor: JwtProcessor
    lateinit var authenticationManager: AuthenticationManager

    beforeTest() {
        repository = mockk<UserRepository>()
        encryptor = mockk<PasswordEncryptor>(relaxed = true)
        jwtGenerator = mockk<JwtGenerator>(relaxed = true)
        jwtProcessor = mockk<JwtProcessor>(relaxed = true)
        authenticationManager = mockk<AuthenticationManager>()
        service =
            AuthService(repository, encryptor, jwtGenerator, jwtProcessor, authenticationManager)
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

        verify { jwtGenerator.generateTokenWith(ofType<Authentication>()) }
    }

    "로그인 성공 시 상황에 맞게 jwt refresh 토큰을 처리한다" {
        val authentication = mockk<Authentication>(relaxed = true)
        every { authentication.principal } returns mockk<User>(relaxed = true)
        every { authenticationManager.authenticate(any()) } returns authentication

        service.signIn(SignInDto("user@gmail.com", "password"))

        verify { jwtGenerator.handleRefreshTokenWith(ofType<Authentication>()) }
    }

    "토큰 재발급 시 access token이 유효한지 확인한다" {
        every { jwtProcessor.verifyForRegenerate(any()) } returns "user@gmail.com"
        repository.canFindUserByEmail()

        service.regenerate("access_token")

        verify { jwtProcessor.verifyForRegenerate(any()) }
    }

    "토큰 재발급 시 새로운 access token과 refresh token을 생성한다" {
        every { jwtProcessor.verifyForRegenerate(any()) } returns "user@gmail.com"
        repository.canFindUserByEmail()
        every { jwtGenerator.generateToken(any<User>()) } returns "new_access_token"
        every { jwtGenerator.handleRefreshToken(any<User>()) } just runs

        service.regenerate("access_token")

        verify { jwtGenerator.generateToken(any<User>()) }
        verify { jwtGenerator.handleRefreshToken(any<User>()) }
    }
})

private fun UserRepository.canFindUserByEmail() {
    every { findByEmail(any()) } returns mockk<User>(relaxed = true)
}

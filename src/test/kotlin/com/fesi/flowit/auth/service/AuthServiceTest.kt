package com.fesi.flowit.auth.service

import com.fesi.flowit.auth.service.dto.SignInDto
import com.fesi.flowit.common.auth.JwtProcessor
import com.fesi.flowit.common.auth.PasswordEncryptor
import com.fesi.flowit.common.response.exceptions.InvalidPasswordException
import com.fesi.flowit.common.response.exceptions.UserNotExistsException
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.mockk.*

class AuthServiceTest : StringSpec({
    lateinit var repository: UserRepository
    lateinit var encryptor: PasswordEncryptor
    lateinit var service: AuthService
    lateinit var jwtGenerator: JwtGenerator
    lateinit var jwtProcessor: JwtProcessor

    beforeTest() {
        repository = mockk<UserRepository>()
        encryptor = mockk<PasswordEncryptor>(relaxed = true)
        jwtGenerator = mockk<JwtGenerator>(relaxed = true)
        jwtProcessor = mockk<JwtProcessor>(relaxed = true)
        service = AuthService(repository, encryptor, jwtGenerator, jwtProcessor)
    }

    "로그인 시 입력한 이메일로 db에 등록된 회원 정보를 검색한다" {
        every { repository.findByEmail(ofType<String>()) } returns (mockk<User>(relaxed = true))

        service.signIn(SignInDto("user@gmail.com", "password"))

        verify { repository.findByEmail(ofType<String>()) }
    }

    "로그인 시 입력한 이메일로 등록된 회원이 없으면 로그인에 실패한다" {
        every { repository.findByEmail(ofType<String>()) } returns (null)

        shouldThrow<UserNotExistsException> {
            service.signIn(SignInDto("user@gmail.com", "password"))
        }

        verify { repository.findByEmail(ofType<String>()) }
    }

    "로그인 시 입력한 비밀번호가 일치하지 않으면 로그인에 실패한다" {
        val user = mockk<User> { every { password } returns "stored_in_db" }
        every { repository.findByEmail(ofType<String>()) } returns (user)
        every { encryptor.encrypt(ofType<String>()) } returns ("encrypted")

        shouldThrow<InvalidPasswordException> {
            service.signIn(SignInDto("user@gmail.com", "password"))
        }

        verify { repository.findByEmail(ofType<String>()) }
        verify { encryptor.encrypt(ofType<String>()) }
    }

    "로그인 성공 시 jwt 토큰을 생성한다" {
        val user = mockk<User>(relaxed = true) { every { password } returns "stored_in_db" }
        every { repository.findByEmail(ofType<String>()) } returns (user)
        every { encryptor.encrypt(ofType<String>()) } returns ("stored_in_db")

        service.signIn(SignInDto("user@gmail.com", "password"))

        verify { jwtGenerator.generateToken(ofType<User>()) }
    }

    "로그인 성공 시 상황에 맞게 jwt refresh 토큰을 처리한다" {
        val user = mockk<User>(relaxed = true) { every { password } returns "stored_in_db" }
        every { repository.findByEmail(ofType<String>()) } returns (user)
        every { encryptor.encrypt(ofType<String>()) } returns ("stored_in_db")

        service.signIn(SignInDto("user@gmail.com", "password"))

        verify { jwtGenerator.handleRefreshToken(ofType<User>()) }
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

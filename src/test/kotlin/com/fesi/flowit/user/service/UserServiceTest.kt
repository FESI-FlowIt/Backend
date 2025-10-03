package com.fesi.flowit.user.service

import com.fesi.flowit.auth.service.JwtProcessor
import com.fesi.flowit.auth.service.PasswordEncryptor
import com.fesi.flowit.auth.vo.TokenInfo
import com.fesi.flowit.common.auth.dto.valid
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.repository.UserRepository
import com.fesi.flowit.user.service.dto.UserDto
import com.fesi.flowit.user.web.response.UserResponse
import com.fesi.flowit.user.web.response.UserSignedUpResponse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.core.Authentication

class UserServiceTest : StringSpec({
    lateinit var repository: UserRepository
    lateinit var encryptor: PasswordEncryptor
    lateinit var jwtProcessor: JwtProcessor
    lateinit var service: UserService

    beforeTest() {
        repository = mockk<UserRepository>()
        encryptor = mockk<PasswordEncryptor>(relaxed = true)
        jwtProcessor = mockk<JwtProcessor>(relaxed = true)
        service = UserService(repository, encryptor, jwtProcessor)
    }

    "회원 등록 시 db에 회원 정보 저장을 요청한다" {
        every { repository.findByEmail(ofType<String>()) } returns null
        every { repository.save(ofType<User>()) } returns (mockk<User>(relaxed = true))

        service.add(UserDto("user@gmail.com", "test", "password"))

        verify { repository.save(ofType<User>()) }
    }

    "회원 등록 시 등록할 회원의 비밀번호를 해싱한다" {
        every { repository.findByEmail(ofType<String>()) } returns null
        every { repository.save(ofType<User>()) } returns (mockk<User>(relaxed = true))

        service.add(UserDto("user@gmail.com", "test", "password"))

        verify { encryptor.encrypt(any()) }
    }

    "이미 가입한 사용자는 회원가입할 수 없다" {
        every { repository.findByEmail(ofType<String>()) } returns (mockk<User>(relaxed = true))

        shouldThrow<AuthException> {
            service.add(UserDto("user@gmail.com", "test", "password"))
        }

        verify { repository.findByEmail(ofType<String>()) }
    }

    "이메일로 등록된 회원이 있는지 확인한다" {
        every { repository.findByEmail(ofType<String>()) } returns (mockk<User>(relaxed = true))
        service.hasUserWithEmail("user@gmail.com") shouldBe(UserSignedUpResponse(true))

        every { repository.findByEmail(ofType<String>()) } returns null
        service.hasUserWithEmail("user@gmail.com") shouldBe(UserSignedUpResponse(false))
    }

    "액세스 토큰으로 회원 정보를 찾는다" {
        val tokenInfo = TokenInfo.valid()
        val authentication = mockk<Authentication>(relaxed = true)
        hasUserDetails(authentication)

        every { jwtProcessor.unpack(any()) } returns tokenInfo
        every { repository.findByEmail(any()) } returns (mockk<User>(relaxed = true))
        every { jwtProcessor.getAuthentication(tokenInfo) } returns authentication

        service.findUserByToken("accessToken") should beInstanceOf<UserResponse>()
    }

    "만료된 액세스 토큰일 경우 회원 정보 검색에 실패한다" {
        every { jwtProcessor.unpack(any())} throws AuthException.fromCode(ApiResultCode.AUTH_TOKEN_EXPIRED)
        shouldThrow<AuthException> {
            service.findUserByToken("accessToken")
        }
    }

    "액세스 토큰으로 회원 정보 검색에 실패할 경우 예외 처리한다" {
        every { jwtProcessor.unpack(any())} returns TokenInfo.valid()
        every { jwtProcessor.getAuthentication(any()) } throws mockk<AuthException>()

        shouldThrow<AuthException> {
            service.findUserByToken("accessToken")
        }
    }
})

private fun hasUserDetails(authentication: Authentication) {
    every { authentication.principal } returns mockk<User>(relaxed = true)
}

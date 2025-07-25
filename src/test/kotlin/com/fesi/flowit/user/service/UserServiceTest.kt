package com.fesi.flowit.user.service

import com.fesi.flowit.common.auth.PasswordEncryptor
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.common.response.exceptions.UserAlreadySignedUpException
import com.fesi.flowit.user.repository.UserRepository
import com.fesi.flowit.user.service.dto.UserDto
import com.fesi.flowit.user.web.response.UserSignedUpResponse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UserServiceTest : StringSpec({
    lateinit var repository: UserRepository
    lateinit var encryptor: PasswordEncryptor
    lateinit var service: UserService

    beforeTest() {
        repository = mockk<UserRepository>()
        encryptor = mockk<PasswordEncryptor>(relaxed = true)
        service = UserService(repository, encryptor)
    }

    "db에 회원 정보 저장을 요청한다" {
        every { repository.findByEmail(ofType<String>()) } returns null
        every { repository.save(ofType<User>()) } returns (mockk<User>(relaxed = true))

        service.add(UserDto("user@gmail.com", "test", "password"))

        verify { repository.save(ofType<User>()) }
    }

    "등록할 회원의 비밀번호를 해싱한다" {
        every { repository.findByEmail(ofType<String>()) } returns null
        every { repository.save(ofType<User>()) } returns (mockk<User>(relaxed = true))

        service.add(UserDto("user@gmail.com", "test", "password"))

        verify { encryptor.encrypt(any()) }
    }

    "이미 가입한 사용자는 회원가입할 수 없다" {
        every { repository.findByEmail(ofType<String>()) } returns (mockk<User>(relaxed = true))

        shouldThrow<UserAlreadySignedUpException> {
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
})

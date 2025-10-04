package com.fesi.flowit.user.web

import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.user.controller.UserController
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.service.UserService
import com.fesi.flowit.user.dto.SignUpRequestDto
import com.fesi.flowit.user.dto.SignUpResponseDto
import com.fesi.flowit.user.dto.UserExistCheckResponseDto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.types.beInstanceOf
import io.mockk.every
import io.mockk.mockk
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity

class UserControllerTest : StringSpec({

    "회원가입 요청을 받을 수 있다" {
        val controller = UserController(mockk<UserService>(relaxed = true))
        val request = SignUpRequestDto("user@gmail.com", "test", "password")

        controller.signUp(request)
    }

    "가입 여부를 확인하는 요청을 받을 수 있다" {
        val controller = UserController(mockk<UserService>(relaxed = true))
        val queryParam = "user@gmail.com"

        controller.hasSignedUp(queryParam)
    }

    "가입 여부를 알 수 있는 응답을 내보낼 수 있다" {
        val service = mockk<UserService>(relaxed = true)
        every { service.checkExistUserByEmail(ofType<String>()) } returns (UserExistCheckResponseDto(true))

        val controller = UserController(service)
        val queryParam = "user@gmail.com"

        controller.hasSignedUp(queryParam) should beInstanceOf<ResponseEntity<ApiResult<UserExistCheckResponseDto>>>()
    }

    "회원 정보 조회 요청을 받을 수 있다" {
        val request = mockk<HttpServletRequest>() {
            every { getHeader("Authorization") } returns "accessToken"
        }
        val controller = UserController(mockk<UserService>(relaxed = true))

        controller.getUserInfo(request)
    }

    "회원 정보 조회 결과를 내보낼 수 있다" {
        val request = mockk<HttpServletRequest>() {
            every { getHeader("Authorization") } returns "accessToken"
        }
        val service = mockk<UserService>(relaxed = true)
        every { service.findUserByToken(any()) } returns SignUpResponseDto.from(mockk<User>(relaxed = true))
        val controller = UserController(service)

        controller.getUserInfo(request) should beInstanceOf<ResponseEntity<ApiResult<SignUpResponseDto>>>()
    }
})

package com.fesi.flowit.auth.local.web

import com.fesi.flowit.auth.controller.AuthController
import com.fesi.flowit.auth.dto.AccessTokenReissueRequestDto
import com.fesi.flowit.auth.service.AuthService
import com.fesi.flowit.auth.dto.SignInRequestDto
import com.fesi.flowit.auth.dto.AccessTokenReissueResponseDto
import com.fesi.flowit.auth.dto.SignInResponseDto
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk

class AuthControllerTest : StringSpec({

    "로그인 요청을 받을 수 있다" {
        val service = mockk<AuthService>(relaxed = true)
        every { service.signIn(any(), any()) } returns mockk<SignInResponseDto>()
        val controller = AuthController(service)
        val request = SignInRequestDto("user@gmail.com", "password")

        controller.signIn(request)
    }

    "토큰 재발급 요청을 받을 수 있다" {
        val request = AccessTokenReissueRequestDto( "refresh_token", "refresh_token_val")

        val service = mockk<AuthService>(relaxed = true)
        every { service.regenerate(any()) } returns mockk<AccessTokenReissueResponseDto>()
        val controller = AuthController(service)

        controller.regenerate(request)
    }
})

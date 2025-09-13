package com.fesi.flowit.auth.local.web

import com.fesi.flowit.auth.local.service.AuthService
import com.fesi.flowit.auth.local.web.request.RegenerateRequest
import com.fesi.flowit.auth.local.web.request.SignInRequest
import com.fesi.flowit.auth.local.web.response.RegenerateResponse
import com.fesi.flowit.auth.local.web.response.SignInResponse
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk

class AuthControllerTest : StringSpec({

    "로그인 요청을 받을 수 있다" {
        val service = mockk<AuthService>(relaxed = true)
        every { service.signIn(any()) } returns mockk<SignInResponse>()
        val controller = AuthController(service)
        val request = SignInRequest("user@gmail.com", "password")

        controller.signIn(request)
    }

    "토큰 재발급 요청을 받을 수 있다" {
        val request = RegenerateRequest("refresh_token", "refresh_token_val")

        val service = mockk<AuthService>(relaxed = true)
        every { service.regenerate(any()) } returns mockk<RegenerateResponse>()
        val controller = AuthController(service)

        controller.regenerate(request)
    }
})

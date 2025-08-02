package com.fesi.flowit.auth.web

import com.fesi.flowit.auth.service.AuthService
import com.fesi.flowit.auth.web.request.SignInRequest
import com.fesi.flowit.auth.web.response.RegenerateResponse
import com.fesi.flowit.auth.web.response.SignInResponse
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class AuthControllerTest : StringSpec({

    "로그인 요청을 받을 수 있다" {
        val service = mockk<AuthService>(relaxed = true)
        every {service.signIn(any())} returns Pair(mockk<SignInResponse>(), "")
        val controller = AuthController(service)
        val request = SignInRequest("user@gmail.com", "password")
        val response = mockk<HttpServletResponse>(relaxUnitFun = true)

        controller.signIn(request, response)
    }

    "토큰 재발급 요청을 받을 수 있다" {
        val request = mockk<HttpServletRequest>() {
            every { getHeader("Authorization") } returns "accessToken"
        }

        val service = mockk<AuthService>(relaxed = true)
        every {service.regenerate(any()) } returns mockk<RegenerateResponse>()
        val controller = AuthController(service)

        controller.regenerate(request)
    }
})

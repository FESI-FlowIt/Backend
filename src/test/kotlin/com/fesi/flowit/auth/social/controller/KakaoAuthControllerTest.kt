package com.fesi.flowit.auth.social.controller

import com.fesi.flowit.auth.social.service.KakaoAuthService
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk

class KakaoAuthControllerTest : StringSpec({

    "클라이언트는 카카오로부터 토큰 발급 요청에 쓸 수 있는 코드값을 받는다" {}

    "카카오 액세스 토큰 발급 요청을 받을 수 있다" {
        val service = mockk<KakaoAuthService>(relaxed = true)
        every { service.fetchAccessToken(any()) } returns ""
        val controller = KakaoAuthControllerImpl(service, "redirectUrl")

        controller.callback("code")
    }

})

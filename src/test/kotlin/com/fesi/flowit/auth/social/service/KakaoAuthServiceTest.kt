package com.fesi.flowit.auth.social.service

import com.fesi.flowit.auth.social.dto.KakaoTokenResponseDto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk

class KakaoAuthServiceTest : StringSpec({

    "카카오에 액세스 토큰 발급 요청을 할 수 있다" {
        val requester = mockk<KakaoApiRequester>()
        every { requester.requestAccessToken(any(), any()) } returns mockk<KakaoTokenResponseDto>(
            relaxed = true
        )

        val service = KakaoAuthService("client_id", "redirect_url", requester)

        service.fetchAccessToken("code")
    }

    "액세스 토큰 발급 요청 uri를 만들 수 있다" {
        val requester = mockk<KakaoApiRequester>()
        val service = KakaoAuthService("abc", "redirect_url", requester)
        val uri = service.makeReqUri("xyz")
        uri shouldBe (
                "https://kauth.kakao.com/oauth/token"
                )
    }

    "카카오로부터 받은 데이터 중 액세스 토큰 값만 추출한다" {
        val requester = mockk<KakaoApiRequester>()
        every { requester.requestAccessToken(any(), any()) } returns mockk<KakaoTokenResponseDto>(
            relaxed = true
        )

        val service = KakaoAuthService("client_id", "redirect_url", requester)

        service.fetchAccessToken("code") shouldNotBe null
    }
})

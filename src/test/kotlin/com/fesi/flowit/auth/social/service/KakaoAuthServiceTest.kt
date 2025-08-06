package com.fesi.flowit.auth.social.service

import com.fesi.flowit.auth.social.dto.KakaoAccount
import com.fesi.flowit.auth.social.dto.KakaoTokenResponseDto
import com.fesi.flowit.auth.social.dto.KakaoUserInfoResponseDto
import com.fesi.flowit.common.response.exceptions.AuthException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import java.util.*

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

    "카카오에 회원 정보 조회 요청을 할 수 있다" {
        val requester = mockk<KakaoApiRequester>()
        every { requester.requestUserInfo(any(), any()) } returns mockk<KakaoUserInfoResponseDto>(
            relaxed = true
        )

        val service = KakaoAuthService("client_id", "redirect_url", requester)

        service.fetchUserInfo("access_token")
    }

    "사용자 이메일 주소가 유효한지 확인한다" {
        val requester = mockk<KakaoApiRequester>()
        val service = KakaoAuthService("client_id", "redirect_url", requester)

        val cases = listOf(
            KakaoAccount(isEmailValid = false, isEmailVerified = true, email = "x@y.com"), // invalid email
            KakaoAccount(isEmailValid = true, isEmailVerified = false, email = "x@y.com"), // not verified email
            KakaoAccount(isEmailValid = true, isEmailVerified = true, email = null), // no email
        )

        cases.forEach { account ->
            val userInfo = KakaoUserInfoResponseDto(
                id = 1, hasSignedUp = null, connectedAt = Date(),
                properties = null, kakaoAccount = account
            )

            shouldThrow<AuthException> {
                service.validateUserInfo(userInfo)
            }
        }
    }

    "이미 다른 인증 방식을 이용해 해당 이메일로 가입했다면 예외 처리한다" {}
    "카카오 회원이고, 서버에 등록되지 않은 사용자면 회원가입 및 로그인한다" {}
    "카카오 회원이고, 서버에 이미 등록된 사용자면 로그인한다" {}
    "사용자가 서비스 기능 이용 시 인증에 쓸 수 있는 액세스 토큰과 리프레시 토큰을 발급한다" {}
})

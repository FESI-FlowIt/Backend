package com.fesi.flowit.auth.social.service

import com.fesi.flowit.auth.service.JwtGenerator
import com.fesi.flowit.auth.social.dto.KakaoAccount
import com.fesi.flowit.auth.social.dto.KakaoTokenResponseDto
import com.fesi.flowit.auth.social.dto.KakaoUserInfoResponseDto
import com.fesi.flowit.auth.social.dto.Profile
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.authentication.AuthenticationManager
import java.util.*

class KakaoAuthServiceTest : StringSpec({

    lateinit var requester: KakaoApiRequester
    lateinit var service: KakaoAuthService
    lateinit var userRepository: UserRepository
    lateinit var jwtGenerator: JwtGenerator
    lateinit var authenticationManager: AuthenticationManager

    beforeEach {
        requester = mockk<KakaoApiRequester>()
        userRepository = mockk<UserRepository>()
        jwtGenerator = mockk<JwtGenerator>()
        authenticationManager = mockk<AuthenticationManager>()
    }

    "카카오에 액세스 토큰 발급 요청을 할 수 있다" {
        every { requester.requestAccessToken(any(), any()) } returns mockk<KakaoTokenResponseDto>(
            relaxed = true
        )

        service = KakaoAuthService(
            "client_id",
            "redirect_url",
            requester,
            userRepository,
            jwtGenerator,
            authenticationManager
        )

        service.fetchAccessToken("code")
    }

    "액세스 토큰 발급 요청 uri를 만들 수 있다" {
        service = KakaoAuthService(
            "abc",
            "redirect_url",
            requester,
            userRepository,
            jwtGenerator,
            authenticationManager
        )
        val uri = service.makeReqUri("xyz")
        uri shouldBe (
                "https://kauth.kakao.com/oauth/token"
                )
    }

    "카카오로부터 받은 데이터 중 액세스 토큰 값만 추출한다" {
        every { requester.requestAccessToken(any(), any()) } returns mockk<KakaoTokenResponseDto>(
            relaxed = true
        )

        service = KakaoAuthService(
            "client_id",
            "redirect_url",
            requester,
            userRepository,
            jwtGenerator,
            authenticationManager
        )

        service.fetchAccessToken("code") shouldNotBe null
    }

    "카카오에 회원 정보 조회 요청을 할 수 있다" {
        every { requester.requestUserInfo(any(), any()) } returns mockk<KakaoUserInfoResponseDto>(
            relaxed = true
        )

        service = KakaoAuthService(
            "client_id",
            "redirect_url",
            requester,
            userRepository,
            jwtGenerator,
            authenticationManager
        )

        service.fetchUserInfo("access_token")
    }

    "사용자 이메일 주소가 유효한지 확인한다" {
        service = KakaoAuthService(
            "client_id",
            "redirect_url",
            requester,
            userRepository,
            jwtGenerator,
            authenticationManager
        )

        val cases = listOf(
            KakaoAccount(
                profileNicknameNeedsAgreement = true,
                profile = Profile(nickname = "nickname"),
                isEmailValid = false,
                isEmailVerified = true,
                email = "x@y.com",
            ), // invalid email
            KakaoAccount(
                profileNicknameNeedsAgreement = true,
                profile = Profile(nickname = "nickname"),
                isEmailValid = true,
                isEmailVerified = false,
                email = "x@y.com"
            ), // not verified email
            KakaoAccount(
                profileNicknameNeedsAgreement = true,
                profile = Profile(nickname = "nickname"),
                isEmailValid = true,
                isEmailVerified = true,
                email = null
            ), // no email
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

    "oauth가 아닌 인증 방식으로 가입한 이메일인지 확인할 수 있다" {
        service = KakaoAuthService(
            "client_id",
            "redirect_url",
            requester,
            userRepository,
            jwtGenerator,
            authenticationManager
        )

        every { userRepository.findByEmailAndProvider(any(), any()) } returns null
        service.isLocalAccountExists("x@y.com") shouldBe false

        every { userRepository.findByEmailAndProvider(any(), any()) } returns mockk<User>(relaxed = true)
        service.isLocalAccountExists("x@y.com") shouldBe true
    }

    "카카오 회원이고, 서버에 등록되지 않은 사용자면 회원가입 및 로그인한다" {}
    "카카오 회원이고, 서버에 이미 등록된 사용자면 로그인한다" {}
    "사용자가 서비스 기능 이용 시 인증에 쓸 수 있는 액세스 토큰과 리프레시 토큰을 발급한다" {}
})

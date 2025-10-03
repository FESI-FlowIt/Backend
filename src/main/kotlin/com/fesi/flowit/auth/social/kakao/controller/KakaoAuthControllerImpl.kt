package com.fesi.flowit.auth.social.kakao.controller

import com.fesi.flowit.auth.social.kakao.dto.KakaoSignInResponse
import com.fesi.flowit.auth.social.kakao.service.KakaoAuthService
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@Tag(name = "카카오 인증")
@RestController
class KakaoAuthControllerImpl(
    private val service: KakaoAuthService,
    @Value("\${auth.kakao.request-uri}")
    private val redirectUri: String
) : KakaoAuthController {

    @PostMapping("/oauth")
    override fun signIn(@RequestParam code: String): ResponseEntity<ApiResult<KakaoSignInResponse>> {
        return ApiResponse.ok(service.authenticate(code))
    }

    @GetMapping("/oauth/callback")
    override fun callback(@RequestParam code: String): String {

        val frontendRedirectUrl = UriComponentsBuilder
            .fromUriString(redirectUri)
            .queryParam("code", code)
            .build()
            .toUriString()

        return "redirect:$frontendRedirectUrl"
    }
}
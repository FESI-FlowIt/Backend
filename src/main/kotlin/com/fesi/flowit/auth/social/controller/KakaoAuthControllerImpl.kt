package com.fesi.flowit.auth.social.controller

import com.fesi.flowit.auth.social.service.KakaoAuthService
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "카카오 인증")
@RestController
class KakaoAuthControllerImpl(private val service: KakaoAuthService) {

    @GetMapping("/callback")
    fun callback(@RequestParam code: String): ResponseEntity<ApiResult<String>> {
        return ApiResponse.ok(service.fetchAccessToken(code))
    }
}
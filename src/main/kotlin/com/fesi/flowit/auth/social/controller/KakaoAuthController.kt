package com.fesi.flowit.auth.social.controller

import com.fesi.flowit.auth.local.web.response.SignInResponse
import com.fesi.flowit.auth.social.dto.KakaoSignInResponse
import com.fesi.flowit.common.response.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam

interface KakaoAuthController {

    @Operation(
        summary = "카카오 로그인",
        description = """
            [GET] http://IP:PORT/oauth?code=xyz
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "카카오 로그인 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = SignInResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증 실패",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = String::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 데이터",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = String::class)
                )]
            )
        ]
    )
    fun callback(@RequestParam code: String): ResponseEntity<ApiResult<KakaoSignInResponse>>
}
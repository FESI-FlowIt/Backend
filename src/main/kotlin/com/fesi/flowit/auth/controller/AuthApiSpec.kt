package com.fesi.flowit.auth.controller

import com.fesi.flowit.auth.dto.AccessTokenReissueRequestDto
import com.fesi.flowit.auth.dto.SignInRequestDto
import com.fesi.flowit.auth.dto.AccessTokenReissueResponseDto
import com.fesi.flowit.auth.dto.SignInResponseDto
import com.fesi.flowit.common.response.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

interface AuthApiSpec {

    @Operation(
        summary = "사용자 로그인",
        description = """
            [POST] http://IP:PORT/auths/signIn
            {
                "email": "user@example.com",
                "password": "password123"
            }
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "로그인 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = SignInResponseDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증 실패 (잘못된 이메일 또는 비밀번호)",
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
    @PostMapping("/auths/signIn")
    fun signIn(
        @RequestBody signInRequest: SignInRequestDto
    ): ResponseEntity<ApiResult<SignInResponseDto>>

    @Operation(
        summary = "토큰 재발급",
        description = """
            토큰을 재발급합니다
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "토큰 재발급 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = AccessTokenReissueResponseDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 데이터",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiResult.Exception::class)
                )]
            )
        ]
    )
    fun regenerate(
        request: AccessTokenReissueRequestDto
    ): ResponseEntity<ApiResult<AccessTokenReissueResponseDto>>
}
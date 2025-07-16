package org.swyp.weddy.domain.auth.web

import com.fesi.flowit.auth.web.request.SignInRequest
import com.fesi.flowit.auth.web.response.SignInResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

interface AuthApiSpec {

    @Tag(name = "auth", description = "인증 관련 API")
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
                    schema = Schema(implementation = SignInResponse::class)
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
        @RequestBody signInRequest: SignInRequest,
        response: HttpServletResponse
    ): ResponseEntity<SignInResponse>
}
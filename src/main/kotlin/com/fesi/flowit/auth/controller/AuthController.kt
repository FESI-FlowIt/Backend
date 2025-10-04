package com.fesi.flowit.auth.controller

import com.fesi.flowit.auth.service.AuthService
import com.fesi.flowit.auth.dto.AccessTokenReissueRequestDto
import com.fesi.flowit.auth.dto.AccessTokenReissueResponseDto
import com.fesi.flowit.auth.dto.SignInRequestDto
import com.fesi.flowit.auth.dto.SignInResponseDto
import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

private val log = loggerFor<AuthController>()

@Tag(name = "인증")
@RestController
class AuthController(
    private val service: AuthService
): AuthApiSpec {
    @PostMapping("/auths/signIn")
    override fun signIn(
        @RequestBody signInRequest: SignInRequestDto
    ): ResponseEntity<ApiResult<SignInResponseDto>> {
        log.debug(">> request signIn(${signInRequest.email})")
        val authResponse = service.signIn(signInRequest.email, signInRequest.password)

        return ApiResponse.ok(authResponse)
    }

    @PostMapping("/auths/tokens")
    override fun regenerate(
        @RequestBody request: AccessTokenReissueRequestDto
    ): ResponseEntity<ApiResult<AccessTokenReissueResponseDto>> {
        log.debug(">> request regenerate(${request})")

        val response = service.regenerate(request.refreshToken)
        return ApiResponse.ok(response)
    }
}
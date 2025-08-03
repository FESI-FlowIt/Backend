package com.fesi.flowit.auth.web

import com.fesi.flowit.auth.service.AuthService
import com.fesi.flowit.auth.service.dto.SignInDto
import com.fesi.flowit.auth.web.request.RegenerateRequest
import com.fesi.flowit.auth.web.request.SignInRequest
import com.fesi.flowit.auth.web.response.RegenerateResponse
import com.fesi.flowit.auth.web.response.SignInResponse
import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.swyp.weddy.domain.auth.web.AuthApiSpec

private val log = loggerFor<AuthController>()

@Tag(name = "인증")
@RestController
class AuthController(
    private val service: AuthService
): AuthApiSpec {
    @PostMapping("/auths/signIn")
    override fun signIn(
        @RequestBody signInRequest: SignInRequest
    ): ResponseEntity<ApiResult<SignInResponse>> {
        log.debug(">> request signIn(${signInRequest})")
        val dto = SignInDto.from(signInRequest)
        val authResponse = service.signIn(dto)

        return ApiResponse.ok(authResponse)
    }

    @PostMapping("/auths/tokens")
    override fun regenerate(
        @RequestBody request: RegenerateRequest
    ): ResponseEntity<ApiResult<RegenerateResponse>> {
        log.debug(">> request regenerate(${request})")

        val response = service.regenerate(request.refreshToken)
        return ApiResponse.ok(response)
    }
}
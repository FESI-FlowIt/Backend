package com.fesi.flowit.auth.web

import com.fesi.flowit.auth.service.AuthService
import com.fesi.flowit.auth.service.dto.SignInDto
import com.fesi.flowit.auth.web.request.SignInRequest
import com.fesi.flowit.auth.web.response.RegenerateResponse
import com.fesi.flowit.auth.web.response.SignInResponse
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.swyp.weddy.domain.auth.web.AuthApiSpec

@RestController
class AuthController(
    private val service: AuthService
): AuthApiSpec {
    @PostMapping("/auths/signIn")
    override fun signIn(
        @RequestBody signInRequest: SignInRequest,
        response: HttpServletResponse
    ): ResponseEntity<ApiResult<SignInResponse>> {
        val dto = SignInDto.from(signInRequest)
        val (authResponse, accessToken) = service.signIn(dto)

        response.setHeader("Authorization", "Bearer $accessToken")

        return ApiResponse.ok(authResponse)
    }

    @PostMapping("/auths/tokens")
    fun regenerate(request: HttpServletRequest): ResponseEntity<ApiResult<RegenerateResponse>> {
        val accessToken = request.getHeader("Authorization")
        val response = service.regenerate(accessToken)
        return ApiResponse.ok(response)
    }
}
package com.fesi.flowit.auth.web

import com.fesi.flowit.auth.service.AuthService
import com.fesi.flowit.auth.service.dto.SignInDto
import com.fesi.flowit.auth.web.request.SignInRequest
import com.fesi.flowit.auth.web.response.RegenerateResponse
import com.fesi.flowit.auth.web.response.SignInResponse
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.common.util.extractAccessToken
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.swyp.weddy.domain.auth.web.AuthApiSpec

@Tag(name = "인증")
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
        val (authResponse, accessToken, refreshToken) = service.signIn(dto)

        response.setHeader("Authorization", "Bearer $accessToken")
        if (refreshToken != "") {
            response.addCookie(Cookie("refreshToken", refreshToken))
        }

        return ApiResponse.ok(authResponse)
    }

    @PostMapping("/auths/tokens")
    override fun regenerate(request: HttpServletRequest): ResponseEntity<ApiResult<RegenerateResponse>> {
        val authHeader = request.getHeader("Authorization")
        val accessToken = authHeader.extractAccessToken()

        val response = service.regenerate(accessToken)
        return ApiResponse.ok(response)
    }
}
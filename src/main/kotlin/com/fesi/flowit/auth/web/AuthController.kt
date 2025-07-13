package com.fesi.flowit.auth.web

import com.fesi.flowit.auth.service.AuthService
import com.fesi.flowit.auth.service.dto.SignInDto
import com.fesi.flowit.auth.web.request.SignInRequest
import com.fesi.flowit.auth.web.response.SignInResponse
import com.fesi.flowit.user.service.dto.UserDto
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val service: AuthService
) {
    @PostMapping("/auths/signIn")
    fun signIn(
        @RequestBody signInRequest: SignInRequest,
        response: HttpServletResponse
    ): ResponseEntity<SignInResponse> {
        val dto = SignInDto.from(signInRequest)
        val (authResponse, accessToken) = service.signIn(dto)

        response.setHeader("Authorization", "Bearer $accessToken")
        return ResponseEntity.ok().body(authResponse)
    }
}
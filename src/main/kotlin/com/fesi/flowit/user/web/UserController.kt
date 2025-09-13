package com.fesi.flowit.user.web

import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.user.service.UserService
import com.fesi.flowit.user.service.dto.UserDto
import com.fesi.flowit.user.web.request.UserRequest
import com.fesi.flowit.user.web.response.UserResponse
import com.fesi.flowit.user.web.response.UserSignedUpResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "회원")
@RestController
class UserController(
    private val service: UserService
) : UserApiSpec {
    @PostMapping("/users")
    override fun signUp(@RequestBody userRequest: UserRequest): ResponseEntity<ApiResult<UserResponse>> {
        val dto = UserDto.from(userRequest)
        val response = service.add(dto)
        return ApiResponse.ok(response)
    }

    @GetMapping("/users")
    override fun hasSignedUp(@RequestParam email: String): ResponseEntity<ApiResult<UserSignedUpResponse>> {
        val response = service.hasUserWithEmail(email)
        return ApiResponse.ok(response)
    }

    @GetMapping("/users/me")
    override fun getUserInfo(request: HttpServletRequest): ResponseEntity<ApiResult<UserResponse>> {
        val authHeader = request.getHeader("Authorization")
        val accessToken = authHeader.extractAccessToken()

        val response = service.findUserByToken(accessToken)
        return ApiResponse.ok(response)
    }

    private fun String.extractAccessToken(): String {
        val bearerPrefix = "Bearer "
        return this.removePrefix(bearerPrefix).trim()
    }
}
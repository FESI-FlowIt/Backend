package com.fesi.flowit.user.controller

import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.user.service.UserService
import com.fesi.flowit.user.dto.SignUpRequestDto
import com.fesi.flowit.user.dto.SignUpResponseDto
import com.fesi.flowit.user.dto.UserExistCheckResponseDto
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "회원")
@RestController
class UserController(
    private val userService: UserService
) : UserApiSpec {
    @PostMapping("/users")
    override fun signUp(@RequestBody userRequest: SignUpRequestDto): ResponseEntity<ApiResult<SignUpResponseDto>> {
        return ApiResponse.ok(userService.signUp(userRequest.email, userRequest.name, userRequest.password))
    }

    @GetMapping("/users")
    override fun hasSignedUp(@RequestParam email: String): ResponseEntity<ApiResult<UserExistCheckResponseDto>> {
        return ApiResponse.ok(userService.checkExistUserByEmail(email))
    }

    @GetMapping("/users/me")
    override fun getUserInfo(request: HttpServletRequest): ResponseEntity<ApiResult<SignUpResponseDto>> {
        val authorizationHeader = request.getHeader("Authorization")
        val accessToken = authorizationHeader.removePrefix("Bearer ").trim()

        val response = userService.findUserByToken(accessToken)
        return ApiResponse.ok(response)
    }
}
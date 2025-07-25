package com.fesi.flowit.user.web

import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.user.service.UserService
import com.fesi.flowit.user.service.dto.UserDto
import com.fesi.flowit.user.web.request.UserRequest
import com.fesi.flowit.user.web.response.UserResponse
import com.fesi.flowit.user.web.response.UserSignedUpResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
}
package com.fesi.flowit.user.web

import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.user.service.UserService
import com.fesi.flowit.user.service.dto.UserDto
import com.fesi.flowit.user.web.request.UserRequest
import com.fesi.flowit.user.web.response.UserResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

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
}
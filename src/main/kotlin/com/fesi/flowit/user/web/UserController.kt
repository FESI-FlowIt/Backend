package com.fesi.flowit.user.web

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
) {
    @PostMapping("/users")
    fun signUp(@RequestBody userRequest: UserRequest): ResponseEntity<UserResponse> {
        val dto = UserDto.from(userRequest)
        val response = service.add(dto)
        return ResponseEntity.ok().body(response)
    }
}
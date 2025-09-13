package com.fesi.flowit.user.service.dto

import com.fesi.flowit.user.web.request.UserRequest

data class UserDto(
    val email: String,
    val name: String,
    val password: String
) {
    companion object {
        fun from(userRequest: UserRequest): UserDto {
            return UserDto(userRequest.email, userRequest.name, userRequest.password)
        }
    }
}

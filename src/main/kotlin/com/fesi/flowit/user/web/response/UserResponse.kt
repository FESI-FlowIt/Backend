package com.fesi.flowit.user.web.response

import com.fesi.flowit.user.entity.User
import java.time.LocalDateTime

data class UserResponse(
    var id: Long,
    val email: String,
    val name: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                user.id,
                user.email,
                user.name,
                user.createdAt,
                user.updatedAt
            )
        }
    }
}

package com.fesi.flowit.user.dto

import com.fesi.flowit.user.entity.User
import java.time.LocalDateTime

data class SignUpResponseDto(
    var id: Long,
    val email: String,
    val name: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(user: User): SignUpResponseDto {
            return SignUpResponseDto(
                user.id,
                user.email,
                user.name,
                user.createdAt,
                user.updatedAt
            )
        }
    }
}
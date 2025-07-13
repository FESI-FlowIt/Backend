package com.fesi.flowit.auth.web.response

import com.fesi.flowit.user.entity.User

class SignInResponse(
    val id: Long,
    val email: String
) {
    companion object {
        fun of(user: User): SignInResponse {
            return SignInResponse(
                user.id,
                user.email
            )
        }
    }
}

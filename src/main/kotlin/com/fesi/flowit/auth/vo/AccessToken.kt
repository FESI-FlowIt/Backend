package com.fesi.flowit.auth.vo

data class AccessToken(
    val token: String,
    val expiresIn: Long
) {
    companion object {
        fun of(token: String, expiresIn: Long): AccessToken {
            return AccessToken(token, expiresIn)
        }
    }
}
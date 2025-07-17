package com.fesi.flowit.common.auth.dto

import java.util.*

data class TokenInfo(
    val email: String,
    val userId: Long,
    val issuedAt: Date,
    val expiration: Date
) {
    companion object
}
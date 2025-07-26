package com.fesi.flowit.common.auth.dto

import java.util.*

fun TokenInfo.Companion.valid(): TokenInfo {
    return TokenInfo.forTest()
}

fun TokenInfo.Companion.notexists(): TokenInfo {
    return TokenInfo.forTest(userId = 999L)
}

fun TokenInfo.Companion.expired(): TokenInfo {
    return TokenInfo.forTest(
        issuedAt = Date(System.currentTimeMillis() - 7200000),
        expiration = Date(System.currentTimeMillis() - 3600000) // 1시간 전 만료
    )
}

fun TokenInfo.Companion.forTest(
    email: String = "test@example.com",
    userId: Long = 1L,
    issuedAt: Date = Date(System.currentTimeMillis() - 3600000), // 1시간 전
    expiration: Date = Date(System.currentTimeMillis() + 3600000) // 1시간 후
): TokenInfo {
    return TokenInfo(
        email = email,
        userId = userId,
        issuedAt = issuedAt,
        expiration = expiration
    )
}

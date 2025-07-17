package com.fesi.flowit.common.response

/**
 * API 응답 코드 정의
 */

enum class ApiResultCode(
    val code: String,
    val message: String
) {
    // 0-1000: Common Code
    SUCCESS("0000", "OK"),
    CREATED("0201", "Created"),

    BAD_REQUEST("0400", "Bad Request"),
    INTERNAL_ERROR("0500", "Unexpected Error");
}
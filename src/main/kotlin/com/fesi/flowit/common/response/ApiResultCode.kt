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
    UNAUTHORIZED("0401", "Unauthorized"),
    CONFLICT("0409", "Conflict"),
    INTERNAL_ERROR("0500", "Unexpected Error"),

    // 1001-1999: Auth
    AUTH_USER_NOT_EXISTS("1001", "User not exists"),
    AUTH_FAIL_TO_PARSE_JWT("1002", "Fail To Parse JWT"),
}
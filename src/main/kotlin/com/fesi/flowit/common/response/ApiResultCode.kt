package com.fesi.flowit.common.response

/**
 * API 응답 코드 정의
 */

enum class ApiResultCode(
    val code: String,
    val message: String
) {
    // 0-1000: General Code
    SUCCESS("0000", "OK"),
    CREATED("0201", "Created"),
    BAD_REQUEST("0400", "Bad Request"),
    INTERNAL_ERROR("0500", "Unexpected Error"),

    // 2000-3000: Goal
    GOAL_INVALID_DUE_DATETIME("3000", "Due date must be later than the creation time"),
    GOAL_INVALID_NAME_LENGTH("3001", "Goal name must be 30 characters or less"),

    // 9000-9999: Common Code
    RGB_FORMAT_INVALID("9000", "Invalid RGB code format")
    ;
}
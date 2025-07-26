package com.fesi.flowit.common.response

/**
 * API 응답 코드 정의
 */

enum class ApiResultCode(
    val code: String,
    val message: String
) {
    // 0-999: General Code
    SUCCESS("0000", "OK"),
    CREATED("0201", "Created"),
    BAD_REQUEST("0400", "Bad Request"),
    INTERNAL_ERROR("0500", "Unexpected Error"),
  
    // 1001-1999: Auth
    AUTH_USER_NOT_EXISTS("1001", "User not exists"),
    AUTH_FAIL_TO_PARSE_JWT("1002", "Fail To Parse JWT"),

    // 3000-3999: Goal
    GOAL_INVALID_DUE_DATETIME("3000", "Due date must be later than the creation time"),
    GOAL_INVALID_NAME_LENGTH("3001", "Goal name must be 30 characters or less"),
    GOAL_ID_INVALID("3002", "Goal-id is invalid."),

    // 4000-4999: Todo
    TODO_NOT_FOUND("4000", "Not found Todo"),
    TODO_INVALID_GOAL("4001", "Goal with todo is invalid"),

    // 9000-9999: Common Code
    RGB_FORMAT_INVALID("9000", "Invalid RGB code format")
    UNAUTHORIZED("0401", "Unauthorized"),
    CONFLICT("0409", "Conflict"),
    INTERNAL_ERROR("0500", "Unexpected Error"),

  ;
}
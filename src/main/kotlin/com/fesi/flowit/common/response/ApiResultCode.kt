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
    UNAUTHORIZED("0401", "Unauthorized"),
    FORBIDDEN("0403", "Forbidden"),
    CONFLICT("0409", "Conflict"),
    INTERNAL_ERROR("0500", "Unexpected Error"),
  
    // 1001-1999: Auth
    AUTH_USER_NOT_EXISTS("1001", "User not exists"),
    AUTH_FAIL_TO_PARSE_JWT("1002", "Fail To Parse JWT"),

    // 3000-3999: Goal
    GOAL_NOT_FOUND("3000", "Not found goal."),
    GOAL_INVALID_DUE_DATETIME("3001", "Due date must be later than the creation time"),
    GOAL_INVALID_NAME_LENGTH("3002", "Goal name must be 30 characters or less"),
    GOAL_ID_INVALID("3003", "Goal-id is invalid."),
    GOAL_NOT_MATCH_USER("3004", "The user has not this goal"),

    // 4000-4999: Todo
    TODO_NOT_FOUND("4000", "Not found Todo"),
    TODO_INVALID_ID("4001", "Todo id is invalid"),
    TODO_INVALID_GOAL("4002", "Goal with todo is invalid"),
    TODO_NOT_MATCH_USER("4003", "The user has not this todo"),

    // 5000-5999: Schedule
    SCHED_NOT_FOUND("5000", "Not found schedule"),
    SCHED_INVALID_ID("5001", "Schedule-id is invalid"),
    SCHED_INVALID_TODO("5002", "Invalid todo in schedule"),

    // 9000-9999: Common Code
    RGB_FORMAT_INVALID("9000", "Invalid RGB code format"),

    ;
}
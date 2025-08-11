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
    NOT_FOUND("0404", "Not found"),
    CONFLICT("0409", "Conflict"),
    INTERNAL_ERROR("0500", "Unexpected Error"),
  
    // 1001-1999: Auth
    AUTH_USER_NOT_EXISTS("1001", "User not exists"),
    AUTH_FAIL_TO_PARSE_JWT("1002", "Fail To Parse JWT"),
    AUTH_TOKEN_EXPIRED("1003", "JWT is expired"),
    AUTH_TOKEN_INVALID("1004", "JWT is invalid"),
    AUTH_FAIL_TO_FETCH_TOKEN("1005", "Fail to fetch token"),
    AUTH_FAIL_TO_FETCH_USER_INFO("1006", "Fail to fetch user info"),
    AUTH_FAIL_TO_SIGNUP_DUPLICATE_USER("1007", "User already signed up using other auth"),

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

    TODO_MATERIAL_UPLOAD_FAIL("4100", "Failed to todo material upload"),

    // 5000-5999: Schedule
    SCHED_NOT_FOUND("5000", "Not found schedule"),
    SCHED_INVALID_ID("5001", "Schedule-id is invalid"),
    SCHED_INVALID_TODO("5002", "Invalid todo in schedule"),

    // 6000-6999: Timer
    TIMER_TODO_NOT_FOUND("6000", "Not found todo-timer"),
    TIMER_TODO_INVALID_TIME("6001", "Todo Timer time is invalid."),
    TIMER_TODO_ALREADY_RUNNING("6002", "Todo Timer is already running."),
    TIMER_TODO_INVALID_ID("6003", "Todo Timer id is invalid."),
    TIMER_TODO_NOT_MATCH_USER("6004", "The user has not this todo-timer"),
    TIMER_TODO_NOT_RUNNING("6005", "Todo Timer is not running"),
    TIMER_TODO_NOT_PAUSED("6006", "Todo Timer is not paused"),
    TIMER_TODO_NOT_ENDED_PAUSED("6007", "Todo Timer is already paused in data.. plz check"),
    TIMER_TODO_PAUSED_ID_INVALID("6008", "Todo Timer paused history id is invalid"),
    TIMER_TODO_PAUSED_MANY("6009", "This timer has many paused histories.. plz check"),

    // 7000-7999: Note
    NOTE_NOT_FOUND("7000", "Not found note"),
    NOTE_INVALID_TITLE_LENGTH("7001", "Note title must be 30 characters or less"),
    NOTE_INVALID_ID("7002", "Note id is invalid"),
    NOTE_INVALID_CONTENT_LENGTH("7003", "Note content must be 1000 words or less"),
    NOTE_GOAL_NOT_FOUND("7004", "No goal connected with note"),
    NOTE_TODO_NOT_FOUND("7005", "No todo connected with note"),
    NOTE_CANNOT_CREATE_NOTE("7006", "There's already note created. Only 1 note per todo can be created"),

    // 9000-9999: Common Code
    RGB_FORMAT_INVALID("9000", "Invalid RGB code format"),

    ;
}
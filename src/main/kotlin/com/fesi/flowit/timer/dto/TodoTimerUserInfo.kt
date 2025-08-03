package com.fesi.flowit.timer.dto

import io.swagger.v3.oas.annotations.media.Schema

data class TodoTimerUserInfo(
    @field:Schema(
        description = "회원 ID",
        example = "1"
    )
    val userId: Long,

    @field:Schema(
        description = "할 일 타이머 동작 여부",
        example = "true | false"
    )
    val isRunningTimer: Boolean,

    @field:Schema(
        description = "타이머의 할 일 ID",
        example = "1",
        nullable = true
    )
    val todoId: Long? = null,

    @field:Schema(
        description = "타이머의 목표 ID",
        example = "1",
        nullable = true
    )
    val goalId :Long? = null
) {
    companion object {
        fun createIfNotExist(userId: Long): TodoTimerUserInfo {
            return TodoTimerUserInfo(userId = userId, isRunningTimer = false)
        }
    }
}
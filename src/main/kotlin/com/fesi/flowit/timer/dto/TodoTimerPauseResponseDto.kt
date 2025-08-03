package com.fesi.flowit.timer.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class TodoTimerPauseResponseDto(
    @field:Schema(
        description = "할 일 타이머 ID",
        example = "1",
    )
    val todoTimerId: Long,

    @field:Schema(
        description = "회원 ID",
        example = "1",
    )
    val todoId: Long,

    @field:Schema(
        description = "중지 시간",
        example = "2025-08-03T12:32:12",
    )
    val pausedDateTime: LocalDateTime,
) {
    companion object {
        fun of(todoTimerId: Long, todoId: Long, startedDateTime: LocalDateTime): TodoTimerPauseResponseDto {
            return TodoTimerPauseResponseDto(todoTimerId, todoId, startedDateTime)
        }
    }
}
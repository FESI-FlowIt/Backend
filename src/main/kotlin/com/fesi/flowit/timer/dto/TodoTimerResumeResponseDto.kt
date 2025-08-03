package com.fesi.flowit.timer.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class TodoTimerResumeResponseDto(
    @field:Schema(
        description = "할 일 타이머 ID",
        example = "1",
    )
    val todoTimerId: Long,

    @field:Schema(
        description = "할 일 ID",
        example = "1",
    )
    val todoId: Long,

    @field:Schema(
        description = "재시작 시간",
        example = "2025-08-03T12:32:12",
    )
    val resumedDateTime: LocalDateTime,

    @field:Schema(
        description = "총 정지 시간(sec)",
        example = "1500",
    )
    val totalPausedTime: Long
) {
    companion object {
        fun of(todoTimerId: Long, todoId: Long, resumedDateTime: LocalDateTime, totalPausedTime: Long): TodoTimerResumeResponseDto {
            return TodoTimerResumeResponseDto(todoTimerId, todoId, resumedDateTime, totalPausedTime)
        }
    }
}
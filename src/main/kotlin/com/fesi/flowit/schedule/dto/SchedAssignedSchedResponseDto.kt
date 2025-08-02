package com.fesi.flowit.schedule.dto

import com.fesi.flowit.common.util.REGEX_RGB_CODE
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime

data class SchedAssignedSchedResponseDto(
    @field:Schema(
        description = "조회 날짜",
        example = "2025-07-19",
    )
    val date: LocalDate,

    val assignedTodos: List<AssignedSched>
) {
    companion object {
        fun of(date: LocalDate, assignedTodos: List<AssignedSched>): SchedAssignedSchedResponseDto {
            return SchedAssignedSchedResponseDto(date, assignedTodos)
        }
    }
}

data class AssignedSched(
    @field:Schema(
        description = "일정 ID",
        example = "1",
    )
    val schedId: Long,

    @field:Schema(
        description = "할 일 ID",
        example = "1",
    )
    val todoId: Long,

    @field:Schema(
        description = "목표 색상 코드",
        example = "#000000",
        pattern = REGEX_RGB_CODE,
    )
    val color: String,

    @field:Schema(
        description = "할 일 이름",
        example = "할 일 이름",
        minLength = 1,
        maxLength = 30,
    )
    val name: String,

    @field:Schema(
        description = "목표 마감 시간",
        example = "2025-07-19T14:29:00",
    )
    val dueDateTime: LocalDateTime,

    @field:Schema(
        description = "일정 시작 시간",
        example = "2025-07-19T14:29:00",
    )
    val startedDateTime: LocalDateTime,

    @field:Schema(
        description = "일정 종료 시간",
        example = "2025-07-19T14:29:00",
    )
    val endedDateTime: LocalDateTime
)
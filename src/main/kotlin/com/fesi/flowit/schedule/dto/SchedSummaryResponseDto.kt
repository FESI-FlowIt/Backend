package com.fesi.flowit.schedule.dto

import com.fesi.flowit.common.extensions.REGEX_RGB_CODE
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class SchedSummaryResponseDto(
    @field:Schema(
        description = "일정 ID",
        example = "1",
    )
    var scheduleId: Long,

    @field:Schema(
        description = "할 일 ID",
        example = "1",
    )
    var todoId: Long,

    @field:Schema(
        description = "할 일 이름",
        example = "할 일 이름",
        minLength = 1,
        maxLength = 30,
    )
    var todoName: String,

    @field:Schema(
        description = "목표 색상 코드",
        example = "#000000",
        pattern = REGEX_RGB_CODE,
    )
    var color: String,

    @field:Schema(
        description = "일정 시작 시간",
        example = "2025-07-19T11:22:33",
    )
    var startedDateTime: LocalDateTime,

    @field:Schema(
        description = "일정 종료 시간",
        example = "2025-07-19T11:22:33",
    )
    var endedDateTime: LocalDateTime
)
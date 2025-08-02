package com.fesi.flowit.todo.vo

import com.fesi.flowit.common.extensions.REGEX_RGB_CODE
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class TodoSummaryWithDateVo(
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
        description = "생성 시간",
        example = "2025-07-19T14:29:00",
    )
    val createdDateTime: LocalDateTime,

    @field:Schema(
        description = "완료 시간",
        example = "2025-07-19T14:29:00",
    )
    val doneDateTime: LocalDateTime?,

    @field:Schema(
        description = "마감 시간",
        example = "2025-07-19T14:29:00",
    )
    val dueDateTime: LocalDateTime,
)
package com.fesi.flowit.schedule.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class SchedCreateRequestDto(
    @field:Schema(
        description = "회원 ID",
        example = "1",
    )
    var userId: Long,

    var scheduleInfos: MutableList<SchedTodoAndDateInfo>
)

data class SchedTodoAndDateInfo(
    @field:Schema(
        description = "할 일 ID",
        example = "1",
    )
    var todoId: Long,

    @field:Schema(
        description = "일정 시작 시간",
        example = "2025-07-19T11:22:33",
    )
    var startedDateTime: LocalDateTime,

    @field:Schema(
        description = "일정 종료 시간",
        example = "2025-07-19T11:22:33",
    )
    var endedDateTime: LocalDateTime,
)
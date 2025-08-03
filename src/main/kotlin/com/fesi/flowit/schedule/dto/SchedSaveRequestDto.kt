package com.fesi.flowit.schedule.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class SchedSaveRequestDto(
    @field:Schema(
        description = "회원 ID",
        example = "1",
    )
    var userId: Long,

    var scheduleInfos: MutableList<SchedSaveInfo>
)

data class SchedSaveInfo(
    @field:Schema(
        description = "일정 ID - 값이 있으면 Update, 없으면 Save",
        example = "1",
        nullable = true
    )
    var schedId: Long?,

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

    @field:Schema(
        description = "일정 삭제 여부",
        example = "true | false"
    )
    @field:JsonProperty("isRemoved")
    var isRemoved: Boolean
)
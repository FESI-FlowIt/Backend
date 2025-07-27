package com.fesi.flowit.schedule.dto

import java.time.LocalDateTime

data class SchedSummaryResponseDto(
    var scheduleId: Long,
    var todoId: Long,
    var todoName: String,
    var color: String,
    var startedDateTime: LocalDateTime,
    var endedDateTime: LocalDateTime
)
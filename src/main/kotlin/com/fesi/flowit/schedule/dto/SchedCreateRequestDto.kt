package com.fesi.flowit.schedule.dto

import java.time.LocalDateTime

data class SchedCreateRequestDto(
    var userId: Long,
    var scheduleInfos: MutableList<SchedTodoAndDateInfo>
)

data class SchedTodoAndDateInfo(
    var todoId: Long,
    var startedDateTime: LocalDateTime,
    var endedDateTime: LocalDateTime,
)
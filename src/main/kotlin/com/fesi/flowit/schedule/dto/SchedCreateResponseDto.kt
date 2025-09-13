package com.fesi.flowit.schedule.dto

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.ScheduleException
import com.fesi.flowit.schedule.entity.Schedule
import io.swagger.v3.oas.annotations.media.Schema

data class SchedCreateResponseDto(
    @field:Schema(
        description = "회원 ID",
        example = "1",
    )
    val userId:Long,

    val schedules: MutableList<SchedSummaryResponseDto>
) {
    companion object {

        fun fromSchedules(userId: Long, schedules: List<Schedule>): SchedCreateResponseDto {
            val summaries = schedules.map { sched ->
                SchedSummaryResponseDto(
                    scheduleId = sched.id ?: throw ScheduleException.fromCode(ApiResultCode.SCHED_INVALID_ID),
                    todoId = sched.todo.id ?: throw ScheduleException.fromCode(ApiResultCode.SCHED_INVALID_TODO),
                    todoName = sched.todo.name,
                    color = sched.todo.goal?.color ?: "#000000",
                    startedDateTime = sched.startedDateTime,
                    endedDateTime = sched.endedDateTime
                )
            }.toMutableList()

            return SchedCreateResponseDto(userId, summaries)
        }
    }
}
package com.fesi.flowit.timer.dto

import com.fesi.flowit.timer.vo.TodoTimerTotalTimeVo
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalTime

data class TodoTimerTotalRunningTime(
    @field:Schema(
        description = "할 일 ID",
        example = "1",
    )
    val todoId: Long,

    @field:Schema(
        description = "누적 작업 시간",
        example = "11:22:33",
    )
    val totalRunningTime: LocalTime
) {
    companion object {
        fun fromTotalTimeVo(todoId: Long, totalTimeVo: TodoTimerTotalTimeVo): TodoTimerTotalRunningTime {
            totalTimeVo.validateLongerPausedTimeThanActiveTime()

            return TodoTimerTotalRunningTime(todoId, totalTimeVo.convertTotalTimeToLocalDate())
        }
    }
}
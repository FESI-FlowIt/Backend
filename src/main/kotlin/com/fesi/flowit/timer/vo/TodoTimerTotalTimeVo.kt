package com.fesi.flowit.timer.vo

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.TodoTimerException
import java.time.LocalTime

data class TodoTimerTotalTimeVo(
    val todoId: Long,
    val activeTimeSec: Number,
    val pausedTimeSec: Number
) {
    companion object {
        fun emptyRecord(todoId: Long): TodoTimerTotalTimeVo {
            return TodoTimerTotalTimeVo(todoId, 0, 0)
        }
    }

    fun convertTotalTimeToLocalDate(): LocalTime {
        validateLongerPausedTimeThanActiveTime()

        val runningTime = activeTimeSec.toLong() - pausedTimeSec.toLong()
        return LocalTime.ofNanoOfDay(runningTime * 1_000_000)
    }

    fun validateLongerPausedTimeThanActiveTime() {
        if (activeTimeSec.toLong() < pausedTimeSec.toLong()) {
            throw TodoTimerException.fromCode(ApiResultCode.TIMER_TODO_INVALID_TIME)
        }
    }
}
package com.fesi.flowit.timer.dto

import java.time.LocalTime

data class TodoTimerStopResponseDto(
    val todoTimerId: Long,
    val todoId: Long,
    val runningTime: LocalTime
) {
    companion object {
        fun of(todoTimerId: Long, todoId: Long, runningTime: Long): TodoTimerStopResponseDto {
            return of(todoTimerId, todoId, LocalTime.ofSecondOfDay(runningTime))
        }

        fun of(todoTimerId: Long, todoId: Long, runningTime: LocalTime): TodoTimerStopResponseDto {
            return TodoTimerStopResponseDto(todoTimerId, todoId, runningTime)
        }
    }
}
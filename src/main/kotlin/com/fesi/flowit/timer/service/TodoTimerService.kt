package com.fesi.flowit.timer.service

import com.fesi.flowit.timer.dto.TodoTimerPauseResponseDto
import com.fesi.flowit.timer.dto.TodoTimerStartResponseDto
import com.fesi.flowit.timer.dto.TodoTimerTotalRunningTime
import com.fesi.flowit.timer.dto.TodoTimerUserInfo

interface TodoTimerService {
    fun hasUserTodoTimer(userId: Long): TodoTimerUserInfo
    fun getTotalRunningTimeByTodo(userId: Long, todoId: Long): TodoTimerTotalRunningTime
    fun startTodoTimer(userId: Long, todoId: Long): TodoTimerStartResponseDto
    fun pauseTodoTimer(userId: Long, todoTimerId: Long): TodoTimerPauseResponseDto
}
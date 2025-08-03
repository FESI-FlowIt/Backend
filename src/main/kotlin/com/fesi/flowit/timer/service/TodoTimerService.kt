package com.fesi.flowit.timer.service

import com.fesi.flowit.timer.dto.*

interface TodoTimerService {
    fun hasUserTodoTimer(userId: Long): TodoTimerUserInfo
    fun getTotalRunningTimeByTodo(userId: Long, todoId: Long): TodoTimerTotalRunningTime
    fun startTodoTimer(userId: Long, todoId: Long): TodoTimerStartResponseDto
    fun pauseTodoTimer(userId: Long, todoTimerId: Long): TodoTimerPauseResponseDto
    fun resumeTodoTimer(userId: Long, todoTimerId: Long): TodoTimerResumeResponseDto
}
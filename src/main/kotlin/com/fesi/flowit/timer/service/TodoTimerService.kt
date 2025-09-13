package com.fesi.flowit.timer.service

import com.fesi.flowit.timer.dto.*
import com.fesi.flowit.timer.entity.TodoTimer
import com.fesi.flowit.user.entity.User
import java.time.LocalDate

interface TodoTimerService {
    fun hasUserTodoTimer(userId: Long): TodoTimerUserInfo
    fun getTotalRunningTimeByTodo(userId: Long, todoId: Long): TodoTimerTotalRunningTime
    fun startTodoTimer(userId: Long, todoId: Long): TodoTimerStartResponseDto
    fun pauseTodoTimer(userId: Long, todoTimerId: Long): TodoTimerPauseResponseDto
    fun resumeTodoTimer(userId: Long, todoTimerId: Long): TodoTimerResumeResponseDto
    fun finishTodoTimer(userId: Long, todoTimerId: Long): TodoTimerStopResponseDto
    fun getFinishedTodoTimerBetween(startDate: LocalDate, endDate: LocalDate, user: User): List<TodoTimer>
}
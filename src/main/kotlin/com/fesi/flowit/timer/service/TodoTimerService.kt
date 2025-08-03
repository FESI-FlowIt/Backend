package com.fesi.flowit.timer.service

import com.fesi.flowit.timer.dto.TodoTimerTotalRunningTime
import com.fesi.flowit.timer.dto.TodoTimerUserInfo

interface TodoTimerService {
    fun hasUserTodoTimer(userId: Long): TodoTimerUserInfo
    fun getTotalRunningTimeByTodo(userId: Long, todoId: Long): TodoTimerTotalRunningTime
}
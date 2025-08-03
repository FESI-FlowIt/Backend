package com.fesi.flowit.timer.controller

import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.timer.dto.TodoTimerTotalRunningTime
import com.fesi.flowit.timer.dto.TodoTimerUserInfo
import com.fesi.flowit.timer.service.TodoTimerService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "할 일 타이머")
@RestController
class TodoTimerControllerImpl(
    val todoTimerService: TodoTimerService
) : TodoTimerController {

    @GetMapping("/todo-timers/user")
    override fun hasUserTodoTimer(@RequestParam("userId") userId: Long): ResponseEntity<ApiResult<TodoTimerUserInfo>> {
        return ApiResponse.ok(todoTimerService.hasUserTodoTimer(userId))
    }

    @GetMapping("/todo-timers/total-time")
    override fun getTotalRunningTimeByTodo(@RequestParam("userId") userId: Long,
                                           @RequestParam("todoId") todoId: Long
    ): ResponseEntity<ApiResult<TodoTimerTotalRunningTime>> {
        return ApiResponse.ok(todoTimerService.getTotalRunningTimeByTodo(userId, todoId))
    }
}
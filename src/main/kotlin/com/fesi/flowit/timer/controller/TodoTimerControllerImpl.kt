package com.fesi.flowit.timer.controller

import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.timer.dto.TodoTimerStartRequestDto
import com.fesi.flowit.timer.dto.TodoTimerStartResponseDto
import com.fesi.flowit.timer.dto.TodoTimerTotalRunningTime
import com.fesi.flowit.timer.dto.TodoTimerUserInfo
import com.fesi.flowit.timer.service.TodoTimerService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

private val log = loggerFor<TodoTimerControllerImpl>()

@Tag(name = "할 일 타이머")
@RestController
class TodoTimerControllerImpl(
    val todoTimerService: TodoTimerService
) : TodoTimerController {

    @GetMapping("/todo-timers/user")
    override fun hasUserTodoTimer(@RequestParam("userId") userId: Long): ResponseEntity<ApiResult<TodoTimerUserInfo>> {
        log.debug(">> request hasUserTodoTimer(userId=${userId})")

        return ApiResponse.ok(todoTimerService.hasUserTodoTimer(userId))
    }

    @GetMapping("/todo-timers/total-time")
    override fun getTotalRunningTimeByTodo(@RequestParam("userId") userId: Long,
                                           @RequestParam("todoId") todoId: Long
    ): ResponseEntity<ApiResult<TodoTimerTotalRunningTime>> {
        log.debug(">> request getTotalRunningTimeByTodo(userId=${userId}, todoId=${todoId})")

        return ApiResponse.ok(todoTimerService.getTotalRunningTimeByTodo(userId, todoId))
    }

    @PostMapping("/todo-timers")
    override fun startTodoTimer(@RequestBody request: TodoTimerStartRequestDto): ResponseEntity<ApiResult<TodoTimerStartResponseDto>> {
        log.debug(">> request startTodoTimer(request=${request})")

        return ApiResponse.created(todoTimerService.startTodoTimer(request.userId, request.todoId))
    }
}
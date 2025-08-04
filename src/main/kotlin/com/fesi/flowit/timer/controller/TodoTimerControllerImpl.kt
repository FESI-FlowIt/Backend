package com.fesi.flowit.timer.controller

import com.fesi.flowit.common.auth.AuthUserId
import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.timer.dto.*
import com.fesi.flowit.timer.service.TodoTimerService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
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
    override fun hasUserTodoTimer(@AuthUserId userId: Long): ResponseEntity<ApiResult<TodoTimerUserInfo>> {
        log.debug(">> request hasUserTodoTimer(userId=${userId})")

        return ApiResponse.ok(todoTimerService.hasUserTodoTimer(userId))
    }

    @GetMapping("/todo-timers/total-time")
    override fun getTotalRunningTimeByTodo(@AuthUserId userId: Long,
                                           @RequestParam("todoId") todoId: Long
    ): ResponseEntity<ApiResult<TodoTimerTotalRunningTime>> {
        log.debug(">> request getTotalRunningTimeByTodo(userId=${userId}, todoId=${todoId})")

        return ApiResponse.ok(todoTimerService.getTotalRunningTimeByTodo(userId, todoId))
    }

    @PostMapping("/todo-timers")
    override fun startTodoTimer(@RequestBody request: TodoTimerStartRequestDto, @AuthUserId userId: Long): ResponseEntity<ApiResult<TodoTimerStartResponseDto>> {
        log.debug(">> request startTodoTimer(request=${request})")

        return ApiResponse.created(todoTimerService.startTodoTimer(userId, request.todoId))
    }

    @PostMapping("/todo-timers/{todoTimerId}/pause")
    override fun pauseTodoTimer(@PathVariable("todoTimerId") todoTimerId: Long,
                                @AuthUserId userId: Long
    ): ResponseEntity<ApiResult<TodoTimerPauseResponseDto>> {
        log.debug(">> request pauseTodoTimer(todoTimerId=${todoTimerId}, userId=${userId})")

        return ApiResponse.created(todoTimerService.pauseTodoTimer(userId, todoTimerId))
    }

    @PatchMapping("/todo-timers/{todoTimerId}/resume")
    override fun resumeTodoTimer(@PathVariable("todoTimerId") todoTimerId: Long,
                                 @AuthUserId userId: Long): ResponseEntity<ApiResult<TodoTimerResumeResponseDto>> {
        log.debug(">> request resumeTodoTimer(todoTimerId=${todoTimerId}, userId=${userId})")

        return ApiResponse.ok(todoTimerService.resumeTodoTimer(userId, todoTimerId))
    }

    @PatchMapping("todo-timers/{todoTimerId}/finish")
    override fun finishTodoTimer(@PathVariable("todoTimerId") todoTimerId: Long,
                                 @AuthUserId userId: Long): ResponseEntity<ApiResult<TodoTimerStopResponseDto>> {
        log.debug(">> request stopTodoTimer(todoTimerId=${todoTimerId}, userId=${userId})")

        return ApiResponse.ok(todoTimerService.finishTodoTimer(userId, todoTimerId))
    }
}
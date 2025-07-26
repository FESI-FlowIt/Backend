package com.fesi.flowit.todo.controller

import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.goal.controller.GoalControllerImpl
import com.fesi.flowit.todo.dto.TodoCreateRequestDto
import com.fesi.flowit.todo.dto.TodoCreateResponseDto
import com.fesi.flowit.todo.service.TodoService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

private val log = loggerFor<GoalControllerImpl>()

@Tag(name = "할 일")
@RestController
class TodoControllerImpl(
    private val todoService: TodoService
) : TodoController {

    @PostMapping("/todos")
    override fun createTodo(@RequestBody request: TodoCreateRequestDto): ResponseEntity<ApiResult<TodoCreateResponseDto>> {
        log.debug(">> request createTodo(${request}")
        return ApiResponse.created(todoService.createTodo(request.userId, request.name, request.goalId))
    }
}
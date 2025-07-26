package com.fesi.flowit.todo.controller

import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.todo.dto.TodoCreateRequestDto
import com.fesi.flowit.todo.dto.TodoCreateResponseDto
import com.fesi.flowit.todo.dto.TodoModifyRequestDto
import com.fesi.flowit.todo.dto.TodoModifyResponseDto
import com.fesi.flowit.todo.service.TodoService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private val log = loggerFor<TodoControllerImpl>()

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

    @PatchMapping("/todos/{todoId}")
    override fun modifyTodo(@PathVariable("todoId") todoId: Long,
                            @RequestBody request: TodoModifyRequestDto
    ): ResponseEntity<ApiResult<TodoModifyResponseDto>> {
        log.debug(">> request modifyTodo(${request})")

        val result = todoService.modifyTodo(
            todoId = todoId,
            userId = request.userId,
            name = request.name,
            goalId = request.goalId
        )

        return ApiResponse.ok(result)
    }

    @DeleteMapping("/todos/{todoId}")
    override fun deleteTodo(@PathVariable("todoId") todoId: Long,
                            @RequestParam("userId") userId: Long
    ): ResponseEntity<ApiResult<Unit>> {
        log.debug(">> request deleteTodo(userId=${userId}, todoId=${todoId})")

        todoService.deleteTodoById(userId, todoId)

        return ApiResponse.noContent()
    }
}
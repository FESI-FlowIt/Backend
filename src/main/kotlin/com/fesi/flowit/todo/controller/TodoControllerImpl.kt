package com.fesi.flowit.todo.controller

import com.fesi.flowit.common.auth.AuthUserId
import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.todo.dto.*
import com.fesi.flowit.todo.service.TodoService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

private val log = loggerFor<TodoControllerImpl>()

@Tag(name = "할 일")
@RestController
class TodoControllerImpl(
    private val todoService: TodoService
) : TodoController {

    @PostMapping("/todos")
    override fun createTodo(@RequestBody request: TodoCreateRequestDto, @AuthUserId userId: Long): ResponseEntity<ApiResult<TodoCreateResponseDto>> {
        log.debug(">> request createTodo(${request}")
        return ApiResponse.created(todoService.createTodo(userId, request.name, request.goalId))
    }

    @PatchMapping("/todos/{todoId}")
    override fun modifyTodo(
        @PathVariable("todoId") todoId: Long,
        @RequestBody request: TodoModifyRequestDto,
        @AuthUserId userId: Long
    ): ResponseEntity<ApiResult<TodoModifyResponseDto>> {
        log.debug(">> request modifyTodo(${request})")

        val result = todoService.modifyTodo(
            todoId = todoId,
            userId = userId,
            name = request.name,
            goalId = request.goalId
        )

        return ApiResponse.ok(result)
    }

    @PatchMapping("/todos/{todoId}/done")
    override fun changeDoneStatus(
        @PathVariable("todoId") todoId: Long,
        @RequestBody request: TodoChangeDoneRequestDto,
        @AuthUserId userId: Long
    ): ResponseEntity<ApiResult<TodoChangeDoneResponseDto>> {
        log.debug(">> request changeDoneStatus(todoId=${todoId}, request=${request}")

        return ApiResponse.ok(todoService.changeDoneStatus(todoId, userId, request.isDone))
    }

    @DeleteMapping("/todos/{todoId}")
    override fun deleteTodo(
        @PathVariable("todoId") todoId: Long,
        @AuthUserId userId: Long
    ): ResponseEntity<ApiResult<Unit>> {
        log.debug(">> request deleteTodo(userId=${userId}, todoId=${todoId})")

        todoService.deleteTodoById(userId, todoId)

        return ApiResponse.noContent()
    }

    @PostMapping("/todos/{todoId}/file")
    fun uploadTodoFile(
        @AuthUserId userId: Long,
        @PathVariable("todoId") todoId: Long,
        @RequestParam file: MultipartFile
    ): ResponseEntity<ApiResult<TodoFileResponseDto>> {
        log.debug(">> request uploadTodoFile(userId=${userId}, todoId=${todoId}, file=${file.originalFilename ?: file.name})")

        return ApiResponse.ok(todoService.uploadTodoFile(userId, todoId, file))
    }
}
package com.fesi.flowit.todo.controller

import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.todo.dto.TodoCreateRequestDto
import com.fesi.flowit.todo.dto.TodoCreateResponseDto
import com.fesi.flowit.todo.service.TodoService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "할 일")
@RestController
class TodoControllerImpl(
    private val todoService: TodoService
) : TodoController {

    @PostMapping("/todo")
    override fun createTodo(@RequestBody request: TodoCreateRequestDto): ResponseEntity<ApiResult<TodoCreateResponseDto>> {
        return ApiResponse.created(todoService.createTodo(request.name, request.goalId))
    }
}
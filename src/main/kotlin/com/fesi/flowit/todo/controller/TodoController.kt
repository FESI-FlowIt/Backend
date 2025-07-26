package com.fesi.flowit.todo.controller

import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.todo.dto.TodoCreateRequestDto
import com.fesi.flowit.todo.dto.TodoCreateResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

interface TodoController {
    @Operation(
        summary = "할 일 생성",
        description = "목표에 할 일을 생성합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "할 일 생성 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = TodoCreateResponseDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "올바르지 않은 요청 혹은 유효하지 않은 파라미터 값",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiResult.Exception::class)
                )]
            )
        ]
    )
    fun createTodo(@RequestBody request: TodoCreateRequestDto): ResponseEntity<ApiResult<TodoCreateResponseDto>>
}
package com.fesi.flowit.todo.controller

import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.todo.dto.TodoCreateRequestDto
import com.fesi.flowit.todo.dto.TodoCreateResponseDto
import com.fesi.flowit.todo.dto.TodoModifyRequestDto
import com.fesi.flowit.todo.dto.TodoModifyResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

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

    @Operation(
        summary = "할 일 수정",
        description = "할 일을 수정합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "할 일 수정 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = TodoModifyResponseDto::class)
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
    fun modifyTodo(@PathVariable("todoId") todoId: Long,
                   @RequestBody request: TodoModifyRequestDto
    ): ResponseEntity<ApiResult<TodoModifyResponseDto>>

    @Operation(
        summary = "할 일 삭제",
        description = "할 일을 삭제합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "할 일 삭제 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = Unit::class)
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
    fun deleteTodo(@PathVariable("todoId") todoId: Long, @RequestParam("userId") userId: Long): ResponseEntity<ApiResult<Unit>>
}
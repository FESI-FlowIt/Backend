package com.fesi.flowit.todo.controller

import com.fesi.flowit.common.auth.AuthUserId
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.todo.dto.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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
    fun createTodo(
        @RequestBody request: TodoCreateRequestDto,
        @Parameter(hidden = true) @AuthUserId userId: Long
    ): ResponseEntity<ApiResult<TodoCreateResponseDto>>

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
                   @RequestBody request: TodoModifyRequestDto,
                   @Parameter(hidden = true) @AuthUserId userId: Long
    ): ResponseEntity<ApiResult<TodoModifyResponseDto>>

    @Operation(
        summary = "할 일 완료 상태 변경",
        description = "할 일 완료 상태를 변경합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "할 일 완료 상태 변경 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = TodoChangeDoneResponseDto::class)
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
    fun changeDoneStatus(@PathVariable("todoId") todoId: Long,
                         @RequestBody request: TodoChangeDoneRequestDto,
                         @Parameter(hidden = true) @AuthUserId userId: Long
    ): ResponseEntity<ApiResult<TodoChangeDoneResponseDto>>

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
    fun deleteTodo(
        @PathVariable("todoId") todoId: Long,
        @Parameter(hidden = true) @AuthUserId userId: Long
    ): ResponseEntity<ApiResult<Unit>>
}
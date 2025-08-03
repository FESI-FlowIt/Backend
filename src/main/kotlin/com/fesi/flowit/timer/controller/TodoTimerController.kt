package com.fesi.flowit.timer.controller

import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.timer.dto.TodoTimerUserInfo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam

interface TodoTimerController {
    @Operation(
        summary = "해당 회원이 할 일 타이머 있는지 확인",
        description = """
            hasTodoTimer가 true이면 이 정보를 상태값으로 들고 있다가, 
            해당 할 일 조회 시 타이머 정보 조회 API를 호출해주시면 됩니다.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = TodoTimerUserInfo::class)
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
    fun hasUserTodoTimer(@RequestParam("userId") userId: Long): ResponseEntity<ApiResult<TodoTimerUserInfo>>
}
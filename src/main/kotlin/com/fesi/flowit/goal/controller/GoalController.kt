package com.fesi.flowit.goal.controller

import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.goal.dto.GoalCreateRequestDto
import com.fesi.flowit.goal.dto.GoalCreateResponseDto
import com.fesi.flowit.goal.dto.GoalFindAllResponseDto
import com.fesi.flowit.goal.dto.GoalSummaryResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

interface GoalController {

    @Operation(
        summary = "목표 생성",
        description = "목표 생성 모달로부터 목표를 생성합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "목표 생성 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = GoalCreateResponseDto::class)
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
    fun createGoal(@RequestBody request: GoalCreateRequestDto): ResponseEntity<ApiResult<GoalCreateResponseDto>>

    @Operation(
        summary = "모든 목표 조회",
        description =
        """
            모든 목표를 조회합니다. 리스트 순서는 다음 우선 순위에 따라 정렬됩니다.
            1. 고정 여부
            2. 생성 날짜
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = GoalFindAllResponseDto::class)
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
    fun getAllGoals(): ResponseEntity<ApiResult<List<GoalFindAllResponseDto>>>

    @Operation(
        summary = "목표 별 할 일",
        description = "목표와 관련된 정보를 반환합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = GoalSummaryResponseDto::class)
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
    fun getGoalsSummary(): ResponseEntity<ApiResult<List<GoalSummaryResponseDto>>>
}
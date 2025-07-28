package com.fesi.flowit.goal.controller

import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.common.response.PageResponse
import com.fesi.flowit.goal.dto.*
import com.fesi.flowit.goal.search.GoalSortCriteria
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.data.domain.Pageable
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.time.YearMonth

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
                    schema = Schema(implementation = GoalInfoResponseDto::class)
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
    fun createGoal(@RequestBody request: GoalCreateRequestDto): ResponseEntity<ApiResult<GoalInfoResponseDto>>
    @Operation(
        summary = "목표 수정",
        description = "목표 수정 모달로부터 목표를 수정합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "목표 수정 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = GoalInfoResponseDto::class)
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
    fun modifyGoal(@PathVariable("goalId") goalId: Long,
                   @RequestBody request: GoalModifyRequestDto): ResponseEntity<ApiResult<GoalInfoResponseDto>>

    @Operation(
        summary = "목표 고정 상태 변경",
        description = "목표 고정 상태를 변경합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "목표 고정 상태 변경 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = GoalChangePinResponseDto::class)
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
    fun changePinStatus(@PathVariable("goalId") goalId: Long,
                        @RequestBody request: GoalChangePinRequestDto): ResponseEntity<ApiResult<GoalChangePinResponseDto>>

    @Operation(
        summary = "목표 삭제",
        description = "목표를 삭제합니다. 목표에 포함된 할 일도 모두 삭제됩니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "목표 및 목표에 포함된 할 일 삭제 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = Any::class)
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
    fun deleteGoal(@PathVariable("goalId") goalId: Long, @RequestParam("userId") userId: Long): ResponseEntity<ApiResult<Unit>>

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
    fun getAllGoals(@RequestParam("userId") userId: Long): ResponseEntity<ApiResult<List<GoalFindAllResponseDto>>>

    @Operation(
        summary = "목표 조회",
        description = "목표를 조회합니다. 할 일 목록과 함께 반환됩니다."
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
    fun getGoalSummary(@PathVariable("goalId") goalId: Long,
                       @RequestParam("userId") userId: Long): ResponseEntity<ApiResult<GoalSummaryResponseDto>>

    @Operation(
        summary = "모든 목표 조회",
        description = "모든 목표를 조건에 따라 탐색합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = PageResponse::class, subTypes = [GoalSummaryResponseDto::class])

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
    fun searchGoalSummaries(
        @RequestParam("userId") userId: Long,
        @RequestParam("isPinned") isPinned: Boolean,
        @RequestParam("sortedBy") sortedBy: GoalSortCriteria,
        pageable: Pageable
    ): ResponseEntity<ApiResult<PageResponse<GoalSummaryResponseDto>>>

    @Operation(
        summary = "목표 별 할 일",
        description = "목표와 관련된 정보를 반환합니다. 대시보드 > 목표 별 할 일"
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
    fun getGoalSummariesInDashboard(@RequestParam("userId") userId: Long): ResponseEntity<ApiResult<List<GoalSummaryResponseDto>>>

    @Operation(
        summary = "월 별 목표 조회 (캘린더)",
        description = "해당 월에 마감되는 목표를 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = GoalsByMonthlyResponseDto::class)
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
    fun getGoalsByDueMonth(@RequestParam("userId") userId: Long,

                           @RequestParam(name = "date", required = true)
                           @DateTimeFormat(pattern = "yyyy-MM")
                           dueYearMonth: YearMonth
    ): ResponseEntity<ApiResult<GoalsByMonthlyResponseDto>>
}
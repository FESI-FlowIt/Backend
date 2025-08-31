package com.fesi.flowit.heatmap.controller

import com.fesi.flowit.common.auth.AuthUserId
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.heatmap.dto.HeatmapInsightWeeklyResponseDto
import com.fesi.flowit.heatmap.dto.HeatmapMonthlyResponseDto
import com.fesi.flowit.heatmap.dto.HeatmapWeeklyResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import java.time.LocalDate
import java.time.YearMonth

interface HeatmapController {
    @Operation(
        summary = "작업 시간 분석 - 이번 주",
        description = """ 
           할 일 타이머의 작업 시간을 시간대 별로 반환합니다.
           주어진 날짜의 월요일~일요일까지 데이터를 반환하며, 일시 정지되어 있던 시간은 제외합니다.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "주간 히트맵 조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = HeatmapWeeklyResponseDto::class)
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
    fun getWeeklyHeatmap(
        @Parameter(hidden = true) @AuthUserId userId: Long,

        @PathVariable("date") date: LocalDate
    ): ResponseEntity<ApiResult<List<HeatmapWeeklyResponseDto>>>

    @Operation(
        summary = "작업 시간 분석 - 이번 달",
        description = """ 
           할 일 타이머의 작업 시간을 시간대 별로 반환합니다.
           일시 정지되어 있던 시간은 제외합니다.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "월간 히트맵 조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = HeatmapMonthlyResponseDto::class)
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
    fun getMonthlyHeatmap(
        @Parameter(hidden = true)
        @AuthUserId 
        userId: Long,

        @PathVariable(name = "yearMonth")
        @DateTimeFormat(pattern = "yyyy-MM")
        yearMonth: YearMonth
    ): ResponseEntity<ApiResult<HeatmapMonthlyResponseDto>>

    @Operation(
        summary = "주간 인사이트 메시지 조회",
        description = """ 
           1. 골든 타임 메시지: 월-일까지 각 시간대 별 기록이 있으면 골든 타임으로 판단
           2. 최대 기록: 같은 작업 시간이 있으면 둘 다 메시지에 포함됨
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "주간 인사이트 메시지 조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = HeatmapInsightWeeklyResponseDto::class)
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
    fun getWeeklyHeatmapInsight(
        @Parameter(hidden = true) @AuthUserId userId: Long,
        @PathVariable("date") date: LocalDate
    ): ResponseEntity<ApiResult<List<HeatmapInsightWeeklyResponseDto>>>
}
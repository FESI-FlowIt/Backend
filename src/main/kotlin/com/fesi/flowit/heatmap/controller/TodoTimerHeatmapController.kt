package com.fesi.flowit.heatmap.controller

import com.fesi.flowit.common.auth.AuthUserId
import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.heatmap.dto.HeatmapInsightMonthlyResponseDto
import com.fesi.flowit.heatmap.dto.HeatmapInsightWeeklyResponseDto
import com.fesi.flowit.heatmap.dto.HeatmapMonthlyResponseDto
import com.fesi.flowit.heatmap.dto.HeatmapWeeklyResponseDto
import com.fesi.flowit.heatmap.service.HeatmapService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.YearMonth

private val log = loggerFor<TodoTimerHeatmapController>()

@Tag(name = "작업 시간 분석(Heatmap)")
@RestController
class TodoTimerHeatmapController(
    private val heatmapService: HeatmapService
) : HeatmapController {

    @GetMapping("/heatmaps/todo-timers/weekly/{date}")
    override fun getWeeklyHeatmap(
        @AuthUserId userId: Long,
        @PathVariable("date") date: LocalDate
    ): ResponseEntity<ApiResult<List<HeatmapWeeklyResponseDto>>> {
        log.debug("request getWeeklyHeatmap(userId=${userId}, date=${date})")

        return ApiResponse.ok(heatmapService.getWeeklyHeatmap(userId, date))
    }

    @GetMapping("/heatmaps/todo-timers/insight/weekly/{date}")
    override fun getWeeklyHeatmapInsight(
        @AuthUserId userId: Long,
        @PathVariable("date") date: LocalDate
    ): ResponseEntity<ApiResult<HeatmapInsightWeeklyResponseDto>> {
        log.debug("request getWeeklyHeatmapInsight(userId=${userId}, date=${date}}")

        return ApiResponse.ok(heatmapService.getWeeklyInsight(userId, date))
    }

    @GetMapping("/heatmaps/todo-timers/monthly/{yearMonth}")
    override fun getMonthlyHeatmap(
        @AuthUserId userId: Long,

        @PathVariable("yearMonth")
        @DateTimeFormat(pattern = "yyyy-MM")
        yearMonth: YearMonth
    ): ResponseEntity<ApiResult<HeatmapMonthlyResponseDto>> {
        log.debug("request getMonthlyHeatmap(userId=${userId}, yearMonth=${yearMonth})")

        return ApiResponse.ok(heatmapService.getMonthlyHeatmap(userId, yearMonth))
    }

    @GetMapping("/heatmaps/todo-timers/insight/monthly/{yearMonth}")
    override fun getMonthlyHeatmapInsight(
        @AuthUserId userId: Long,

        @PathVariable("yearMonth")
        @DateTimeFormat(pattern = "yyyy-MM")
        yearMonth: YearMonth
    ): ResponseEntity<ApiResult<HeatmapInsightMonthlyResponseDto>> {
        log.debug("request getMonthlyHeatmapInsight(userId=${userId}, yearMonth=${yearMonth})")

        return ApiResponse.ok(heatmapService.getMonthlyInsight(userId, yearMonth))
    }
}
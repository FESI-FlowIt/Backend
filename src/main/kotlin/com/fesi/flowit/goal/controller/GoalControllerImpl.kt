package com.fesi.flowit.goal.controller

import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.goal.dto.*
import com.fesi.flowit.goal.service.GoalService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.YearMonth

@Tag(name = "목표")
@RestController
class GoalControllerImpl(
    private val goalService: GoalService
) : GoalController {

    @PostMapping("/goal")
    override fun createGoal(@RequestBody request: GoalCreateRequestDto): ResponseEntity<ApiResult<GoalCreateResponseDto>> {
        val result = goalService.createGoal(
            name = request.name,
            color = request.color,
            dueDateTime = request.dueDateTime
        )

        return ApiResponse.created(result)
    }

    @GetMapping("/goals")
    override fun getAllGoals(): ResponseEntity<ApiResult<List<GoalFindAllResponseDto>>> {
        // @TODO user_id 필요
        return ApiResponse.ok(goalService.getAllGoals())
    }

    @GetMapping("/goals/todos")
    override fun getGoalsSummary(): ResponseEntity<ApiResult<List<GoalSummaryResponseDto>>> {
        return ApiResponse.ok(goalService.getGoalsSummaries())
    }

    @GetMapping("/goals/todos/due-monthly")
    override fun getGoalsByDueMonth(
        @RequestParam(name = "date", required = true)
        @DateTimeFormat(pattern = "yyyy-MM")
        dueYearMonth: YearMonth
    ): ResponseEntity<ApiResult<GoalsByMonthlyResponseDto>> {
        return ApiResponse.ok(goalService.getGoalSummariesByDueYearMonth(dueYearMonth))
    }
}
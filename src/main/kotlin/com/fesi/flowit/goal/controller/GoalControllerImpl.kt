package com.fesi.flowit.goal.controller

import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.goal.dto.*
import com.fesi.flowit.goal.service.GoalService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.YearMonth

private val log = loggerFor<GoalControllerImpl>()

@Tag(name = "목표")
@RestController
class GoalControllerImpl(
    private val goalService: GoalService
) : GoalController {

    @PostMapping("/goals")
    override fun createGoal(@RequestBody request: GoalCreateRequestDto): ResponseEntity<ApiResult<GoalInfoResponseDto>> {
        log.debug(">> request createGoal(${request})")

        val result = goalService.createGoal(
            userId = request.userId,
            name = request.name,
            color = request.color,
            dueDateTime = request.dueDateTime
        )

        return ApiResponse.created(result)
    }

    @PatchMapping("/goals")
    override fun modifyGoal(@RequestBody request: GoalModifyRequestDto): ResponseEntity<ApiResult<GoalInfoResponseDto>> {
        log.debug(">> request modifyGoal(${request})")

        val result = goalService.modifyGoal(
            goalId = request.goalId,
            userId = request.userId,
            name = request.name,
            color = request.color,
            dueDateTime = request.dueDateTime
        )

        return ApiResponse.ok(result)
    }

    @GetMapping("/goals/{userId}")
    override fun getAllGoals(@PathVariable userId: Long): ResponseEntity<ApiResult<List<GoalFindAllResponseDto>>> {
        log.debug(">> request getAllGoals(userId=${userId}")
        return ApiResponse.ok(goalService.getAllGoals(userId))
    }

    @GetMapping("/goals/todos/{userId}")
    override fun getGoalsSummary(@PathVariable userId: Long): ResponseEntity<ApiResult<List<GoalSummaryResponseDto>>> {
        log.debug(">> request getGoalsSummary(userId=${userId}")
        return ApiResponse.ok(goalService.getGoalsSummaries(userId))
    }

    @GetMapping("/goals/todos/due-monthly")
    override fun getGoalsByDueMonth(
        userId: Long,

        @RequestParam(name = "date", required = true)
        @DateTimeFormat(pattern = "yyyy-MM")
        dueYearMonth: YearMonth
    ): ResponseEntity<ApiResult<GoalsByMonthlyResponseDto>> {
        log.debug(">> request getGoalsByDueMonth(userId=${userId}")
        return ApiResponse.ok(goalService.getGoalSummariesByDueYearMonth(userId, dueYearMonth))
    }
}
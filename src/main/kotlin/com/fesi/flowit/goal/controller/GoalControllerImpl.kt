package com.fesi.flowit.goal.controller

import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.common.response.PageResponse
import com.fesi.flowit.goal.dto.*
import com.fesi.flowit.goal.search.GoalSortCriteria
import com.fesi.flowit.goal.service.GoalService
import com.fesi.flowit.goal.search.GoalWidgetCondition
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
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

    @PatchMapping("/goals/{goalId}")
    override fun modifyGoal(@PathVariable("goalId") goalId: Long,
                            @RequestBody request: GoalModifyRequestDto): ResponseEntity<ApiResult<GoalInfoResponseDto>> {
        log.debug(">> request modifyGoal(${request})")

        val result = goalService.modifyGoal(
            goalId = goalId,
            userId = request.userId,
            name = request.name,
            color = request.color,
            dueDateTime = request.dueDateTime
        )

        return ApiResponse.ok(result)
    }

    @PatchMapping("/goals/{goalId}/pin")
    override fun changePinStatus(@PathVariable("goalId") goalId: Long,
                                 @RequestBody request: GoalChangePinRequestDto): ResponseEntity<ApiResult<GoalChangePinResponseDto>> {
        log.debug(">> request changePinStatus(goalId=${goalId}, request=${request})")

        return ApiResponse.ok(goalService.changePinStatus(goalId, request.userId, request.isPinned))
    }

    @DeleteMapping("/goals/{goalId}")
    override fun deleteGoal(@PathVariable("goalId") goalId: Long,
                            @RequestParam("userId") userId: Long
    ): ResponseEntity<ApiResult<Unit>> {
        log.debug(">> request deleteGoal(userId=${userId}, goalId=${goalId})")

        goalService.deleteGoalById(userId, goalId)

        return ApiResponse.noContent()
    }

    @GetMapping("/goals")
    override fun getAllGoals(@RequestParam("userId") userId: Long): ResponseEntity<ApiResult<List<GoalFindAllResponseDto>>> {
        log.debug(">> request getAllGoals(userId=${userId})")
        return ApiResponse.ok(goalService.getAllGoals(userId))
    }

    @GetMapping("/goals/{goalId}/summary")
    override fun getGoalSummary(@PathVariable("goalId") goalId: Long,
                       @RequestParam("userId") userId: Long): ResponseEntity<ApiResult<GoalSummaryResponseDto>> {
        log.debug(">> request getGoalSummary(userId=${userId}, goalId=${goalId})")

        return ApiResponse.ok(goalService.getGoalsSummary(userId, goalId))
    }

    @GetMapping("/goals/dashboard/summaries")
    override fun getGoalSummariesInDashboard(@RequestParam("userId") userId: Long): ResponseEntity<ApiResult<List<GoalSummaryResponseDto>>> {
        log.debug(">> request getGoalSummariesInDashboard(userId=${userId})")
        return ApiResponse.ok(goalService.getGoalsSummariesInDashboard(userId))
    }

    @GetMapping("/goals/summaries")
    override fun searchGoalSummaries(
        @RequestParam("userId") userId: Long,
        @RequestParam("isPinned") isPinned: Boolean,
        @RequestParam("sortedBy") sortedBy: GoalSortCriteria,
        pageable: Pageable
    ): ResponseEntity<ApiResult<PageResponse<GoalSummaryResponseDto>>> {
        log.debug(">> request searchGoalSummaries(userId=${userId}, isPinned=${isPinned}, sortedBy=${sortedBy} page=${pageable})")

        val searchCond = GoalWidgetCondition(userId, sortedBy, isPinned)
        return ApiResponse.ok(goalService.searchGoalSummaries(searchCond, pageable))
    }

    @GetMapping("/goals/todos/due-monthly")
    override fun getGoalsByDueMonth(
        @RequestParam("userId") userId: Long,

        @RequestParam(name = "date", required = true)
        @DateTimeFormat(pattern = "yyyy-MM")
        dueYearMonth: YearMonth
    ): ResponseEntity<ApiResult<GoalsByMonthlyResponseDto>> {
        log.debug(">> request getGoalsByDueMonth(userId=${userId})")
        return ApiResponse.ok(goalService.getGoalSummariesByDueYearMonth(userId, dueYearMonth))
    }
}
package com.fesi.flowit.goal.service

import com.fesi.flowit.common.response.PageResponse
import com.fesi.flowit.goal.dto.*
import com.fesi.flowit.goal.entity.Goal
import com.fesi.flowit.goal.search.GoalWidgetCondition
import com.fesi.flowit.user.entity.User
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.time.YearMonth

interface GoalService {
    fun createGoal(userId: Long, name: String, color: String, dueDateTime: LocalDateTime): GoalInfoResponseDto
    fun modifyGoal(goalId: Long, userId: Long, name: String, color: String, dueDateTime: LocalDateTime): GoalInfoResponseDto
    fun changePinStatus(goalId: Long, userId: Long, isPinned: Boolean): GoalChangePinResponseDto
    fun deleteGoalById(userId: Long, goalId: Long)
    fun getAllGoals(userId: Long): List<GoalFindAllResponseDto>
    fun getGoalById(goalId: Long): Goal
    fun doesNotUserOwnGoal(user: User, goal: Goal): Boolean
    fun getGoalsSummariesInDashboard(userId: Long): List<GoalSummaryResponseDto>
    fun getGoalSummariesByDueYearMonth(userId: Long, dueYearMonth: YearMonth): GoalsByMonthlyResponseDto
    fun searchGoalSummaries(cond: GoalWidgetCondition, pageable: Pageable): PageResponse<GoalSummaryResponseDto>
}
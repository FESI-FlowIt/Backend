package com.fesi.flowit.goal.service

import com.fesi.flowit.goal.dto.GoalsByMonthlyResponseDto
import com.fesi.flowit.goal.dto.GoalCreateResponseDto
import com.fesi.flowit.goal.dto.GoalFindAllResponseDto
import com.fesi.flowit.goal.dto.GoalSummaryResponseDto
import com.fesi.flowit.goal.entity.Goal
import java.time.LocalDateTime
import java.time.YearMonth

interface GoalService {
    fun createGoal(name: String, color: String, dueDateTime: LocalDateTime): GoalCreateResponseDto
    fun getAllGoals(): List<GoalFindAllResponseDto>
    fun getGoalById(goalId: Long): Goal?
    fun getGoalsSummaries(): List<GoalSummaryResponseDto>
    fun getGoalSummariesByDueYearMonth(dueYearMonth: YearMonth): GoalsByMonthlyResponseDto
}
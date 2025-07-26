package com.fesi.flowit.goal.service

import com.fesi.flowit.goal.dto.GoalsByMonthlyResponseDto
import com.fesi.flowit.goal.dto.GoalInfoResponseDto
import com.fesi.flowit.goal.dto.GoalFindAllResponseDto
import com.fesi.flowit.goal.dto.GoalSummaryResponseDto
import com.fesi.flowit.goal.entity.Goal
import java.time.LocalDateTime
import java.time.YearMonth

interface GoalService {
    fun createGoal(userId: Long, name: String, color: String, dueDateTime: LocalDateTime): GoalInfoResponseDto
    fun modifyGoal(goalId: Long, userId: Long, name: String, color: String, dueDateTime: LocalDateTime): GoalInfoResponseDto
    fun getAllGoals(userId: Long): List<GoalFindAllResponseDto>
    fun getGoalById(goalId: Long): Goal?
    fun getGoalsSummaries(userId: Long): List<GoalSummaryResponseDto>
    fun getGoalSummariesByDueYearMonth(userId: Long, dueYearMonth: YearMonth): GoalsByMonthlyResponseDto
}
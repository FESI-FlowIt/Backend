package com.fesi.flowit.goal.service

import com.fesi.flowit.goal.dto.GoalCreateResponseDto
import com.fesi.flowit.goal.dto.GoalFindAllResponseDto
import com.fesi.flowit.goal.entity.Goal
import java.time.LocalDateTime

interface GoalService {
    fun createGoal(name: String, color: String, dueDateTime: LocalDateTime): GoalCreateResponseDto
    fun findAllGoals(): List<GoalFindAllResponseDto>
    fun findGoalById(goalId: Long): Goal?
}
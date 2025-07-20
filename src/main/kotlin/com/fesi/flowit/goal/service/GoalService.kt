package com.fesi.flowit.goal.service

import com.fesi.flowit.goal.dto.GoalCreateResponseDto
import com.fesi.flowit.goal.dto.GoalFindAllResponseDto
import java.time.LocalDateTime

interface GoalService {
    fun createGoal(name: String, color: String, dueDateTime: LocalDateTime): GoalCreateResponseDto
    fun findAllGoals(): List<GoalFindAllResponseDto>
}
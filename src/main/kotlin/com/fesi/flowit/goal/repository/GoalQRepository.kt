package com.fesi.flowit.goal.repository

import com.fesi.flowit.goal.dto.GoalFindAllResponseDto

interface GoalQRepository {
    fun findAllGoals(userId: Long): List<GoalFindAllResponseDto>
}
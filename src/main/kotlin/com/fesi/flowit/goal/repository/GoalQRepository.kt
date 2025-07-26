package com.fesi.flowit.goal.repository

import com.fesi.flowit.goal.dto.GoalFindAllResponseDto
import com.fesi.flowit.user.entity.User

interface GoalQRepository {
    fun findAllGoalsByUser(user: User): List<GoalFindAllResponseDto>
}
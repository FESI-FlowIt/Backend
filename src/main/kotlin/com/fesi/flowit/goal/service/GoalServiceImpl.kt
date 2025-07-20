package com.fesi.flowit.goal.service

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.GoalException
import com.fesi.flowit.goal.dto.GoalCreateResponseDto
import com.fesi.flowit.goal.dto.GoalFindAllResponseDto
import com.fesi.flowit.goal.entity.Goal
import com.fesi.flowit.goal.repository.GoalQRepository
import com.fesi.flowit.goal.repository.GoalRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class GoalServiceImpl(
    private val goalRepository: GoalRepository,
    private val goalQRepository: GoalQRepository
) : GoalService {

    /**
     * 목표 생성
     */
    @Transactional
    override fun createGoal(name: String, color: String, dueDateTime: LocalDateTime): GoalCreateResponseDto {
        val createdDateTime: LocalDateTime = LocalDateTime.now()

        if (isInvalidDueDateTime(dueDateTime, createdDateTime)) {
            throw GoalException.fromCode(ApiResultCode.GOAL_INVALID_DUE_DATETIME)
        }

        val goal: Goal = goalRepository.save(Goal.of(
            name = name,
            color = color,
            createdDateTime = createdDateTime,
            modifiedDateTime = createdDateTime,
            dueDateTime = dueDateTime,
            isPinned = false
        ))

        return GoalCreateResponseDto.fromGoal(goal)
    }

    /**
     * 모든 목표 조회
     */
    override fun findAllGoals(): List<GoalFindAllResponseDto> {
        return goalQRepository.findAllGoals()
    }

    override fun findGoalById(goalId: Long): Goal? {
        return goalRepository.findById(goalId).orElse(null)
    }

    private fun isInvalidDueDateTime(dueDateTime: LocalDateTime, createDateTime: LocalDateTime): Boolean {
        return dueDateTime.isBefore(createDateTime)
    }
}
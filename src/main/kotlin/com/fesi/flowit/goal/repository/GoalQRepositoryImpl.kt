package com.fesi.flowit.goal.repository

import com.fesi.flowit.goal.dto.GoalFindAllResponseDto
import com.fesi.flowit.goal.dto.QGoalFindAllResponseDto
import com.fesi.flowit.goal.entity.QGoal
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class GoalQRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : GoalQRepository {
    private val goal = QGoal.goal

    override fun findAllGoals(userId: Long) : List<GoalFindAllResponseDto> {
        return queryFactory
            .select(QGoalFindAllResponseDto(goal.id, goal.name, goal.color, goal.isPinned))
            .from(goal)
            .where(isSameUserId(userId))
            .orderBy(goal.isPinned.desc())
            .fetch()
    }

    private fun isSameUserId(userId: Long): BooleanExpression {
        return goal.user.id.eq(userId)
    }
}
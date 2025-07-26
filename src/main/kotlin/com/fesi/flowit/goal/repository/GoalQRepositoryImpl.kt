package com.fesi.flowit.goal.repository

import com.fesi.flowit.goal.dto.GoalFindAllResponseDto
import com.fesi.flowit.goal.dto.QGoalFindAllResponseDto
import com.fesi.flowit.goal.entity.QGoal
import com.fesi.flowit.user.entity.User
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class GoalQRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : GoalQRepository {
    private val goal = QGoal.goal

    override fun findAllGoalsByUser(user: User) : List<GoalFindAllResponseDto> {
        return queryFactory
            .select(QGoalFindAllResponseDto(goal.id, goal.name, goal.color, goal.isPinned))
            .from(goal)
            .where(isOwnedBy(user))
            .orderBy(goal.isPinned.desc())
            .fetch()
    }

    private fun isOwnedBy(user: User): BooleanExpression {
        return goal.user.eq(user)
    }
}
package com.fesi.flowit.goal.repository

import com.fesi.flowit.goal.dto.GoalFindAllResponseDto
import com.fesi.flowit.goal.dto.QGoalResponseDto
import com.fesi.flowit.goal.entity.QGoal
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class GoalQRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : GoalQRepository {
    private val goal = QGoal.goal

    override fun findAllGoals() : List<GoalFindAllResponseDto> {
        return queryFactory
            .select(QGoalResponseDto(goal.name, goal.color, goal.isPinned))
            .from(goal)
            .orderBy(goal.isPinned.desc())
            .fetch()
    }
}
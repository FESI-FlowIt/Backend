package com.fesi.flowit.goal.repository

import com.fesi.flowit.goal.dto.GoalFindAllResponseDto
import com.fesi.flowit.goal.dto.QGoalFindAllResponseDto
import com.fesi.flowit.goal.entity.QGoal
import com.fesi.flowit.goal.search.GoalSortCriteria
import com.fesi.flowit.goal.vo.GoalSummaryVo
import com.fesi.flowit.goal.search.GoalWidgetCondition
import com.fesi.flowit.user.entity.User
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

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

    override fun searchGoals(user: User, cond: GoalWidgetCondition, pageable: Pageable): Page<GoalSummaryVo> {
        val goals = queryFactory
            .select(
                Projections.constructor(
                    GoalSummaryVo::class.java,
                    goal.id,
                    goal.name,
                    goal.color,
                    goal.createdDateTime,
                    goal.dueDateTime,
                    goal.isPinned
                )
            )
            .from(goal)
            .where(
                isOwnedBy(user),
                isOnlyPinned(cond.isPinned),
                isNotExpireGoal()
            )
            .orderBy(orderByGoalSortCriteria(cond.sortedBy))
            .limit(pageable.pageSize.toLong())
            .fetch()

        val totalCount = queryFactory
            .select(goal.count())
            .from(goal)
            .where(
                isOwnedBy(user),
                isOnlyPinned(cond.isPinned),
                isNotExpireGoal()
            )
            .fetchOne() ?: 0

        return PageImpl(goals, pageable, totalCount)
    }

    private fun isOnlyPinned(isPinned: Boolean): BooleanExpression? {
        return if (isPinned) {
            goal.isPinned.eq(true)
        } else {
             null
        }
    }

    private fun isNotExpireGoal(): BooleanExpression {
        return goal.dueDateTime.goe(LocalDateTime.now())
    }

    private fun orderByGoalSortCriteria(cond: GoalSortCriteria): OrderSpecifier<LocalDateTime> {
        return when (cond) {
            GoalSortCriteria.LATEST -> goal.createdDateTime.desc()
            GoalSortCriteria.DUE_DATE -> goal.dueDateTime.asc()
        }
    }

    private fun isOwnedBy(user: User): BooleanExpression {
        return goal.user.eq(user)
    }
}
package com.fesi.flowit.goal.repository

import com.fesi.flowit.goal.dto.GoalFindAllResponseDto
import com.fesi.flowit.goal.dto.QGoalFindAllResponseDto
import com.fesi.flowit.goal.dto.TodoSummaryInGoal
import com.fesi.flowit.goal.entity.Goal
import com.fesi.flowit.goal.entity.QGoal
import com.fesi.flowit.goal.search.GoalSortCriteria
import com.fesi.flowit.goal.vo.GoalSummaryVo
import com.fesi.flowit.goal.search.GoalWidgetCondition
import com.fesi.flowit.note.entity.Note
import com.fesi.flowit.note.entity.QNote.note
import com.fesi.flowit.todo.entity.*
import com.fesi.flowit.todo.vo.TodoSummaryInGoalCond
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
    private val todo = QTodo.todo

    override fun findAllGoalsByUser(user: User) : List<GoalFindAllResponseDto> {
        return queryFactory
            .select(QGoalFindAllResponseDto(goal.id, goal.name, goal.color, goal.isPinned))
            .from(goal)
            .where(isOwnedBy(user))
            .orderBy(goal.isPinned.desc())
            .fetch()
    }

    /**
     * 페이징 조건에 따른 목표 검색
     */
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
                isNotExpireGoal(cond.isExpiredGoals)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(orderByGoalSortCriteria(cond.sortedBy))
            .fetch()

        val totalCount = queryFactory
            .select(goal.count())
            .from(goal)
            .where(
                isOwnedBy(user),
                isOnlyPinned(cond.isPinned),
                isNotExpireGoal(cond.isExpiredGoals)
            )
            .fetchOne() ?: 0

        return PageImpl(goals, pageable, totalCount)
    }

    /**
     * 목표에 대한 할 일 조회
     */
    override fun findTodoSummaryByGoalIds(cond: TodoSummaryInGoalCond): List<TodoSummaryInGoal> {
        return queryFactory
            .select(Projections.constructor(
                TodoSummaryInGoal::class.java,
                todo.id,
                goal.id,
                todo.name,
                todo.isDone
            ))
            .from(goal)
            .leftJoin(todo)
            .on(todo.goal.id.eq(goal.id))
            .where(
                isOwnedBy(cond.user),
                isGoalIdEqual(cond.goalId),
                isGoalIdIn(cond.goalIds),
                isTodoDone(cond.isDone),
                isExistTodo()
            )
            .fetch()
    }

    /**
     * 목표에 포함된 할 일 목록 조회
     */
    override fun findTodosInGoal(user: User, goal: Goal): List<Todo> {
        val todos: List<Todo> = queryFactory
            .selectFrom(todo)
            .leftJoin(todo.materials).fetchJoin()
            .where(
                isOwnedBy(user),
                isTodoInGoal(goal)
            )
            .fetch()

        val todoIds = todos.map { todo -> todo.id }

        val notes: List<Note> = queryFactory
            .selectFrom(note)
            .where(note.todo.id.`in`(todoIds))
            .fetch()

        val notesByTodoId = notes.groupBy { note -> note.todo?.id }
        todos.forEach { todo ->
            todo.notes.clear()
            todo.notes.addAll(notesByTodoId[todo.id] ?: emptyList())
        }
        return todos
    }

    private fun isOnlyPinned(isPinned: Boolean): BooleanExpression? {
        return if (isPinned) {
            goal.isPinned.eq(true)
        } else {
             null
        }
    }

    private fun orderByGoalSortCriteria(cond: GoalSortCriteria): OrderSpecifier<LocalDateTime> {
        return when (cond) {
            GoalSortCriteria.LATEST -> goal.createdDateTime.desc()
            GoalSortCriteria.DUE_DATE -> goal.dueDateTime.asc()
        }
    }

    private fun isGoalIdIn(goalIds: List<Long>?): BooleanExpression? = if (goalIds == null) null else goal.id.`in`(goalIds)
    private fun isGoalIdEqual(goalId: Long?): BooleanExpression? = if (goalId == null) null else goal.id.eq(goalId)
    private fun isNotExpireGoal(isExpired: Boolean?): BooleanExpression? {
        return if (isExpired == null) null else goal.dueDateTime.goe(LocalDateTime.now())
    }
    private fun isOwnedBy(user: User?): BooleanExpression? = if (user == null) null else goal.user.eq(user)
    private fun isExistTodo(): BooleanExpression = todo.id.isNotNull
    private fun isTodoDone(isDone: Boolean?): BooleanExpression? = if (isDone == null) null else todo.isDone.eq(isDone)
    private fun isTodoInGoal(goal: Goal?): BooleanExpression? = if (goal == null) null else todo.goal.eq(goal)
}
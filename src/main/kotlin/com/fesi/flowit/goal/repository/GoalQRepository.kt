package com.fesi.flowit.goal.repository

import com.fesi.flowit.goal.dto.GoalFindAllResponseDto
import com.fesi.flowit.goal.dto.TodoSummaryInGoal
import com.fesi.flowit.goal.vo.GoalSummaryVo
import com.fesi.flowit.goal.search.GoalWidgetCondition
import com.fesi.flowit.todo.vo.TodoSummaryInGoalCond
import com.fesi.flowit.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface GoalQRepository {
    fun findAllGoalsByUser(user: User): List<GoalFindAllResponseDto>
    fun searchGoals(user: User, cond: GoalWidgetCondition, pageable: Pageable): Page<GoalSummaryVo>
    fun findTodoSummaryByGoalIds(cond: TodoSummaryInGoalCond): List<TodoSummaryInGoal>
}
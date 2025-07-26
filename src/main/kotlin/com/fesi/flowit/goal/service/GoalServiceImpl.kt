package com.fesi.flowit.goal.service

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.GoalException
import com.fesi.flowit.goal.dto.*
import com.fesi.flowit.goal.entity.Goal
import com.fesi.flowit.goal.repository.GoalQRepository
import com.fesi.flowit.goal.repository.GoalRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth

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
    override fun getAllGoals(): List<GoalFindAllResponseDto> {
        return goalQRepository.findAllGoals()
    }

    /**
     * 목표 요약 정보 조회 (목표 별 할 일)
     * 고정되어 있는 목표 우선, 이후 최신순으로 정렬해 최대 3개 반환
     */
    @Transactional
    override fun getGoalsSummaries(): List<GoalSummaryResponseDto> {
        // 대상 목표 조회
        val goalsInDashboard = goalRepository.findGoalsInDashboard()
        val goalIds: List<Long> = goalsInDashboard.map { it.goalId }

        // 목표 별 할 일 조회
        val todoSummariesByGoalIds = goalRepository.findTodoSummaryByGoalIds(goalIds)
        val todoMapByGoalId: Map<Long, List<TodoSummaryInGoal>> = todoSummariesByGoalIds.groupBy { it.goalId }

        // 결과 반환
        return goalsInDashboard.map { goal ->
            val todos = todoMapByGoalId[goal.goalId].orEmpty()
            val doneCount = todos.count { it.isDone }
            val progressRate = if (todos.isNotEmpty()) (doneCount.toDouble() / todos.size * 100).toInt() else 0

            GoalSummaryResponseDto.fromTodoSummaryAndProgressRate(
                goalId         = goal.goalId,
                goalName       = goal.goalName,
                color          = goal.color,
                createDateTime = goal.createDateTime,
                dueDateTime    = goal.dueDateTime,
                isPinned       = goal.isPinned,
                todos          = todos,
                progressRate   = progressRate
            )
        }
    }

    /**
     * 월 별 목표 조회 (마감일 기준)
     */
    override fun getGoalSummariesByDueYearMonth(dueYearMonth: YearMonth): GoalsByMonthlyResponseDto {
        val start = dueYearMonth.atDay(1).atStartOfDay()
        val end = dueYearMonth.atEndOfMonth().atTime(LocalTime.MAX)

        // 해당 월에 마감일이 있는 목표 조회
        val goalsInCalenderByMonthly =  goalRepository.findGoalsInCalenderByDueDateMonthly(start, end)

        return GoalsByMonthlyResponseDto.of(dueYearMonth, goalsInCalenderByMonthly)
    }

    override fun getGoalById(goalId: Long): Goal? {
        return goalRepository.findById(goalId).orElse(null)
    }

    private fun isInvalidDueDateTime(dueDateTime: LocalDateTime, createDateTime: LocalDateTime): Boolean {
        return dueDateTime.isBefore(createDateTime)
    }
}
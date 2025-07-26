package com.fesi.flowit.goal.service

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.GoalException
import com.fesi.flowit.common.response.exceptions.UserNotExistsException
import com.fesi.flowit.goal.dto.*
import com.fesi.flowit.goal.entity.Goal
import com.fesi.flowit.goal.repository.GoalQRepository
import com.fesi.flowit.goal.repository.GoalRepository
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth

@Service
class GoalServiceImpl(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val goalQRepository: GoalQRepository
) : GoalService {

    /**
     * 목표 생성
     */
    @Transactional
    override fun createGoal(userId: Long, name: String, color: String, dueDateTime: LocalDateTime): GoalInfoResponseDto {
        val user: User = userRepository.findById(userId)
            .orElseThrow { UserNotExistsException.fromCode(ApiResultCode.AUTH_USER_NOT_EXISTS) }

        val createdDateTime: LocalDateTime = LocalDateTime.now()
        if (isInvalidDueDateTime(dueDateTime, createdDateTime)) {
            throw GoalException.fromCode(ApiResultCode.GOAL_INVALID_DUE_DATETIME)
        }

        val goal: Goal = goalRepository.save(Goal.of(
            user = user,
            name = name,
            color = color,
            createdDateTime = createdDateTime,
            modifiedDateTime = createdDateTime,
            dueDateTime = dueDateTime,
            isPinned = false
        ))

        return GoalInfoResponseDto.fromGoal(goal)
    }

    /**
     * 목표 수정
     */
    @Transactional
    override fun modifyGoal(goalId: Long, userId: Long, name: String, color: String, dueDateTime: LocalDateTime): GoalInfoResponseDto {
        val user: User = userRepository.findById(userId)
            .orElseThrow { UserNotExistsException.fromCode(ApiResultCode.AUTH_USER_NOT_EXISTS) }

        val goal: Goal = goalRepository.findById(goalId)
            .orElseThrow { GoalException.fromCode(ApiResultCode.GOAL_NOT_FOUND) }

        if (goal.user != user) {
            throw GoalException.fromCode(ApiResultCode.GOAL_NOT_MATCH_USER)
        }

        if (isInvalidDueDateTime(dueDateTime, goal.createdDateTime)) {
            throw GoalException.fromCode(ApiResultCode.GOAL_INVALID_DUE_DATETIME)
        }

        goal.name = name
        goal.color = color
        goal.dueDateTime = dueDateTime
        goal.modifiedDateTime = LocalDateTime.now()

        return GoalInfoResponseDto.fromGoal(goal)
    }

    /**
     * 모든 목표 조회
     */
    override fun getAllGoals(userId: Long): List<GoalFindAllResponseDto> {
        return goalQRepository.findAllGoals(userId)
    }

    /**
     * 목표 요약 정보 조회 (목표 별 할 일)
     * 고정되어 있는 목표 우선, 이후 최신순으로 정렬해 최대 3개 반환
     */
    @Transactional
    override fun getGoalsSummaries(userId: Long): List<GoalSummaryResponseDto> {
        // 대상 목표 조회
        val goalsInDashboard = goalRepository.findGoalsInDashboard(userId)
        val goalIds: List<Long> = goalsInDashboard.map { it.goalId }

        // 목표 별 할 일 조회
        val todoSummariesByGoalIds = goalRepository.findTodoSummaryByGoalIds(userId, goalIds)
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
    override fun getGoalSummariesByDueYearMonth(userId: Long, dueYearMonth: YearMonth): GoalsByMonthlyResponseDto {
        val start = dueYearMonth.atDay(1).atStartOfDay()
        val end = dueYearMonth.atEndOfMonth().atTime(LocalTime.MAX)

        // 해당 월에 마감일이 있는 목표 조회
        val goalsInCalenderByMonthly =  goalRepository.findGoalsInCalenderByDueDateMonthly(userId, start, end)

        return GoalsByMonthlyResponseDto.of(dueYearMonth, goalsInCalenderByMonthly)
    }

    override fun getGoalById(goalId: Long): Goal? {
        return goalRepository.findById(goalId).orElse(null)
    }

    private fun isInvalidDueDateTime(dueDateTime: LocalDateTime, createDateTime: LocalDateTime): Boolean {
        return dueDateTime.isBefore(createDateTime)
    }
}
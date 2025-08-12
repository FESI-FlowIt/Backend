package com.fesi.flowit.goal.repository

import com.fesi.flowit.goal.dto.GoalSummaryInCalender
import com.fesi.flowit.goal.entity.Goal
import com.fesi.flowit.goal.vo.GoalSummaryVo
import com.fesi.flowit.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface GoalRepository : JpaRepository<Goal, Long> {
    @Query("""
        SELECT new com.fesi.flowit.goal.vo.GoalSummaryVo(
             g.id, g.name, g.color, g.createdDateTime, g.dueDateTime, g.isPinned
        ) 
        FROM Goal g
        WHERE g.user.id = :userId
        ORDER BY g.isPinned desc
        LIMIT 3
        """)
    fun findGoalsInDashboard(@Param("userId") userId: Long): List<GoalSummaryVo>

    @Query("""
      SELECT new com.fesi.flowit.goal.dto.GoalSummaryInCalender(
             g.id, g.name, g.color, g.createdDateTime, g.dueDateTime
        ) 
        FROM Goal g
        WHERE 
            g.user.id = :userId
            AND g.dueDateTime BETWEEN :startOfMonth AND :endOfMonth
    """)
    fun findGoalsInCalenderByDueDateMonthly(@Param("userId") userId: Long,
                                            @Param("startOfMonth") startOfMonth: LocalDateTime,
                                            @Param("endOfMonth") endOfMonth: LocalDateTime): MutableList<GoalSummaryInCalender>

    @Query("""
       SELECT new com.fesi.flowit.goal.vo.GoalSummaryVo(
             g.id, g.name, g.color, g.createdDateTime, g.dueDateTime, g.isPinned
        ) 
        FROM Goal g
        WHERE
            g.user = :user
            AND g.dueDateTime > CURRENT_TIMESTAMP
    """)
    fun findGoalsInProgress(@Param("user") user: User): List<GoalSummaryVo>

    @Query("""
       SELECT new com.fesi.flowit.goal.vo.GoalSummaryVo(
             g.id, g.name, g.color, g.createdDateTime, g.dueDateTime, g.isPinned
        ) 
        FROM Goal g
        WHERE
            g.user = :user
    """)
    fun findGoalsByUser(@Param("user") user: User): List<GoalSummaryVo>
}
package com.fesi.flowit.timer.repository

import com.fesi.flowit.timer.dto.TodoTimerUserInfo
import com.fesi.flowit.timer.entity.TodoTimer
import com.fesi.flowit.timer.vo.TodoTimerTotalTimeVo
import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TodoTimerRepository : JpaRepository<TodoTimer, Long> {

    @Query("""
        SELECT new com.fesi.flowit.timer.dto.TodoTimerUserInfo(
            u.id,
            tt.id,
            CASE WHEN u.todoTimer.status != 'FINISHED' THEN true ELSE false END,
            u.todoTimer.todo.id,
            u.todoTimer.todo.goal.id
        )
        FROM User u
        INNER JOIN TodoTimer tt
            ON u.todoTimer = tt
        WHERE
            u = :user 
    """)
    fun hasUserTodoTimer(@Param("user") user: User): TodoTimerUserInfo?

    @Query("""
        SELECT new com.fesi.flowit.timer.vo.TodoTimerTotalTimeVo(
            todoTimer.todo.id,
            COALESCE(SUM(FUNCTION('TIMESTAMPDIFF', SECOND, todoTimer.startedDateTime, todoTimer.endedDateTime)), 0),
            COALESCE(SUM(FUNCTION('TIMESTAMPDIFF', SECOND, pausedHistory.pauseStartedDateTime, pausedHistory.pauseEndedDateTime)), 0)
        )
        FROM TodoTimer todoTimer
        LEFT JOIN TodoTimerPauseHistory pausedHistory
          ON pausedHistory.timer = todoTimer
             AND pausedHistory.pauseEndedDateTime IS NOT NULL
        WHERE
            todoTimer.todo = :todo
            AND todoTimer.status = 'FINISHED'
            AND todoTimer.endedDateTime IS NOT NULL 
        GROUP BY todoTimer.todo.id
    """)
    fun calculateTotalRunningTime(@Param("todo") todo: Todo): TodoTimerTotalTimeVo?

    @Query(
        """
        SELECT timer
        FROM TodoTimer timer
        LEFT JOIN FETCH timer.pauseHistories tp 
        WHERE
            timer.todo.user = :user
            AND timer.status = 'FINISHED'
            AND timer.endedDateTime IS NOT NULL
            AND timer.startedDateTime BETWEEN :startDateTime AND :endDateTime
    """
    )
    fun findTodoTimersByStatusAndBetween(
        @Param("startDateTime") startDateTime: LocalDateTime,
        @Param("endDateTime") endDateTime: LocalDateTime,
        @Param("user") user: User
    ): List<TodoTimer>
}
package com.fesi.flowit.timer.repository

import com.fesi.flowit.timer.entity.TodoTimer
import com.fesi.flowit.timer.entity.TodoTimerPauseHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TodoTimerPausedHistoryRepository : JpaRepository<TodoTimerPauseHistory, Long> {
    @Query("""
        SELECT CASE WHEN COUNT(t.id) > 0 THEN true ELSE false END
        FROM TodoTimerPauseHistory t
        WHERE
            t.timer = :todoTimer 
            AND t.pauseEndedDateTime IS NULL
    """)
    fun existPausedTimerNotEndedByTimer(todoTimer: TodoTimer): Boolean

    @Query("""
        SELECT timerHistory
        FROM TodoTimerPauseHistory timerHistory
        JOIN FETCH timerHistory.timer timer
        WHERE
            timerHistory.timer = :todoTimer
            AND timerHistory.pauseEndedDateTime IS NULL
    """)
    fun findPausedTimerByTimer(todoTimer: TodoTimer): List<TodoTimerPauseHistory>

    @Query("""
        SELECT timerHistory
        FROM TodoTimerPauseHistory timerHistory
        JOIN FETCH timerHistory.timer timer
        WHERE
            timerHistory.timer = :todoTimer
    """)
    fun findTodoTimerPauseHistoriesByTimer(todoTimer: TodoTimer): List<TodoTimerPauseHistory>
}
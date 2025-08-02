package com.fesi.flowit.schedule.repository

import com.fesi.flowit.schedule.dto.AssignedSched
import com.fesi.flowit.schedule.entity.Schedule
import com.fesi.flowit.user.entity.User
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ScheduleRepository : JpaRepository<Schedule, Long> {
    @Query("""
        SELECT new com.fesi.flowit.schedule.dto.AssignedSched(
            s.id, t.id, g.color, t.name, g.dueDateTime, s.startedDateTime, s.endedDateTime 
        )
        FROM Schedule s
        JOIN s.todo t
        JOIN t.goal g
        WHERE
            s.user = :user
            AND FUNCTION('DATE', s.startedDateTime) = :date
    """)
    fun findAssignedSchedByDate(@Param("user") user: User, @Param("date") date: LocalDate): List<AssignedSched>

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("""
        DELETE FROM Schedule s 
        WHERE s.id IN :schedIds
    """)
    fun deleteAllByIds(@Param("schedIds") schedIds: List<Long>)
}
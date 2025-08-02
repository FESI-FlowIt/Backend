package com.fesi.flowit.schedule.repository

import com.fesi.flowit.schedule.dto.AssignedSched
import com.fesi.flowit.schedule.entity.Schedule
import com.fesi.flowit.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
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
    fun findAssignedSchedByDate(user: User, date: LocalDate): List<AssignedSched>
}
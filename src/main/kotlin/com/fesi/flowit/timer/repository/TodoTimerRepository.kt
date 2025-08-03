package com.fesi.flowit.timer.repository

import com.fesi.flowit.timer.dto.TodoTimerUserInfo
import com.fesi.flowit.timer.entity.TodoTimer
import com.fesi.flowit.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TodoTimerRepository : JpaRepository<TodoTimer, Long> {

    @Query("""
        SELECT new com.fesi.flowit.timer.dto.TodoTimerUserInfo(
            u.id, 
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
    fun hasUserTodoTimer(user: User): TodoTimerUserInfo?

}
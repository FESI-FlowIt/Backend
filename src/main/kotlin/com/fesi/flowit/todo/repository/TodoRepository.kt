package com.fesi.flowit.todo.repository

import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.todo.vo.TodoSummaryWithDateVo
import com.fesi.flowit.user.entity.User
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface TodoRepository : JpaRepository<Todo, Long> {

    @Query("""
        SELECT new com.fesi.flowit.todo.vo.TodoSummaryWithDateVo(
            t.id, g.color, t.name, t.isDone, t.createdDateTime, t.doneDateTime, g.dueDateTime
        )
        FROM Todo t
        JOIN  t.goal g
        WHERE
            t.user = :user
            AND g.dueDateTime > :date
    """)
    fun findTodosByDueDate(@Param("user") user: User, @Param("date") date: LocalDateTime): MutableList<TodoSummaryWithDateVo>

    @EntityGraph(attributePaths = ["goal", "user"])
    fun findAllByIdIn(@Param("todoIds") todoIds: List<Long>): List<Todo>
}
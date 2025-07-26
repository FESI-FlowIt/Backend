package com.fesi.flowit.todo.entity

import com.fesi.flowit.goal.entity.Goal
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name ="todo")
class Todo private constructor(
    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val isDone: Boolean = false,

    @CreatedDate
    @Column(nullable = false)
    val createdDateTime: LocalDateTime,

    @Column(nullable = false)
    var modifiedDateTime: LocalDateTime,

    @Column
    var doneDateTime: LocalDateTime? = null
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "goal_id")
    var goal: Goal? = null
        set(goal) {
            field = goal
            goal?.addTodo(this)
        }
    companion object {
        fun of(name: String, isDone: Boolean, createdDateTime: LocalDateTime, modifiedDateTime: LocalDateTime): Todo {
            return Todo(name, isDone, createdDateTime, modifiedDateTime, null)
        }

        fun withGoal(name: String, isDone: Boolean, createdDateTime: LocalDateTime, modifiedDateTime: LocalDateTime, goal: Goal): Todo {
            val todo = of(name, isDone, createdDateTime, modifiedDateTime)
            todo.goal = goal
            return todo
        }
    }
}
package com.fesi.flowit.todo.entity

import com.fesi.flowit.goal.entity.Goal
import com.fesi.flowit.user.entity.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name ="todos")
class Todo private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    @Column(nullable = false)
    var name: String,

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    var goal: Goal? = null
        set(goal) {
            field = goal
            goal?.addTodo(this)
        }

    companion object {
        fun of(user: User, name: String, isDone: Boolean, createdDateTime: LocalDateTime, modifiedDateTime: LocalDateTime): Todo {
            return Todo(user, name, isDone, createdDateTime, modifiedDateTime, null)
        }

        fun withGoal(user: User, name: String, isDone: Boolean, createdDateTime: LocalDateTime, modifiedDateTime: LocalDateTime, goal: Goal): Todo {
            val todo = of(user, name, isDone, createdDateTime, modifiedDateTime)
            todo.goal = goal
            return todo
        }
    }
}
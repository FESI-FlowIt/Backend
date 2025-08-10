package com.fesi.flowit.todo.entity

import com.fesi.flowit.goal.entity.Goal
import com.fesi.flowit.note.entity.Note
import com.fesi.flowit.schedule.entity.Schedule
import com.fesi.flowit.timer.entity.TodoTimer
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
    var isDone: Boolean = false,

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

    @OneToMany(mappedBy = "todo", cascade = [CascadeType.ALL], orphanRemoval = true)
    val schedules: MutableList<Schedule> = mutableListOf()

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name ="todo_timer_id", referencedColumnName = "id")
    var todoTimer: TodoTimer? = null

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name ="note_id", referencedColumnName = "id")
    var note: Note? = null
        set(value) {
            field = value
            value?.todo = this
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

    fun initializeTodoTimer() {
        this.todoTimer = null
    }

    fun doesNotUserOwnTodo(user: User): Boolean {
        return this.user != user
    }
}
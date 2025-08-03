package com.fesi.flowit.timer.entity

import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "todo_timers")
class TodoTimer(
    @OneToOne(mappedBy = "todoTimer", fetch = FetchType.LAZY)
    val user: User,

    @OneToOne(mappedBy = "todoTimer", fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    val todo: Todo,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: TodoTimerStatus = TodoTimerStatus.STARTED,

    @Column(nullable = false)
    val createdDateTime: LocalDateTime,

    @Column(nullable = false)
    val startedDateTime: LocalDateTime,

    @Column(nullable = true)
    var endedDateTime: LocalDateTime?,

    @Column(nullable = false)
    val runningTime: Long = 0L
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @OneToMany(mappedBy = "timer", cascade = [CascadeType.ALL], orphanRemoval = true)
    val pauseHistory: MutableList<TodoTimerPauseHistory> = mutableListOf()

    companion object {
        fun startTimer(user: User, todo: Todo, createdDateTime: LocalDateTime): TodoTimer {
            return TodoTimer(
                user = user,
                todo = todo,
                status = TodoTimerStatus.RUNNING,
                createdDateTime = createdDateTime,
                startedDateTime = createdDateTime,
                endedDateTime = null
            )
        }
    }

    fun setUser(user: User) {
        user.todoTimer = this
    }

    fun setTodo(todo: Todo) {
        todo.todoTimer = this
    }

    fun pauseTimer() {
        this.status = TodoTimerStatus.PAUSED
    }

    fun resumeTimer() {
        this.status = TodoTimerStatus.RUNNING
    }

    fun doesNotUserOwnTodoTimer(user: User): Boolean {
        return this.user != user
    }
}
package com.fesi.flowit.user.entity

import com.fesi.flowit.goal.entity.Goal
import com.fesi.flowit.schedule.entity.Schedule
import com.fesi.flowit.timer.entity.TodoTimer
import com.fesi.flowit.todo.entity.Todo
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.Objects

@Entity
@Table(name = "users")
class User(
    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    @get:JvmName("getPasswd")
    val password: String,

    @Column(nullable = false)
    val provider: String = "local",

    @Column(nullable = false)
    val createdAt: LocalDateTime,

    @Column(nullable = true)
    val updatedAt: LocalDateTime,

    @Column(nullable = true)
    val deletedAt: LocalDateTime?,

    @Column(nullable = false)
    val isDeleted: Boolean = false,
): UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val goals: MutableList<Goal> = mutableListOf()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val todos: MutableList<Todo> = mutableListOf()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val schedules: MutableList<Schedule> = mutableListOf()

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name ="todo_timer_id", referencedColumnName = "id")
    var todoTimer: TodoTimer? = null

    companion object {
        fun of(
            email: String,
            name: String,
            password: String,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
            deletedAt: LocalDateTime?,
            isDeleted: Boolean = false
        ): User {
            return User(
                email = email,
                name = name,
                password = password,
                createdAt = createdAt,
                updatedAt = updatedAt,
                deletedAt = deletedAt,
                isDeleted = isDeleted
            )
        }
    }

    fun addGoal(goal: Goal) {
        this.goals.add(goal)
    }

    fun addTodo(todo: Todo) {
        this.todos.add(todo)
    }

    fun initializeTodoTimer() {
        this.todoTimer = null
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (this::class != other::class) {
            return false
        }

        return id == (other as User).id
    }

    override fun hashCode() = Objects.hashCode(id)

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.email
    }
}

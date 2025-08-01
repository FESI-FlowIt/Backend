package com.fesi.flowit.goal.entity

import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.user.entity.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "goals")
class Goal private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var color: String = "#000000",

    @Column(nullable = false)
    var isPinned: Boolean = false,

    @CreatedDate
    @Column(nullable = false)
    val createdDateTime: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    @Column(nullable = false)
    var modifiedDateTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var dueDateTime: LocalDateTime,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @OneToMany(mappedBy = "goal", cascade = [CascadeType.ALL], orphanRemoval = true)
    val todos: MutableList<Todo> = mutableListOf()

    companion object {
        fun of(
            user: User, name: String, color: String, isPinned: Boolean,
            createdDateTime: LocalDateTime, modifiedDateTime: LocalDateTime, dueDateTime: LocalDateTime,
        ): Goal {
            return Goal(user, name, color, isPinned, createdDateTime, modifiedDateTime, dueDateTime)
        }
    }

    fun addTodo(todo: Todo) {
        this.todos.add(todo)
    }
}
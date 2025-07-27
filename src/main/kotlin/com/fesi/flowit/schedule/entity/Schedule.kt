package com.fesi.flowit.schedule.entity

import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.user.entity.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "schedules")
class Schedule private constructor(
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User,

    @ManyToOne
    @JoinColumn(name = "todo_id")
    var todo: Todo,

    @Column(nullable = false)
    var startedDateTime: LocalDateTime,

    @Column(nullable = false)
    var endedDateTime: LocalDateTime,

    @CreatedDate
    @Column(nullable = false)
    var createdDateTime: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    @Column(nullable = false)
    var modifiedDateTime: LocalDateTime = LocalDateTime.now()
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    companion object {
        fun of(user: User, todo: Todo,
               startedDateTime: LocalDateTime, endedDateTime: LocalDateTime, createdDateTime: LocalDateTime): Schedule {
            return Schedule(user, todo, startedDateTime, endedDateTime, createdDateTime, modifiedDateTime = createdDateTime)
        }
    }
}
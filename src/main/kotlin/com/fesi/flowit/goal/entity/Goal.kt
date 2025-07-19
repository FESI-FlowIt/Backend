package com.fesi.flowit.goal.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

/**
 * @todo 회원과 연관 관계 설정 필요
 */

@Entity
@Table(name = "goal")
class Goal private constructor(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var color: String = "#000000",

    @Column(nullable = false)
    val isPinned: Boolean = false,

    @CreatedDate
    @Column(nullable = false)
    val createdDateTime: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    @Column(nullable = false)
    var modifiedDateTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val dueDateTime: LocalDateTime
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    companion object {
        fun of(
            name: String, color: String, isPinned: Boolean,
            createdDateTime: LocalDateTime, modifiedDateTime: LocalDateTime, dueDateTime: LocalDateTime,
        ): Goal {
            return Goal(name, color, isPinned, createdDateTime, modifiedDateTime, dueDateTime)
        }
    }
}
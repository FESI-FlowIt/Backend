package com.fesi.flowit.timer.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name ="todo_timer_pause_histories")
class TodoTimerPauseHistory(
    @ManyToOne(fetch = FetchType.LAZY)
    val timer: TodoTimer,

    val pauseStartedDateTime: LocalDateTime,

    var pauseEndedDateTime: LocalDateTime? = null,

    val totalPausedTime: Long = 0L
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    companion object {
        fun createPauseTimerHistory(timer: TodoTimer, pauseStartedDateTime: LocalDateTime): TodoTimerPauseHistory {
            return TodoTimerPauseHistory(timer = timer, pauseStartedDateTime =  pauseStartedDateTime)
        }
    }
}
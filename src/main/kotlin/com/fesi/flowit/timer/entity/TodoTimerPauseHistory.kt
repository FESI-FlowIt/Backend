package com.fesi.flowit.timer.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name ="todo_timer_pause_history")
class TodoTimerPauseHistory(
    @ManyToOne(fetch = FetchType.LAZY)
    val timer: TodoTimer,

    val pauseStartedDateTime: LocalDateTime,

    val pauseEndedDateTime: LocalDateTime,

    val totalPausedTime: Long = 0L
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
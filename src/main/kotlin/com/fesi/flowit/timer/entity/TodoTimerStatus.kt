package com.fesi.flowit.timer.entity

enum class TodoTimerStatus(
    private val description: String,
    private val isRun: Boolean
) {
    STARTED("시작 상태", true),
    RUNNING("동작 중", true),
    PAUSED("일시 정지", true),
    FINISHED("종료", false)

    ;

    fun hasTimer(): Boolean {
        return this.isRun
    }

    fun isRunning(): Boolean {
        return this == RUNNING
    }
}
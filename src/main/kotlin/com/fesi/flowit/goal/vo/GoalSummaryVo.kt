package com.fesi.flowit.goal.vo

import java.time.LocalDateTime

data class GoalSummaryVo(
    val goalId: Long,
    val goalName: String,
    val color: String,
    val createDateTime: LocalDateTime,
    val dueDateTime: LocalDateTime,
    val isPinned: Boolean,
)
package com.fesi.flowit.goal.vo

import java.time.LocalDateTime

data class GoalSummaryVo(
    var goalId: Long,
    var goalName: String,
    var color: String,
    var createDateTime: LocalDateTime,
    var dueDateTime: LocalDateTime,
    var isPinned: Boolean,
)
package com.fesi.flowit.goal.vo

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class GoalSummaryVo @QueryProjection constructor(
    val goalId: Long,
    val goalName: String,
    val color: String,
    val createDateTime: LocalDateTime,
    val dueDateTime: LocalDateTime,
    val isPinned: Boolean,
)
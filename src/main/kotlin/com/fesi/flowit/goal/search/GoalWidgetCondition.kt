package com.fesi.flowit.goal.search

data class GoalWidgetCondition(
    val userId: Long,
    val sortedBy: GoalSortCriteria,
    val isPinned: Boolean,
    val isExpiredGoals: Boolean = false
)
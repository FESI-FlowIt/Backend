package com.fesi.flowit.todo.vo

import com.fesi.flowit.user.entity.User

data class TodoSummaryInGoalCond(
    var goalIds: List<Long>? = null,
    var goalId: Long? = null,
    var isDone: Boolean? = null,
    var user: User? = null
) {
    companion object {
        fun of(goalIds: List<Long>? = null,
               goalId: Long? = null,
               isDone: Boolean? = null,
               user: User? = null
        ): TodoSummaryInGoalCond {
            return TodoSummaryInGoalCond(
                goalIds = goalIds,
                goalId = goalId,
                isDone = isDone,
                user = user
            )
        }
    }
}
package com.fesi.flowit.todo.dto

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.TodoException
import io.swagger.v3.oas.annotations.media.Schema

data class TodoCreateRequestDto(
    @field:Schema(
        description = "할 일 이름",
        example = "할 일",
        minLength = 1,
        maxLength = 30,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val name: String,

    @field:Schema(
        description = "목표 ID",
        example = "10",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val goalId: Long
) {
    init {
        validateName()
        validateGoalId()
    }

    private fun validateName() {
        if (name.isBlank()) {
            throw TodoException.fromCodeWithMsg(ApiResultCode.BAD_REQUEST, "Goal name is required.")
        }

        if (name.length !in 1..30) {
            throw TodoException.fromCode(ApiResultCode.GOAL_INVALID_NAME_LENGTH)
        }
    }

    private fun validateGoalId() {
        if (goalId < 0) {
            throw TodoException.fromCode(ApiResultCode.GOAL_ID_INVALID)
        }
    }
}
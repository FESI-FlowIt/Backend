package com.fesi.flowit.todo.dto

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.TodoException
import com.fesi.flowit.todo.entity.Todo
import io.swagger.v3.oas.annotations.media.Schema

class TodoCreateResponseDto(
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

    companion object {
        fun fromTodo(todo: Todo): TodoCreateResponseDto {
            return TodoCreateResponseDto(
                name = todo.name,
                goalId = todo.goal?.id ?: throw TodoException.fromCodeWithMsg(ApiResultCode.TODO_INVALID_GOAL, "goal-id is null"))
        }
    }
}
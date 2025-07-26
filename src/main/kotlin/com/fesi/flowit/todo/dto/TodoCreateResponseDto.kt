package com.fesi.flowit.todo.dto

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.TodoException
import com.fesi.flowit.todo.entity.Todo
import io.swagger.v3.oas.annotations.media.Schema

class TodoCreateResponseDto(
    @field:Schema(
        description = "할 일 ID",
        example = "1",
    )
    var todoId: Long,

    @field:Schema(
        description = "할 일 이름",
        example = "할 일",
        minLength = 1,
        maxLength = 30,
    )
    var name: String,

    @field:Schema(
        description = "목표 ID",
        example = "10",
    )
    var goalId: Long
) {

    companion object {
        fun fromTodo(todo: Todo): TodoCreateResponseDto {
            return TodoCreateResponseDto(
                todoId = todo.id ?: throw TodoException.fromCodeWithMsg(ApiResultCode.TODO_INVALID_ID, "goal-id is null"),
                name = todo.name,
                goalId = todo.goal?.id ?: throw TodoException.fromCodeWithMsg(ApiResultCode.TODO_INVALID_GOAL, "goal-id is null"))
        }
    }
}
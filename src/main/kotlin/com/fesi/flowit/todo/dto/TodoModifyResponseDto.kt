package com.fesi.flowit.todo.dto

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.TodoException
import com.fesi.flowit.todo.entity.Todo
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class TodoModifyResponseDto(
    @field:Schema(
        description = "회원 ID",
        example = "1",
    )
    var userId: Long,

    @field:Schema(
        description = "목표 ID",
        example = "10",
    )
    var goalId: Long,

    @field:Schema(
        description = "할 일 이름",
        example = "할 일",
        minLength = 1,
        maxLength = 30,
    )
    var name: String,

    @field:Schema(
        description = "생성 시간",
        example = "2025-07-19T14:29:00 | 2025-07-19",
    )
    var createdDateTime: LocalDateTime,

    @field:Schema(
        description = "수정 시간",
        example = "2025-07-19T14:29:00 | 2025-07-19",
    )
    var modifiedDateTime: LocalDateTime
) {
    companion object {
        fun fromTodo(todo: Todo): TodoModifyResponseDto {
            return TodoModifyResponseDto(
                userId = todo.user.id,
                goalId = todo.goal?.id ?: throw TodoException.fromCode(ApiResultCode.TODO_INVALID_GOAL),
                name = todo.name,
                createdDateTime = todo.createdDateTime,
                modifiedDateTime = todo.modifiedDateTime
            )
        }
    }
}
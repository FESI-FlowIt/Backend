package com.fesi.flowit.todo.dto

import io.swagger.v3.oas.annotations.media.Schema

data class TodoChangeDoneResponseDto(
    @field:Schema(
        description = "할 일 ID",
        example = "1",
    )
    var todoId: Long,

    @field:Schema(
        description = "완료 여부",
        example = "true | false",
    )
    var isDone: Boolean
) {
    companion object {
        fun of(todoId: Long, isDone: Boolean): TodoChangeDoneResponseDto {
            return TodoChangeDoneResponseDto(todoId, isDone)
        }
    }
}
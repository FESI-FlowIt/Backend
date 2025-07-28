package com.fesi.flowit.todo.dto

import io.swagger.v3.oas.annotations.media.Schema

data class TodoChangeDoneRequestDto(
    @field:Schema(
        description = "회원 ID",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    var userId: Long,

    @field:Schema(
        description = "완료 여부",
        example = "true | false",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    var isDone: Boolean
) {
    companion object {
        fun of(userId: Long, isDone: Boolean): TodoChangeDoneRequestDto {
            return TodoChangeDoneRequestDto(userId, isDone)
        }
    }
}
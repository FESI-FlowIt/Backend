package com.fesi.flowit.todo.dto

import io.swagger.v3.oas.annotations.media.Schema

data class TodoChangeDoneRequestDto(
    @field:Schema(
        description = "완료 여부",
        example = "true | false",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    var isDone: Boolean
) {
    companion object {
        fun of(isDone: Boolean): TodoChangeDoneRequestDto {
            return TodoChangeDoneRequestDto(isDone)
        }
    }
}
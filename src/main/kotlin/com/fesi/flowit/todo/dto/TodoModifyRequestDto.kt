package com.fesi.flowit.todo.dto

import io.swagger.v3.oas.annotations.media.Schema

data class TodoModifyRequestDto (
    @field:Schema(
        description = "목표 ID",
        example = "10",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    var goalId: Long,

    @field:Schema(
        description = "할 일 이름",
        example = "할 일",
        minLength = 1,
        maxLength = 30,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    var name: String
)
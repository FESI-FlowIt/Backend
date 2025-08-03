package com.fesi.flowit.timer.dto

import io.swagger.v3.oas.annotations.media.Schema

data class TodoTimerStartRequestDto(
    @field:Schema(
        description = "회원 ID",
        example = "1",
    )
    val userId: Long,

    @field:Schema(
        description = "할 일 ID",
        example = "1",
    )
    val todoId: Long
)
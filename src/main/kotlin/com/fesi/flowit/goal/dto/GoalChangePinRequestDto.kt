package com.fesi.flowit.goal.dto

import io.swagger.v3.oas.annotations.media.Schema

data class GoalChangePinRequestDto(
    @field:Schema(
        description = "회원 ID",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val userId: Long,

    @field:Schema(
        description = "고정 여부",
        example = "true | false",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val isPinned: Boolean
) {
    companion object {
        fun of(userId: Long, isPinned: Boolean): GoalChangePinRequestDto {
            return GoalChangePinRequestDto(userId, isPinned)
        }
    }
}
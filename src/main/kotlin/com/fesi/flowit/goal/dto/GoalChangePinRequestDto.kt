package com.fesi.flowit.goal.dto

import io.swagger.v3.oas.annotations.media.Schema

data class GoalChangePinRequestDto(
    @field:Schema(
        description = "고정 여부",
        example = "true | false",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val isPinned: Boolean
) {
    companion object {
        fun of(isPinned: Boolean): GoalChangePinRequestDto {
            return GoalChangePinRequestDto(isPinned)
        }
    }
}
package com.fesi.flowit.goal.dto

import io.swagger.v3.oas.annotations.media.Schema

data class GoalChangePinResponseDto(
    @field:Schema(
        description = "목표 ID",
        example = "1",
    )
    var goalId: Long,

    @field:Schema(
        description = "고정 여부",
        example = "true | false",
    )
    var isPinned: Boolean
) {
    companion object {
        fun of(goalId: Long, isPinned: Boolean): GoalChangePinResponseDto {
            return GoalChangePinResponseDto(goalId, isPinned)
        }
    }
}
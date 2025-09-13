package com.fesi.flowit.goal.dto

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.GoalException
import com.fesi.flowit.common.extensions.REGEX_RGB_CODE
import com.fesi.flowit.goal.entity.Goal
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class GoalInfoResponseDto(
    @field:Schema(
        description = "목표 ID",
        example = "1",
    )
    var goalId: Long,

    @field:Schema(
        description = "목표 이름",
        example = "목표 이름",
        minLength = 1,
        maxLength = 30,
    )
    var name: String,

    @field:Schema(
        description = "목표 색상 코드",
        example = "#000000",
        pattern = REGEX_RGB_CODE,
    )
    val color: String,

    @field:Schema(
        description = "생성 시간",
        example = "2025-07-19T14:29:00 | 2025-07-19",
    )
    val createdDateTime: LocalDateTime,

    @field:Schema(
        description = "마감 기한",
        example = "2025-07-19T14:29:00 | 2025-07-19",
    )
    val dueDateTime: LocalDateTime,

    @field:Schema(
        description = "수정 시간",
        example = "2025-07-19T14:29:00 | 2025-07-19",
    )
    val modifiedDateTime: LocalDateTime,

    @field:Schema(
        description = "고정 여부",
        example = "true | false",
    )
    val isPinned: Boolean
) {
    companion object {
        fun fromGoal(goal: Goal): GoalInfoResponseDto {
            return GoalInfoResponseDto(
                goalId = goal.id ?: throw GoalException.fromCodeWithMsg(ApiResultCode.GOAL_ID_INVALID, "Failed to create goal."),
                name = goal.name,
                color = goal.color,
                createdDateTime = goal.createdDateTime,
                modifiedDateTime = goal.modifiedDateTime,
                dueDateTime = goal.dueDateTime,
                isPinned = goal.isPinned
            )
        }
    }
}

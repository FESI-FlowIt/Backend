package com.fesi.flowit.goal.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fesi.flowit.common.util.REGEX_RGB_CODE
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class GoalSummaryResponseDto(
    @field:Schema(
        description = "목표 ID",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    var goalId: Long,

    @field:Schema(
        description = "목표 이름",
        example = "목표 이름",
        minLength = 1,
        maxLength = 30,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    var goalName: String,

    @field:Schema(
        description = "목표 색상 코드",
        example = "#000000",
        pattern = REGEX_RGB_CODE,
    )
    var color: String,

    @field:Schema(
        description = "생성 시간",
        example = "2025-07-19T14:29:00 | 2025-07-19",
    )
    var createDateTime: LocalDateTime,

    @field:Schema(
        description = "마감 기한",
        example = "2025-07-19T14:29:00 | 2025-07-19",
    )
    var dueDateTime: LocalDateTime,

    @field:Schema(
        description = "고정 여부",
        example = "true | false",
    )
    var isPinned: Boolean,
) {
    var todos: List<TodoSummaryInGoal>? = null

    @field:Schema(
        description = "진행률 (소숫점 버림)",
        example = "25",
    )
    var progressRate: Int = 0

    companion object {
        fun fromTodoSummaryAndProgressRate(goalId: Long, goalName: String, color: String, createDateTime: LocalDateTime,
                                           dueDateTime: LocalDateTime, isPinned: Boolean, todos: List<TodoSummaryInGoal>,
                                           progressRate: Int
        ): GoalSummaryResponseDto {
            val goalSummaryResponseDto = GoalSummaryResponseDto(goalId, goalName, color, createDateTime, dueDateTime, isPinned)
            goalSummaryResponseDto.todos = todos
            goalSummaryResponseDto.progressRate = progressRate

            return goalSummaryResponseDto
        }
    }
}

data class TodoSummaryInGoal(
    @field:Schema(
        description = "할 일 ID",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    var todoId: Long,

    @get:JsonIgnore
    var goalId: Long,

    @field:Schema(
        description = "할 일 이름",
        example = "할 일 이름",
        minLength = 1,
        maxLength = 30,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    var todoName: String,

    @field:Schema(
        description = "완료 여부",
        example = "true | false",
    )
    var isDone: Boolean
)
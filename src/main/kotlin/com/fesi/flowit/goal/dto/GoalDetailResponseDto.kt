package com.fesi.flowit.goal.dto

import com.fesi.flowit.common.extensions.REGEX_RGB_CODE
import com.fesi.flowit.todo.vo.TodoSummaryInGoalDetailVo
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class GoalDetailResponseDto(
    @field:Schema(
        description = "목표 이름",
        example = "목표1",
    )
    val name: String,

    @field:Schema(
        description = "목표 색상 코드",
        example = "#000000",
        pattern = REGEX_RGB_CODE,
    )
    val color: String,

    @field:Schema(
        description = "마감일",
        example = "2025-07-19",
    )
    val dueDateTime: LocalDateTime,

    @field:Schema(
        description = "진행률 (소숫점 버림)",
        example = "25",
    )
    var progressRate: Int = 0,

    @field:Schema(description = "해당 목표의 할 일 정보")
    val todos: MutableList<TodoSummaryInGoalDetailVo>
) {
    companion object {
        fun of(
            name: String,
            color: String,
            dueDateTime: LocalDateTime,
            progressRate: Int,
            todos: MutableList<TodoSummaryInGoalDetailVo>
        ): GoalDetailResponseDto {
            return GoalDetailResponseDto(name, color, dueDateTime, progressRate, todos)
        }
    }
}
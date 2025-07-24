package com.fesi.flowit.goal.dto

import com.fesi.flowit.common.util.REGEX_RGB_CODE
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.time.YearMonth

data class GoalsByMonthlyResponseDto(
    @field:Schema(
        description = "조회 대상 마감 월",
        example = "2025-07",
        pattern = "yyyy-MM",
    )
    var date: YearMonth,
    var goals: MutableList<GoalSummaryInCalender>
) {
    companion object {
        fun of(date: YearMonth, goals: MutableList<GoalSummaryInCalender>): GoalsByMonthlyResponseDto {
            return GoalsByMonthlyResponseDto(date, goals)
        }
    }
}

data class GoalSummaryInCalender(
    @field:Schema(
        description = "목표 ID",
        example = "10",
    )
    var id: Long,

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
    var color: String,

    @field:Schema(
        description = "생성 시간",
        example = "2025-07-19T14:29:00",
    )
    var createDateTime: LocalDateTime,

    @field:Schema(
        description = "마감 기한",
        example = "2025-07-19T14:29:00",
    )
    var dueDateTime: LocalDateTime
)
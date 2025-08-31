package com.fesi.flowit.heatmap.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class HeatmapInsightWeeklyResponseDto(
    @field:Schema(
        description = "작업 날짜",
        example = "2025-07-19",
    )
    val date: LocalDate,

    @field:Schema(
        description = "인사이트 메시지 목록",
        example = "[이번 주 골든 타임 1회 달성!]",
    )
    val insights: MutableList<String>
) {
    companion object {
        fun of(date: LocalDate, insights: MutableList<String>): HeatmapInsightWeeklyResponseDto {
            return HeatmapInsightWeeklyResponseDto(date, insights)
        }
    }
}
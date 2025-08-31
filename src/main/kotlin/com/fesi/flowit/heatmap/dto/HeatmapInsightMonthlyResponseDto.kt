package com.fesi.flowit.heatmap.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.YearMonth

data class HeatmapInsightMonthlyResponseDto(
    @field:Schema(
        description = "대상 월",
        example = "2025-08",
    )
    val yearMonth: YearMonth,

    @field:Schema(
        description = "인사이트 메시지 목록",
        example = "[이번 달은 1주 차를 열심히 보냈네요!]",
    )
    val insights: MutableList<String>
    ) {
        companion object {
            fun of(yearMonth: YearMonth, insights: MutableList<String>): HeatmapInsightMonthlyResponseDto {
                return HeatmapInsightMonthlyResponseDto(yearMonth, insights)
            }
        }
    }
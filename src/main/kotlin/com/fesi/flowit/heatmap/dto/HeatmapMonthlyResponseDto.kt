package com.fesi.flowit.heatmap.dto

import com.fesi.flowit.heatmap.vo.HeatmapQuarterVo
import io.swagger.v3.oas.annotations.media.Schema
import java.time.YearMonth

data class HeatmapMonthlyResponseDto(
    @field:Schema(
        description = "대상 월",
        example = "2025-07",
    )
    val yearMonth: YearMonth,

    @field:Schema(description = "각 주간 히트맵")
    val weeklyHeatmaps: MutableList<WeeklyHeatmapOfMonth>

) {
    companion object {
        fun fromYearMonth(year: YearMonth): HeatmapMonthlyResponseDto{
            return HeatmapMonthlyResponseDto(year, mutableListOf())
        }
    }

    fun addHeatmaps(weeklyHeatmaps: WeeklyHeatmapOfMonth) {
        this.weeklyHeatmaps.add(weeklyHeatmaps)
    }
}

data class WeeklyHeatmapOfMonth(
    @field:Schema(
        description = "주차",
        example = "1",
    )
    val weekOfMonth: Int,
    val timeSlots: HeatmapQuarterVo
) {
    companion object {
        fun of(weekOfMonth: Int, timeSlots: HeatmapQuarterVo): WeeklyHeatmapOfMonth {
            return WeeklyHeatmapOfMonth(weekOfMonth, timeSlots)
        }
    }
}
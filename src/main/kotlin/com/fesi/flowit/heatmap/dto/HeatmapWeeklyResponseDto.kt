package com.fesi.flowit.heatmap.dto

import com.fesi.flowit.heatmap.vo.HeatmapQuarterVo
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class HeatmapWeeklyResponseDto(
    @field:Schema(
        description = "작업 날짜",
        example = "2025-07-19",
    )
    val date: LocalDate,
    val timeSlots: HeatmapQuarterVo
) {
    companion object {
        fun of(date: LocalDate, timeSlots: HeatmapQuarterVo): HeatmapWeeklyResponseDto {
            return HeatmapWeeklyResponseDto(date, timeSlots)
        }

        fun createIfNoRecordWithDate(date: LocalDate): HeatmapWeeklyResponseDto {
            return HeatmapWeeklyResponseDto(date, HeatmapQuarterVo.createIfNoRecord())
        }
    }
}
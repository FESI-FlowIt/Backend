package com.fesi.flowit.heatmap.service

import com.fesi.flowit.heatmap.dto.HeatmapMonthlyResponseDto
import com.fesi.flowit.heatmap.dto.HeatmapWeeklyResponseDto
import java.time.LocalDate
import java.time.YearMonth

interface HeatmapService {
    fun getWeeklyHeatmap(userId: Long, targetDate: LocalDate): List<HeatmapWeeklyResponseDto>
    fun getMonthlyHeatmap(userId: Long, targetMonth: YearMonth): HeatmapMonthlyResponseDto
}
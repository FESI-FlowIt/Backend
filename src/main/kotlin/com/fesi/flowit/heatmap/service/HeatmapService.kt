package com.fesi.flowit.heatmap.service

import com.fesi.flowit.heatmap.dto.HeatmapWeeklyResponseDto
import java.time.LocalDate

interface HeatmapService {
    fun getWeeklyHeatmap(userId: Long, targetDate: LocalDate): List<HeatmapWeeklyResponseDto>
}
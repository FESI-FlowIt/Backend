package com.fesi.flowit.heatmap.vo

import io.swagger.v3.oas.annotations.media.Schema

data class HeatmapSlot(
    @field:Schema(
        description = "작업 시간 (분)",
        example = "10",
    )
    var minutes: Int,

    @field:Schema(
        description = "Intensity",
        example = "1",
    )
    var intensity: Int
) {
    companion object {
        fun createEmptySlot(): HeatmapSlot {
            return HeatmapSlot(0, 0)
        }

        fun withMinutes(min: Int): HeatmapSlot {
            return HeatmapSlot(min, convertMinutesToIntensity(min))
        }

        fun of(minutes: Long, intensity: Int): HeatmapSlot {
            return of(minutes, intensity)
        }

        private fun convertMinutesToIntensity(min: Int): Int = when {
            min >= 240 -> 4
            min >= 120 -> 3
            min >= 60 -> 2
            min > 0 -> 1
            else -> 0
        }
    }

    fun addMinutes(minutes: Number) {
        this.minutes += minutes.toInt()
    }

    fun updateIntensity() {
        intensity = convertMinutesToIntensity(minutes)
    }
}
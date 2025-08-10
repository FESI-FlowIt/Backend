package com.fesi.flowit.heatmap.vo

import io.swagger.v3.oas.annotations.media.Schema

data class HeatmapQuarterVo(
    @field:Schema(description = "새벽(00:00-06:00)")
    var dawn: HeatmapSlot,

    @field:Schema(description = "오전(06:00-12:00)")
    var morning: HeatmapSlot,

    @field:Schema(description = "오후(12:00-18:00)")
    var afternoon: HeatmapSlot,

    @field:Schema(description = "저녁(18:00-24:00)")
    var evening: HeatmapSlot
) {
    companion object {
        fun of(dawn: HeatmapSlot, morning: HeatmapSlot, afternoon: HeatmapSlot, evening: HeatmapSlot): HeatmapQuarterVo {
            return HeatmapQuarterVo(dawn, morning, afternoon, evening)
        }

        fun createIfNoRecord(): HeatmapQuarterVo {
            return HeatmapQuarterVo(
                dawn      = HeatmapSlot.createEmptySlot(),
                morning   = HeatmapSlot.createEmptySlot(),
                afternoon = HeatmapSlot.createEmptySlot(),
                evening   = HeatmapSlot.createEmptySlot()
            )
        }

        fun createFullTimeRecord(): HeatmapQuarterVo {
            val fullTimeMinutes = 1440

            return HeatmapQuarterVo(
                dawn      = HeatmapSlot.withMinutes(fullTimeMinutes),
                morning   = HeatmapSlot.withMinutes(fullTimeMinutes),
                afternoon = HeatmapSlot.withMinutes(fullTimeMinutes),
                evening   = HeatmapSlot.withMinutes(fullTimeMinutes)
            )
        }
    }

    fun addSlotByQuarterVo(quarterVo: HeatmapQuarterVo) {
        this.dawn.minutes      += quarterVo.dawn.minutes
        this.morning.minutes   += quarterVo.morning.minutes
        this.afternoon.minutes += quarterVo.afternoon.minutes
        this.evening.minutes   += quarterVo.evening.minutes
    }

    fun removeSlotByQuarterVo(quarterVo: HeatmapQuarterVo) {
        this.dawn.minutes      -= quarterVo.dawn.minutes
        this.morning.minutes   -= quarterVo.morning.minutes
        this.afternoon.minutes -= quarterVo.afternoon.minutes
        this.evening.minutes   -= quarterVo.evening.minutes
    }

    fun getHeatmapSlotByTimeQuarter(quarter: TimeQuarter): HeatmapSlot {
        return when (quarter) {
            TimeQuarter.DAWN -> dawn
            TimeQuarter.MORNING -> morning
            TimeQuarter.EVENING -> evening
            TimeQuarter.AFTERNOON -> afternoon
        }
    }

    fun updateSlotIntensity() {
        dawn.updateIntensity()
        morning.updateIntensity()
        afternoon.updateIntensity()
        evening.updateIntensity()
    }
}
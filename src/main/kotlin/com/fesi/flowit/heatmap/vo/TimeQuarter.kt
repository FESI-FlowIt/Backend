package com.fesi.flowit.heatmap.vo

import com.fesi.flowit.common.extensions.isAfterOrEquals
import com.fesi.flowit.common.extensions.isBeforeOrEquals
import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.CommonException
import java.time.Duration
import java.time.LocalTime

private val log = loggerFor<TimeQuarter>()

/**
 * 24시간을 시간대 별 분리
 * DAWN       00:00 - 06:00
 * MORNING    06:00 - 12:00
 * AFTERNOON  12:00 - 18:00
 * EVENING    18:00 - 23:59
 *
 * EVENING의 경우 종료 시간이 24:00이 아닌, 23:59분이므로 시간 계산에 유의
 */

enum class TimeQuarter(
    val startTime: LocalTime,
    val endTime: LocalTime
) {
    DAWN(LocalTime.of(0, 0), LocalTime.of(6, 0)) {
        override fun getNextQuarter(): TimeQuarter {
            return MORNING
        }
    },
    MORNING(LocalTime.of(6, 0), LocalTime.of(12, 0)) {
        override fun getNextQuarter(): TimeQuarter {
            return AFTERNOON
        }
    },
    AFTERNOON(LocalTime.of(12, 0), LocalTime.of(18, 0)) {
        override fun getNextQuarter(): TimeQuarter {
            return EVENING
        }
    },
    EVENING(LocalTime.of(18, 0), LocalTime.MAX) {
        override fun getNextQuarter(): TimeQuarter {
            return MORNING
        }
    }

    ;

    companion object {
        fun from(time: LocalTime): TimeQuarter {
            return values().find { timeQuarter ->
                time.isAfterOrEquals(timeQuarter.startTime) && time.isBeforeOrEquals(timeQuarter.endTime)
            } ?: run {
                log.error("TimeQuarter.from().. ${time} does not fall within any quarter.")
                throw CommonException.fromCodeWithMsg(ApiResultCode.INTERNAL_ERROR, "Failed to convert time to TimeQuarter")
            }
        }
    }

    fun calculateRunningTimeBetween(started: LocalTime, ended: LocalTime): Long {
        val overlapStartTime = maxOf(this.startTime, started)
        val overlapEndTime = minOf(this.endTime, ended)

        if (overlapStartTime.isAfterOrEquals(overlapEndTime)) {
            return 0
        }

        var runningTime = Duration.between(overlapStartTime, overlapEndTime).toMinutes()
        if (overlapEndTime == LocalTime.MAX) {
            runningTime += 1
        }

        return runningTime
    }
    abstract fun getNextQuarter(): TimeQuarter

    fun hasExceededEndTime(ended: LocalTime): Boolean {
        return ended.isAfter(this.endTime)
    }
}
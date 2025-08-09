package com.fesi.flowit.common.extensions

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import java.util.stream.Collectors

fun generateDateRange(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
    return startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList())
}

/**
 * Extension functions for LocalTime
 */
fun LocalTime.isAfterOrEquals(time: LocalTime): Boolean {
    return this.isAfter(time) || this == time
}

fun LocalTime.isBeforeOrEquals(time: LocalTime): Boolean {
    return this.isBefore(time) || this == time
}


/**
 * Extension functions for LocalDate
 */
fun LocalDate.getStartOfWeek(): LocalDate {
    return this.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
}

fun LocalDate.getEndOfWeek(): LocalDate {
    return this.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
}

fun LocalDate.isAfterOrEquals(date: LocalDate): Boolean {
    return this.isAfter(date) || this == date
}

fun LocalDate.isBeforeOrEquals(date: LocalDate): Boolean {
    return this.isBefore(date) || this == date
}
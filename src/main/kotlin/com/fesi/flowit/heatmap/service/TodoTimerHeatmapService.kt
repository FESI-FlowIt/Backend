package com.fesi.flowit.heatmap.service

import com.fesi.flowit.common.extensions.generateDateRange
import com.fesi.flowit.common.extensions.getEndOfWeek
import com.fesi.flowit.common.extensions.getStartOfWeek
import com.fesi.flowit.common.extensions.isBeforeOrEquals
import com.fesi.flowit.heatmap.dto.HeatmapMonthlyResponseDto
import com.fesi.flowit.heatmap.dto.HeatmapWeeklyResponseDto
import com.fesi.flowit.heatmap.dto.WeeklyHeatmapOfMonth
import com.fesi.flowit.heatmap.vo.HeatmapQuarterVo
import com.fesi.flowit.heatmap.vo.TimeQuarter
import com.fesi.flowit.timer.entity.TodoTimer
import com.fesi.flowit.timer.service.TodoTimerService
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.service.UserService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth

@Service
class TodoTimerHeatmapService(
    private val userService: UserService,
    private val todoTimerService: TodoTimerService
) : HeatmapService {
    /**
     * 주간 히트맵 조회
     */
    override fun getWeeklyHeatmap(userId: Long, targetDate: LocalDate): List<HeatmapWeeklyResponseDto> {
        val user: User = userService.findUserById(userId)

        val startDateOfWeek: LocalDate = targetDate.getStartOfWeek()
        val endDateOfWeek: LocalDate = targetDate.getEndOfWeek()
        val weekDates = generateDateRange(startDateOfWeek, endDateOfWeek)

        // TodoTimer의 시작 시간 - 끝 시간(not-null) 조회
        val finishedTodoTimers: List<TodoTimer> = todoTimerService.getFinishedTodoTimerBetween(startDateOfWeek, endDateOfWeek, user)

        if (finishedTodoTimers.isEmpty()) {
            return weekDates.map { day -> HeatmapWeeklyResponseDto.createIfNoRecordWithDate(day) }
        }

        val heatMapQuarterVoByDateMap: MutableMap<LocalDate, HeatmapQuarterVo> = weekDates.associateWith {
            HeatmapQuarterVo.createIfNoRecord()
        }.toMutableMap()

        finishedTodoTimers.forEach { todoTimer ->
            // 타이머 기록 계산
            if (todoTimer.isFinishedTimer()) {
                processTimeRecord(
                    heatMapQuarterVoByDateMap,
                    todoTimer.startedDateTime,
                    todoTimer.endedDateTime!!,
                    endDateOfWeek,
                    HeatmapQuarterVo::addSlotByQuarterVo
                )
            }

            // 일시 중지 기록 차감
            todoTimer.pauseHistories.forEach {pauseHistory ->
                if (pauseHistory.isPausedEnd()) {
                    processTimeRecord(
                        heatMapQuarterVoByDateMap,
                        pauseHistory.pauseStartedDateTime,
                        pauseHistory.pauseEndedDateTime!!,
                        endDateOfWeek,
                        HeatmapQuarterVo::removeSlotByQuarterVo
                    )
                }
            }
        }

        // intensity 업데이트
        for (heatmapQuarterVoInDate in heatMapQuarterVoByDateMap) {
            heatmapQuarterVoInDate.value.updateSlotIntensity()
        }

        return weekDates.map {
            HeatmapWeeklyResponseDto.of(it, heatMapQuarterVoByDateMap[it] ?: HeatmapQuarterVo.createIfNoRecord())
        }
    }

    /**
     * 월간 히트맵 조회
     */
    override fun getMonthlyHeatmap(userId: Long, targetMonth: YearMonth): HeatmapMonthlyResponseDto {
        val user: User = userService.findUserById(userId)

        val firstDayOfMonth = targetMonth.atDay(1)
        val lastDayOfMonth = targetMonth.atEndOfMonth()

        var currentDate = firstDayOfMonth
        var weekNumber = 1

        val response = HeatmapMonthlyResponseDto.fromYearMonth(targetMonth)

        while (currentDate.isBeforeOrEquals(lastDayOfMonth)) {
            val mondayOfWeek = maxOf(currentDate.getStartOfWeek(), firstDayOfMonth)
            val sundayOfWeek = minOf(currentDate.getEndOfWeek(), lastDayOfMonth)
            val weekDates = generateDateRange(mondayOfWeek, sundayOfWeek)

            val finishedTodoTimers: List<TodoTimer> = todoTimerService.getFinishedTodoTimerBetween(mondayOfWeek, sundayOfWeek, user)

            val heatMapQuarterVoByDateMap: MutableMap<LocalDate, HeatmapQuarterVo> =
                weekDates.associateWith { HeatmapQuarterVo.createIfNoRecord() }.toMutableMap()

            finishedTodoTimers.forEach { todoTimer ->
                // 타이머 기록 계산
                if (todoTimer.isFinishedTimer()) {
                    processTimeRecord(
                        heatMapQuarterVoByDateMap,
                        todoTimer.startedDateTime,
                        todoTimer.endedDateTime!!,
                        sundayOfWeek,
                        HeatmapQuarterVo::addSlotByQuarterVo
                    )
                }

                // 일시 중지 기록 차감
                todoTimer.pauseHistories.forEach {pauseHistory ->
                    if (pauseHistory.isPausedEnd()) {
                        processTimeRecord(
                            heatMapQuarterVoByDateMap,
                            pauseHistory.pauseStartedDateTime,
                            pauseHistory.pauseEndedDateTime!!,
                            sundayOfWeek,
                            HeatmapQuarterVo::removeSlotByQuarterVo
                        )
                    }
                }
            }

            // 주간 데이터 변환
            val totalWeeklyHeatmap = HeatmapQuarterVo.createIfNoRecord()

            heatMapQuarterVoByDateMap.forEach { heatmap ->
                heatmap.value.updateSlotIntensity()
                totalWeeklyHeatmap.addSlotByQuarterVo(heatmap.value)
            }

            response.addHeatmaps(WeeklyHeatmapOfMonth.of(weekNumber, totalWeeklyHeatmap))

            currentDate = sundayOfWeek.plusDays(1)
            weekNumber++
        }

        return response
    }

    /**
     * 타이머 시간 기록 계산
     * @param heatMapQuarterVoByDateMap 날짜 별 기록 저장을 위한 Map
     * @param startedDateTime 기록 시작 시간
     * @param endedDateTime 기록 종료 시간
     * @param endDateOfWeek 이번 주 마지막 날(일요일)
     * @param operationForRunningTime 계산한 측정 시간을 처리하기 위한 메소드
     */
    private fun processTimeRecord(
        heatMapQuarterVoByDateMap: MutableMap<LocalDate, HeatmapQuarterVo>,
        startedDateTime: LocalDateTime,
        endedDateTime: LocalDateTime,
        endDateOfWeek: LocalDate,
        operationForRunningTime: HeatmapQuarterVo.(HeatmapQuarterVo) -> Unit
    ) {
        // 일 별 타이머 기록 계산
        val timerStartedDate = startedDateTime.toLocalDate()
        val timerEndedDate = endedDateTime.toLocalDate()

        val timerStartedTime = startedDateTime.toLocalTime()
        val timerEndedTime = endedDateTime.toLocalTime()

        if (isMultiDayRecord(timerStartedDate, timerEndedDate)) {
            // 기록 시작 날짜
            val runningTimeInStartDay = calculateHeatmapQuarterInDay(timerStartedTime, LocalTime.MAX)
            heatMapQuarterVoByDateMap[timerStartedDate]?.operationForRunningTime(runningTimeInStartDay)

            // 기록 중인 날짜
            var allTimeRunningDate = timerStartedDate

            while (isRequiredCalculateNextRecord(allTimeRunningDate, minOf(timerEndedDate, endDateOfWeek))) {
                allTimeRunningDate = allTimeRunningDate.plusDays(1)
                heatMapQuarterVoByDateMap[allTimeRunningDate]?.operationForRunningTime(HeatmapQuarterVo.createFullTimeRecord())
            }

            // 기록 종료 날짜
            if (timerEndedDate.isBeforeOrEquals(endDateOfWeek)) {
                val runningTimeInEndDay = calculateHeatmapQuarterInDay(LocalTime.MIN, timerEndedTime)
                heatMapQuarterVoByDateMap[timerEndedDate]?.operationForRunningTime(runningTimeInEndDay)
            }
        } else {
            // 당일 기록
            val runningTimeInDay = calculateHeatmapQuarterInDay(timerStartedTime,timerEndedTime)
            heatMapQuarterVoByDateMap[timerStartedDate]?.operationForRunningTime(runningTimeInDay)
        }
    }

    /**
     * 하루 내 시간대 별 히트맵 계산
     */
    private fun calculateHeatmapQuarterInDay(startedTime: LocalTime, endedTime: LocalTime): HeatmapQuarterVo {
        val heatmapQuarterInDay = HeatmapQuarterVo.createIfNoRecord()

        // 시작 시간대 동작 시간 계산
        var timeQuarter = TimeQuarter.from(startedTime)

        heatmapQuarterInDay
            .getHeatmapSlotByTimeQuarter(timeQuarter)
            .addMinutes(timeQuarter.calculateRunningTimeBetween(startedTime, endedTime))

        // 이후 시간대 동작 시간 계산
        while (timeQuarter.hasExceededEndTime(endedTime)) {
            timeQuarter = timeQuarter.getNextQuarter()

            heatmapQuarterInDay
                .getHeatmapSlotByTimeQuarter(timeQuarter)
                .addMinutes(timeQuarter.calculateRunningTimeBetween(startedTime, endedTime))
        }

        return heatmapQuarterInDay
    }

    private fun isRequiredCalculateNextRecord(targetDate: LocalDate, lastDay: LocalDate): Boolean {
        return targetDate.isBefore(lastDay)
    }

    private fun isMultiDayRecord(startedDate: LocalDate, endedDate: LocalDate): Boolean {
        return startedDate.isBefore(endedDate)
    }
}
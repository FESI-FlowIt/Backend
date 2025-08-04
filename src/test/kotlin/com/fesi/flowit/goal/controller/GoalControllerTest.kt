package com.fesi.flowit.goal.controller

import com.fesi.flowit.common.response.PageResponse
import com.fesi.flowit.goal.dto.*
import com.fesi.flowit.goal.search.GoalSortCriteria
import com.fesi.flowit.goal.service.GoalService
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.time.YearMonth

class GoalControllerTest : StringSpec({
    "목표 생성 요청을 받을 수 있다" {
        val request = GoalCreateRequestDto(
            userId = 1,
            name = "건강 프로젝트",
            color = "#00FF00",
            dueDateTime = LocalDateTime.of(2025, 8, 4, 12, 0)
        )

        val service = mockk<GoalService>(relaxed = true)
        every {
            service.createGoal(
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk<GoalInfoResponseDto>()

        val controller = GoalControllerImpl(service)

        controller.createGoal(request)
    }

    "목표 수정 요청을 받을 수 있다" {
        val request = GoalModifyRequestDto(
            userId = 1,
            name = "업데이트된 목표",
            color = "#0000FF",
            dueDateTime = LocalDateTime.of(2025, 12, 31, 23, 59)
        )

        val service = mockk<GoalService>(relaxed = true)
        every {
            service.modifyGoal(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk<GoalInfoResponseDto>()

        val controller = GoalControllerImpl(service)

        controller.modifyGoal(goalId = 10L, request)
    }

    "목표 고정 상태 변경 요청을 받을 수 있다" {
        val request = GoalChangePinRequestDto(userId = 1, isPinned = true)

        val service = mockk<GoalService>(relaxed = true)
        every {
            service.changePinStatus(
                any(),
                any(),
                any()
            )
        } returns mockk<GoalChangePinResponseDto>()

        val controller = GoalControllerImpl(service)

        controller.changePinStatus(goalId = 5L, request)
    }

    "목표 삭제 요청을 받을 수 있다" {
        val service = mockk<GoalService>(relaxed = true)
        every { service.deleteGoalById(any(), any()) } just runs

        val controller = GoalControllerImpl(service)

        controller.deleteGoal(goalId = 2L, userId = 1L)
    }

    "모든 목표 조회 요청을 받을 수 있다" {
        val service = mockk<GoalService>(relaxed = true)
        every { service.getAllGoals(any()) } returns emptyList()

        val controller = GoalControllerImpl(service)

        controller.getAllGoals(userId = 1L)
    }

    "하나의 목표 조회 요청을 받을 수 있다" {
        val service = mockk<GoalService>(relaxed = true)
        every { service.getGoalsSummary(any(), any()) } returns mockk<GoalSummaryResponseDto>()

        val controller = GoalControllerImpl(service)

        controller.getGoalSummary(userId = 1L, goalId = 100L)
    }

    "목표 별 할 일 조회 요청을 받을 수 있다" {
        val service = mockk<GoalService>(relaxed = true)
        every { service.getGoalsSummariesInDashboard(any()) } returns emptyList()

        val controller = GoalControllerImpl(service)

        controller.getGoalSummariesInDashboard(userId = 1L)
    }

    "조건에 따른 모든 목표 조회 요청을 받을 수 있다" {
        val service = mockk<GoalService>(relaxed = true)
        every {
            service.searchGoalSummaries(any(), any())
        } returns mockk<PageResponse<GoalSummaryResponseDto>>()

        val controller = GoalControllerImpl(service)

        val pageable = PageRequest.of(0, 10)
        controller.searchGoalSummaries(
            userId = 1L,
            isPinned = true,
            sortedBy = GoalSortCriteria.DUE_DATE,
            pageable = pageable
        )
    }

    "월별 목표 조회 요청을 받을 수 있다" {
        val service = mockk<GoalService>(relaxed = true)
        every {
            service.getGoalSummariesByDueYearMonth(
                any(),
                any()
            )
        } returns mockk<GoalsByMonthlyResponseDto>()

        val controller = GoalControllerImpl(service)

        controller.getGoalsByDueMonth(userId = 1L, dueYearMonth = YearMonth.of(2025, 8))
    }

    "진행 중인 목표 조회 요청을 받을 수 있다" {
        val service = mockk<GoalService>(relaxed = true)
        every { service.getGoalsSummariesInProgress(any()) } returns mockk()

        val controller = GoalControllerImpl(service)

        controller.getGoalsSummariesInProgress(userId = 1L)
    }
})

package com.fesi.flowit.schedule.controller

import com.fesi.flowit.schedule.dto.*
import com.fesi.flowit.schedule.service.SchedService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime

class SchedControllerTest : StringSpec({
    "일정 저장 요청을 받을 수 있다" {
        val request = SchedSaveRequestDto(
            userId = 1,
            scheduleInfos = mutableListOf(
                SchedSaveInfo(
                    schedId = null,
                    todoId = 1,
                    startedDateTime = LocalDateTime.of(2025, 8, 4, 9, 0),
                    endedDateTime = LocalDateTime.of(2025, 8, 4, 10, 0),
                    isRemoved = false
                )
            )
        )

        val service = mockk<SchedService>(relaxed = true)
        every { service.saveSchedules(any()) } returns mockk<SchedCreateResponseDto>()

        val controller = SchedControllerImpl(service)

        controller.saveSchedules(request)
    }

    "미할당 할 일 조회 요청을 받을 수 있다" {
        val service = mockk<SchedService>(relaxed = true)
        every { service.getUnassignedTodos(any(), any()) } returns mockk<SchedUnassignedTodosResponseDto>()

        val controller = SchedControllerImpl(service)

        controller.getUnassignedTodos(userId = 1, date = LocalDate.of(2025, 8, 4))
    }

    "할당된 일정 조회 요청을 받을 수 있다" {
        val service = mockk<SchedService>(relaxed = true)
        every { service.getAssignedSched(any(), any()) } returns mockk<SchedAssignedSchedResponseDto>()

        val controller = SchedControllerImpl(service)

        controller.getAssignedSched(userId = 1, date = LocalDate.of(2025, 8, 4))
    }
})

package com.fesi.flowit.timer.controller

import com.fesi.flowit.timer.dto.*
import com.fesi.flowit.timer.service.TodoTimerService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class TodoTimerControllerTest : StringSpec({
    "사용자의 타이머 존재 여부 확인 요청을 받을 수 있다" {
        val service = mockk<TodoTimerService>(relaxed = true)
        every { service.hasUserTodoTimer(any()) } returns mockk<TodoTimerUserInfo>()

        val controller = TodoTimerControllerImpl(service)

        controller.hasUserTodoTimer(userId = 1)
    }

    "할 일의 총 타이머 실행 시간 확인 요청을 받을 수 있다" {
        val service = mockk<TodoTimerService>(relaxed = true)
        every { service.getTotalRunningTimeByTodo(any(), any()) } returns mockk<TodoTimerTotalRunningTime>()

        val controller = TodoTimerControllerImpl(service)

        controller.getTotalRunningTimeByTodo(userId = 1, todoId = 1)
    }

    "할 일 타이머 시작 요청을 받을 수 있다" {
        val request = TodoTimerStartRequestDto(todoId = 1)

        val service = mockk<TodoTimerService>(relaxed = true)
        every { service.startTodoTimer(any(), any()) } returns mockk<TodoTimerStartResponseDto>()

        val controller = TodoTimerControllerImpl(service)

        controller.startTodoTimer(request, userId = 1)
    }

    "할 일 타이머 일시정지 요청을 받을 수 있다" {
        val service = mockk<TodoTimerService>(relaxed = true)
        every { service.pauseTodoTimer(any(), any()) } returns mockk<TodoTimerPauseResponseDto>()

        val controller = TodoTimerControllerImpl(service)

        controller.pauseTodoTimer(todoTimerId = 1, userId = 1)
    }

    "할 일 타이머 다시 시작 요청을 받을 수 있다" {
        val service = mockk<TodoTimerService>(relaxed = true)
        every { service.resumeTodoTimer(any(), any()) } returns mockk<TodoTimerResumeResponseDto>()

        val controller = TodoTimerControllerImpl(service)

        controller.resumeTodoTimer(todoTimerId = 1, userId = 1)
    }

    "할 일 타이머 종료 요청을 받을 수 있다" {
        val service = mockk<TodoTimerService>(relaxed = true)
        every { service.finishTodoTimer(any(), any()) } returns mockk<TodoTimerStopResponseDto>()

        val controller = TodoTimerControllerImpl(service)

        controller.finishTodoTimer(todoTimerId = 1, userId = 1)
    }
})

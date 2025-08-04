package com.fesi.flowit.todo.controller

import com.fesi.flowit.todo.dto.*
import com.fesi.flowit.todo.service.TodoService
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs

class TodoControllerTest : StringSpec({
    "할 일 생성 요청을 받을 수 있다" {
        val request = TodoCreateRequestDto(name = "할 일", goalId = 1)

        val service = mockk<TodoService>(relaxed = true)
        every {
            service.createTodo(
                any(),
                any(),
                any())
        } returns mockk<TodoCreateResponseDto>()
        val controller = TodoControllerImpl(service)

        controller.createTodo(request, userId = 1)
    }

    "할 일 수정 요청을 받을 수 있다" {
        val request = TodoModifyRequestDto(name = "할 일", goalId = 1)

        val service = mockk<TodoService>(relaxed = true)
        every {
            service.modifyTodo(
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk<TodoModifyResponseDto>()
        val controller = TodoControllerImpl(service)

        controller.modifyTodo(todoId = 1, request, userId = 1)
    }

    "할 일 완료 상태 변경 요청을 받을 수 있다" {
        val request = TodoChangeDoneRequestDto(isDone = true)

        val service = mockk<TodoService>(relaxed = true)
        every {
            service.changeDoneStatus(
                any(),
                any(),
                any(),
            )
        } returns mockk<TodoChangeDoneResponseDto>()
        val controller = TodoControllerImpl(service)

        controller.changeDoneStatus(todoId = 1, request, userId = 1)
    }

    "할 일 삭제 요청을 받을 수 있다" {
        val service = mockk<TodoService>(relaxed = true)
        every {
            service.deleteTodoById(
                any(),
                any(),
            )
        } just runs
        val controller = TodoControllerImpl(service)

        controller.deleteTodo(todoId = 1, userId = 1)
    }
})

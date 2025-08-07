package com.fesi.flowit.note.service

import com.fesi.flowit.note.dto.NoteInfoResponseDto
import com.fesi.flowit.note.entity.Note
import com.fesi.flowit.note.repository.NoteRepository
import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.todo.service.TodoService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class NoteServiceTest : StringSpec({

    lateinit var repository: NoteRepository
    lateinit var todoService: TodoService
    lateinit var service: NoteService

    beforeTest() {
        repository = mockk<NoteRepository>()
        todoService = mockk<TodoService>()
        service = NoteService(todoService, repository)
    }

    "노트를 등록할 수 있다" {
        every { todoService.getTodoById(any()) } returns mockk<Todo>()
        every { repository.save(any()) } returns mockk<Note>(relaxed = true)

        service.createNote(
            userId = 1,
            todoId = 1L,
            title = "노트 제목",
            link = "",
            content = "노트 내용"
        ) shouldBe instanceOf<NoteInfoResponseDto>()

        verify { repository.save(any()) }
    }
})

package com.fesi.flowit.note.service

import com.fesi.flowit.note.dto.NoteDetailResponseDto
import com.fesi.flowit.note.dto.NoteInfoResponseDto
import com.fesi.flowit.note.entity.Note
import com.fesi.flowit.note.repository.NoteRepository
import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.todo.service.TodoService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import io.mockk.*
import java.util.*

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

    "노트를 상세 조회할 수 있다" {
        every { todoService.getTodoById(any()) } returns mockk<Todo>(relaxed = true)
        every { repository.findById(any()) } returns Optional.of(mockk<Note>(relaxed = true))

        service.getNoteDetail(todoId = 1L, noteId = 1L) shouldBe instanceOf<NoteDetailResponseDto>()
    }

    "노트를 수정할 수 있다" {
        val note = mockk<Note>(relaxed = true)
        val todo = mockk<Todo>(relaxed = true)
        every { note.todo } returns todo

        every { todoService.getTodoById(any()) } returns todo
        every { repository.findById(any()) } returns Optional.of(note)

        service.modifyNote(
            todoId = 1L,
            noteId = 1L,
            title = "노트 제목",
            link = "",
            content = "노트 내용"
        ) shouldBe instanceOf<NoteInfoResponseDto>()
    }

    "노트를 삭제할 수 있다" {
        val note = mockk<Note>(relaxed = true)
        val todo = mockk<Todo>(relaxed = true)
        every { note.todo } returns todo

        every { todoService.getTodoById(any()) } returns todo
        every { repository.findById(any()) } returns Optional.of(note)
        every { repository.deleteById(any()) } just runs

        service.deleteNote(
            todoId = 1L,
            noteId = 1L
        ) shouldBe instanceOf<Unit>()
    }
})

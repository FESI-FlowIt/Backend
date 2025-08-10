package com.fesi.flowit.note.controller

import com.fesi.flowit.common.response.exceptions.NoteException
import com.fesi.flowit.note.dto.NoteCreateRequestDto
import com.fesi.flowit.note.dto.NoteDetailResponseDto
import com.fesi.flowit.note.dto.NoteInfoResponseDto
import com.fesi.flowit.note.dto.NoteModifyRequestDto
import com.fesi.flowit.note.service.NoteService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs

class NoteControllerTest : StringSpec({

    "노트 생성 요청을 받을 수 있다" {
        val request = NoteCreateRequestDto(
            title = "노트 제목",
            content = "노트 내용",
            wordCount = 100
        )

        val service = mockk<NoteService>(relaxed = true)
        every {
            service.createNote(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk<NoteInfoResponseDto>()

        val controller = NoteControllerImpl(service)

        controller.createNote(todoId = 1L, request, userId = 1)
    }

    "본문 단어수를 확인할 수 있다" {
        listOf(0L, 1001L).forEach { invalidWc ->
            shouldThrow<NoteException> {
                NoteCreateRequestDto(
                    title = "노트 제목",
                    content = "노트 내용",
                    wordCount = invalidWc
                )
            }
        }
    }

    "노트 상세 조회 요청을 받을 수 있다" {
        val service = mockk<NoteService>(relaxed = true)
        every {
            service.getNoteDetail(
                any(),
                any()
            )
        } returns mockk<NoteDetailResponseDto>()

        val controller = NoteControllerImpl(service)

        controller.getNoteDetail(todoId = 1L, noteId = 1L)
    }

    "노트 수정 요청을 받을 수 있다" {
        val service = mockk<NoteService>(relaxed = true)
        every {
            service.modifyNote(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk<NoteInfoResponseDto>()

        val controller = NoteControllerImpl(service)

        controller.modifyNote(
            todoId = 1L, noteId = 1L,
            NoteModifyRequestDto(
                title = "노트 제목",
                content = "노트 내용",
                wordCount = 2
            )
        )
    }

    "노트 삭제 요청을 받을 수 있다" {
        val service = mockk<NoteService>(relaxed = true)
        every {
            service.deleteNote(
                any(),
                any()
            )
        } just runs

        val controller = NoteControllerImpl(service)

        controller.deleteNote(
            todoId = 1L, noteId = 1L
        )
    }
})

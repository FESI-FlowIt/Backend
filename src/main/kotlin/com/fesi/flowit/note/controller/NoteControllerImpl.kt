package com.fesi.flowit.note.controller

import com.fesi.flowit.common.auth.AuthUserId
import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.note.dto.*
import com.fesi.flowit.note.service.NoteService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private val log = loggerFor<NoteControllerImpl>()

@Tag(name = "노트")
@RestController
class NoteControllerImpl(
    private val service: NoteService
) : NoteController {
    @PostMapping("/todos/{todoId}/notes")
    override fun createNote(
        @PathVariable("todoId") todoId: Long,
        @RequestBody request: NoteCreateRequestDto,
        @AuthUserId userId: Long
    ): ResponseEntity<ApiResult<NoteInfoResponseDto>> {
        log.debug(">> request createNote(${request})")

        return ApiResponse.created(
            service.createNote(
                userId = userId,
                todoId = todoId,
                title = request.title,
                link = request.link!!,
                content = request.content
            )
        )
    }

    @GetMapping("/todos/{todoId}/notes/{noteId}")
    override fun getNoteDetail(
        @PathVariable("todoId") todoId: Long,
        @PathVariable("noteId") noteId: Long
    ): ResponseEntity<ApiResult<NoteDetailResponseDto>> {
        log.debug(">> request getNoteDetail(${todoId}, ${noteId})")

        return ApiResponse.ok(
            service.getNoteDetail(
                todoId,
                noteId
            )
        )
    }

    @PatchMapping("/todos/{todoId}/notes/{noteId}")
    override fun modifyNote(
        @PathVariable("todoId") todoId: Long,
        @PathVariable("noteId") noteId: Long,
        @RequestBody request: NoteModifyRequestDto
    ): ResponseEntity<ApiResult<NoteInfoResponseDto?>>  {
        log.debug(">> request modifyNote(${todoId}, ${noteId})")

        return ApiResponse.ok(
            service.modifyNote(
                todoId = todoId,
                noteId = noteId,
                title = request.title,
                link = request.link!!,
                content = request.content
            )
        )
    }

    @DeleteMapping("/todos/{todoId}/notes/{noteId}")
    override fun deleteNote(
        @PathVariable("todoId") todoId: Long,
        @PathVariable("noteId") noteId: Long
    ): ResponseEntity<ApiResult<Unit>> {
        log.debug(">> request deleteNote(${todoId}, ${noteId})")

        service.deleteNote(todoId, noteId)

        return ApiResponse.noContent()
    }

    @GetMapping("/todos/{todoId}/notes")
    override fun getAllNotes(@PathVariable("todoId") todoId: Long): ResponseEntity<ApiResult<List<NoteFindAllResponseDto>?>> {
        log.debug(">> request getAllNotes(${todoId})")

        return ApiResponse.ok(service.getAllNotes(todoId))
    }
}

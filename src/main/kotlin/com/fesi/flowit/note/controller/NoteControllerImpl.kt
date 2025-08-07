package com.fesi.flowit.note.controller

import com.fesi.flowit.common.auth.AuthUserId
import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.note.dto.NoteCreateRequestDto
import com.fesi.flowit.note.dto.NoteInfoResponseDto
import com.fesi.flowit.note.service.NoteService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

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
}

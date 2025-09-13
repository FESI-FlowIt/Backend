package com.fesi.flowit.note.dto

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.NoteException
import com.fesi.flowit.note.entity.Note
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class NoteFindAllResponseDto(

    @field:Schema(
        description = "노트 ID",
        example = "1",
    )
    var noteId: Long,

    @field:Schema(
        description = "할 일 ID",
        example = "1",
    )
    var todoId: Long,

    @field:Schema(
        description = "노트 제목",
        example = "노트 제목",
        minLength = 1,
        maxLength = 30,
    )
    var title: String,

    @field:Schema(
        description = "수정 시간",
        example = "2025-07-19T14:29:00 | 2025-07-19",
    )
    val modifiedDateTime: LocalDateTime
) {
    companion object {
        fun fromNoteWithTodoId(note: Note, todoId: Long): NoteFindAllResponseDto {
            return NoteFindAllResponseDto(
                noteId = note.id ?: throw NoteException.fromCodeWithMsg(
                    ApiResultCode.NOTE_INVALID_ID,
                    "Failed to create note."
                ),
                todoId = todoId,
                title = note.title,
                modifiedDateTime = note.modifiedDateTime
            )
        }
    }
}

package com.fesi.flowit.note.dto

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.NoteException
import com.fesi.flowit.note.entity.Note
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class NoteInfoResponseDto(
    @field:Schema(
        description = "노트 ID",
        example = "1",
    )
    var noteId: Long,

    @field:Schema(
        description = "노트 제목",
        example = "노트 제목",
        minLength = 1,
        maxLength = 30,
    )
    var title: String,

    @field:Schema(
        description = "외부 자료 링크",
        example = "google.com"
    )
    var link: String,

    @field:Schema(
        description = "노트 내용"
    )
    val content: String,

    @field:Schema(
        description = "생성 시간",
        example = "2025-07-19T14:29:00 | 2025-07-19",
    )
    val createdDateTime: LocalDateTime,

    @field:Schema(
        description = "수정 시간",
        example = "2025-07-19T14:29:00 | 2025-07-19",
    )
    val modifiedDateTime: LocalDateTime,
) {
    companion object {
        fun fromNote(note: Note): NoteInfoResponseDto {
            return NoteInfoResponseDto(
                noteId = note.id ?: throw NoteException.fromCodeWithMsg(
                    ApiResultCode.NOTE_INVALID_ID,
                    "Failed to create note."
                ),
                title = note.title,
                link = note.link,
                content = note.content,
                createdDateTime = note.createdDateTime,
                modifiedDateTime = note.modifiedDateTime
            )
        }
    }
}

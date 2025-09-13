package com.fesi.flowit.note.dto

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.NoteException
import io.swagger.v3.oas.annotations.media.Schema

data class NoteCreateRequestDto(
    @field:Schema(
        description = "노트 제목",
        example = "노트 제목",
        minLength = 1,
        maxLength = 30,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val title: String,

    @field:Schema(
        description = "링크",
        example = "링크 url",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    val link: String? = "",

    @field:Schema(
        description = "노트 본문",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val content: String,

    @field:Schema(
        description = "본문 단어 수",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val wordCount: Long
) {
    init {
        validateTitle()
        validateWordCount()
    }

    private fun validateTitle() {
        if (title.isBlank()) {
            throw NoteException.fromCodeWithMsg(
                ApiResultCode.BAD_REQUEST,
                "Note title is required."
            )
        }

        if (title.length !in 1..30) {
            throw NoteException.fromCode(ApiResultCode.NOTE_INVALID_TITLE_LENGTH)
        }
    }

    private fun validateWordCount() {
        if (wordCount !in 1..1000) {
            throw NoteException.fromCode(ApiResultCode.NOTE_INVALID_CONTENT_LENGTH)
        }
    }
}

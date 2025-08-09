package com.fesi.flowit.note.dto

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.NoteException
import com.fesi.flowit.goal.entity.Goal
import com.fesi.flowit.note.entity.Note
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class NoteDetailResponseDto(
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
        description = "수정 시간",
        example = "2025-07-19T14:29:00 | 2025-07-19",
    )
    val modifiedDateTime: LocalDateTime,

    @field:Schema(
        description = "할 일 ID"
    )
    val todoId: Long,

    @field:Schema(
        description = "목표 ID"
    )
    var goalId: Long
) {
    companion object {
        fun fromNoteWithGoal(note: Note, goal: Goal): NoteDetailResponseDto {
            val todo = note.todo ?: throw NoteException.fromCode(
                ApiResultCode.NOTE_TODO_NOT_FOUND
            )
            return NoteDetailResponseDto(
                noteId = note.id ?: throw NoteException.fromCodeWithMsg(
                    ApiResultCode.NOTE_INVALID_ID,
                    "Failed to create note."
                ),
                title = note.title,
                link = note.link,
                content = note.content,
                modifiedDateTime = note.modifiedDateTime,
                todoId = todo.id!!,
                goalId = goal.id!!
            )
        }
    }
}

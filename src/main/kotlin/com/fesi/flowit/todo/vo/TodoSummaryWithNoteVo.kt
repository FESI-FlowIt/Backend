package com.fesi.flowit.todo.vo

import com.fesi.flowit.note.vo.NoteInfoVo
import io.swagger.v3.oas.annotations.media.Schema

data class TodoSummaryWithNoteVo(
    @field:Schema(
        description = "할 일 ID",
        example = "1",
    )
    val todoId: Long,

    @field:Schema(
        description = "할 일 이름",
        example = "할 일 이름",
        minLength = 1,
        maxLength = 30,
    )
    val name: String,

    @field:Schema(
        description = "완료 여부",
        example = "true | false",
    )
    val isDone: Boolean,

    @field:Schema(
        description = "노트 정보"
    )
    val note: List<NoteInfoVo>
)

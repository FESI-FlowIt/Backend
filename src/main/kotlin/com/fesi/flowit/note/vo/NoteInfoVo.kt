package com.fesi.flowit.note.vo

import io.swagger.v3.oas.annotations.media.Schema

data class NoteInfoVo(
    @field:Schema(
        description = "노트 ID",
        example = "1",
    )
    val id: Long,

    @field:Schema(
        description = "노트 제목",
        example = "노트 제목",
    )
    val title: String,

    @field:Schema(
        description = "노트 링크",
        example = "https://example.com",
    )
    val link: String,

    @field:Schema(
        description = "노트 내용",
        example = "노트 내용",
    )
    val content: String
)
package com.fesi.flowit.todo.vo

import io.swagger.v3.oas.annotations.media.Schema

data class TodoFileMaterial(
    @field:Schema(
        description = "파일 이름",
        example = "example.png",
    )
    val name: String,

    @field:Schema(
        description = "파일 링크",
        example = "https://www.example.com",
    )
    val url: String,
) {
    companion object {
        fun of(name: String, url: String): TodoFileMaterial {
            return TodoFileMaterial(name, url)
        }
    }
}

data class TodoLinkMaterial(
    @field:Schema(
        description = "파일 링크",
        example = "https://www.example.com",
    )
    val url: String
) {
    companion object {
        fun of(url: String): TodoLinkMaterial {
            return TodoLinkMaterial(url)
        }
    }
}

data class TodoNoteMaterial(
    @field:Schema(
        description = "노트 제목",
        example = "할 일 1의 노트입니다.",
    )
    val title: String,

    @field:Schema(
        description = "노트 ID",
        example = "1",
    )
    val noteId: Long
) {
    companion object {
        fun of(title: String, noteId: Long): TodoNoteMaterial {
            return TodoNoteMaterial(title, noteId)
        }
    }
}
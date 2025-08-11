package com.fesi.flowit.todo.vo

import io.swagger.v3.oas.annotations.media.Schema

data class TodoSummaryInGoalDetailVo(
    @field:Schema(
        description = "할 일 이름",
        example = "할 일1",
    )
    val name: String,

    @field:Schema(
        description = "완료 여부",
        example = "true | false",
    )
    val isDone: Boolean,

    @field:Schema(description = "파일 목록")
    val files: MutableList<TodoFileMaterial>,

    @field:Schema(description = "링크 목록")
    val links: MutableList<TodoLinkMaterial>,

    @field:Schema(description = "노트 목록")
    val notes: MutableList<TodoNoteMaterial>

) {
    companion object {
        fun of(
            name: String,
            isDone: Boolean,
            files: MutableList<TodoFileMaterial>,
            links: MutableList<TodoLinkMaterial>,
            notes: MutableList<TodoNoteMaterial>
        ): TodoSummaryInGoalDetailVo {
            return TodoSummaryInGoalDetailVo(name, isDone, files, links, notes)
        }
    }
}
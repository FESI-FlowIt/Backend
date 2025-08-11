package com.fesi.flowit.todo.dto

import io.swagger.v3.oas.annotations.media.Schema

data class TodoMaterialLinkDto(
    @field:Schema(
        description = "할 일 링크",
        example = "https://www.example.com",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val link: String
) {
    companion object {
        fun of(link: String): TodoMaterialLinkDto {
            return TodoMaterialLinkDto(link)
        }
    }
}
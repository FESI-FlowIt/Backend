package com.fesi.flowit.todo.dto

import io.swagger.v3.oas.annotations.media.Schema

data class TodoFileResponseDto(
    @field:Schema(
        description = "할 일 ID",
        example = "1",
    )
    val todoId: Long,

    @field:Schema(
        description = "파일 링크",
        example = "https://BUCKET_EXAMPLE.s3.REGION_EXAMPLE.amazonaws.com/",
    )
    val url: String,

    @field:Schema(
        description = "파일 이름",
        example = "FileNameInYourLocal.png",
    )
    val fileName: String
) {
    companion object {
        fun of(todoId: Long, url: String, fileName: String): TodoFileResponseDto {
            return TodoFileResponseDto(todoId, url, fileName)
        }
    }
}
package com.fesi.flowit.note.controller

import com.fesi.flowit.common.auth.AuthUserId
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.note.dto.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

interface NoteController {

    @Operation(
        summary = "노트 생성",
        description = "노트를 생성합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "노트 생성 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = NoteInfoResponseDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "올바르지 않은 요청 혹은 유효하지 않은 파라미터 값",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiResult.Exception::class)
                )]
            )
        ]
    )
    fun createNote(
        @PathVariable("todoId") todoId: Long,
        @RequestBody request: NoteCreateRequestDto,
        @Parameter(hidden = true) @AuthUserId userId: Long
    ): ResponseEntity<ApiResult<NoteInfoResponseDto>>

    @Operation(
        summary = "노트 상세 조회",
        description = "노트 상세 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "노트 상세 조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = NoteDetailResponseDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "올바르지 않은 요청 혹은 유효하지 않은 파라미터 값",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiResult.Exception::class)
                )]
            )
        ]
    )
    fun getNoteDetail(
        @PathVariable("todoId") todoId: Long,
        @PathVariable("noteId") noteId: Long
    ): ResponseEntity<ApiResult<NoteDetailResponseDto>>

    @Operation(
        summary = "노트 수정",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "노트 수정 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = NoteInfoResponseDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "올바르지 않은 요청 혹은 유효하지 않은 파라미터 값",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiResult.Exception::class)
                )]
            )
        ]
    )
    fun modifyNote(
        @PathVariable("todoId") todoId: Long,
        @PathVariable("noteId") noteId: Long,
        @RequestBody request: NoteModifyRequestDto
    ): ResponseEntity<ApiResult<NoteInfoResponseDto?>>

    @Operation(
        summary = "노트 삭제",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "노트 삭제 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = Any::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "올바르지 않은 요청 혹은 유효하지 않은 파라미터 값",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiResult.Exception::class)
                )]
            )
        ]
    )
    fun deleteNote(
        @PathVariable("todoId") todoId: Long,
        @PathVariable("todoId") noteId: Long
    ): ResponseEntity<ApiResult<Unit>>

    @Operation(
        summary = "노트 목록 가져오기",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "노트 목록 가져오기 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = NoteFindAllResponseDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "올바르지 않은 요청 혹은 유효하지 않은 파라미터 값",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ApiResult.Exception::class)
                )]
            )
        ]
    )
    fun getAllNotes(@PathVariable("todoId") todoId: Long): ResponseEntity<ApiResult<List<NoteFindAllResponseDto>?>>
}

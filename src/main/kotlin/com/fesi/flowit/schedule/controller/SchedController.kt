package com.fesi.flowit.schedule.controller

import com.fesi.flowit.common.auth.AuthUserId
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.schedule.dto.SchedAssignedSchedResponseDto
import com.fesi.flowit.schedule.dto.SchedSaveRequestDto
import com.fesi.flowit.schedule.dto.SchedCreateResponseDto
import com.fesi.flowit.schedule.dto.SchedUnassignedTodosResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

interface SchedController {
    @Operation(
        summary = "일정 저장",
        description = """
            일정을 저장합니다.
           
            - Request에 schedId가 없으면 새로운 일정을 생성합니다.
            - Request에 schedId가 있으면 일정 시작-종료 시간을 업데이트합니다.
            - Request에 isRemoved가 true이면 일정을 삭제합니다.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "일정 저장 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = SchedCreateResponseDto::class)
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
    fun saveSchedules(
        @RequestBody request: SchedSaveRequestDto,
        @AuthUserId userId: Long
    ): ResponseEntity<ApiResult<SchedCreateResponseDto>>

    @Operation(
        summary = "미배치 할 일 조회",
        description =
        """
            미배치 할 일을 조회힙니다. 
            해당 할 일을 포함하는 목표의 마감일이 지나지 않은 모든 할 일을 반환합니다.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = SchedUnassignedTodosResponseDto::class)
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
    fun getUnassignedTodos(@AuthUserId userId: Long,

                           @RequestParam(name = "date", required = true)
                           @DateTimeFormat(pattern = "yyyy-MM-dd")
                           date: LocalDate
    ): ResponseEntity<ApiResult<SchedUnassignedTodosResponseDto>>

    @Operation(
        summary = "배치된 일정 조회",
        description = "일정 시작 날을 기준으로 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = SchedAssignedSchedResponseDto::class)
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
    fun getAssignedSched(@AuthUserId userId: Long,

                         @RequestParam(name = "date", required = true)
                         @DateTimeFormat(pattern = "yyyy-MM-dd")
                         date: LocalDate
    ): ResponseEntity<ApiResult<SchedAssignedSchedResponseDto>>
}
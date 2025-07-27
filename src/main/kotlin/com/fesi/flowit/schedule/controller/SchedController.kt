package com.fesi.flowit.schedule.controller

import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.schedule.dto.SchedCreateRequestDto
import com.fesi.flowit.schedule.dto.SchedCreateResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

interface SchedController {
    fun createSchedules(@RequestBody request: SchedCreateRequestDto): ResponseEntity<ApiResult<SchedCreateResponseDto>>
}
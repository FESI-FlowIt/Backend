package com.fesi.flowit.schedule.controller

import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.schedule.dto.SchedCreateRequestDto
import com.fesi.flowit.schedule.dto.SchedCreateResponseDto
import com.fesi.flowit.schedule.service.SchedService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

private val log = loggerFor<SchedControllerImpl>()

@RestController
class SchedControllerImpl(
    private val schedService: SchedService
) : SchedController {

    @PostMapping("/schedules")
    override fun createSchedules(@RequestBody request: SchedCreateRequestDto): ResponseEntity<ApiResult<SchedCreateResponseDto>> {
        log.debug(">> request createSchedules(${request})")

        return ApiResponse.created(schedService.createSchedules(request))
    }
}
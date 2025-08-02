package com.fesi.flowit.schedule.controller

import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResponse
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.schedule.dto.SchedCreateRequestDto
import com.fesi.flowit.schedule.dto.SchedCreateResponseDto
import com.fesi.flowit.schedule.dto.SchedUnassignedTodosResponseDto
import com.fesi.flowit.schedule.service.SchedService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

private val log = loggerFor<SchedControllerImpl>()

@RestController
@Tag(name = "일정")
class SchedControllerImpl(
    private val schedService: SchedService
) : SchedController {

    @PostMapping("/schedules")
    override fun createSchedules(@RequestBody request: SchedCreateRequestDto): ResponseEntity<ApiResult<SchedCreateResponseDto>> {
        log.debug(">> request createSchedules(${request})")

        return ApiResponse.created(schedService.createSchedules(request))
    }

    @GetMapping("/schedules/unassigned")
    override fun getUnassignedTodo(@RequestParam("userId") userId: Long,

                          @RequestParam(name = "date", required = true)
                          @DateTimeFormat(pattern = "yyyy-MM-dd")
                          date: LocalDate
    ): ResponseEntity<ApiResult<SchedUnassignedTodosResponseDto>> {
        log.debug(">> request getUnassignedTodo(userId=${userId}, date: ${date})")

        return ApiResponse.ok(schedService.getUnassignedTodo(userId, date))
    }
}
package com.fesi.flowit.schedule.service

import com.fesi.flowit.schedule.dto.SchedCreateRequestDto
import com.fesi.flowit.schedule.dto.SchedCreateResponseDto

interface SchedService {
    fun createSchedules(request: SchedCreateRequestDto): SchedCreateResponseDto
}
package com.fesi.flowit.schedule.service

import com.fesi.flowit.schedule.dto.SchedCreateRequestDto
import com.fesi.flowit.schedule.dto.SchedCreateResponseDto
import com.fesi.flowit.schedule.dto.SchedUnassignedTodosResponseDto
import java.time.LocalDate

interface SchedService {
    fun createSchedules(request: SchedCreateRequestDto): SchedCreateResponseDto
    fun getUnassignedTodo(userId: Long, date: LocalDate): SchedUnassignedTodosResponseDto
}
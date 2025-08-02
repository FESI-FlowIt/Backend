package com.fesi.flowit.schedule.service

import com.fesi.flowit.schedule.dto.SchedAssignedSchedResponseDto
import com.fesi.flowit.schedule.dto.SchedCreateRequestDto
import com.fesi.flowit.schedule.dto.SchedCreateResponseDto
import com.fesi.flowit.schedule.dto.SchedUnassignedTodosResponseDto
import java.time.LocalDate

interface SchedService {
    fun createSchedules(request: SchedCreateRequestDto): SchedCreateResponseDto
    fun getUnassignedTodos(userId: Long, date: LocalDate): SchedUnassignedTodosResponseDto
    fun getAssignedSched(userId: Long, date: LocalDate): SchedAssignedSchedResponseDto
}
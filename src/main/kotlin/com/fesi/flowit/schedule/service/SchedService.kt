package com.fesi.flowit.schedule.service

import com.fesi.flowit.schedule.dto.SchedAssignedSchedResponseDto
import com.fesi.flowit.schedule.dto.SchedSaveRequestDto
import com.fesi.flowit.schedule.dto.SchedCreateResponseDto
import com.fesi.flowit.schedule.dto.SchedUnassignedTodosResponseDto
import java.time.LocalDate

interface SchedService {
    fun saveSchedules(request: SchedSaveRequestDto): SchedCreateResponseDto
    fun getUnassignedTodos(userId: Long, date: LocalDate): SchedUnassignedTodosResponseDto
    fun getAssignedSched(userId: Long, date: LocalDate): SchedAssignedSchedResponseDto
}
package com.fesi.flowit.schedule.repository

import com.fesi.flowit.schedule.entity.Schedule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ScheduleRepository : JpaRepository<Schedule, Long>
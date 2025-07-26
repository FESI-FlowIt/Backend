package com.fesi.flowit.goal.repository

import com.fesi.flowit.goal.entity.Goal
import org.springframework.data.jpa.repository.JpaRepository

interface GoalRepository : JpaRepository<Goal, Long>
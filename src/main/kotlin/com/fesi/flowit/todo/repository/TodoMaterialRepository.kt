package com.fesi.flowit.todo.repository

import com.fesi.flowit.todo.entity.TodoMaterial
import org.springframework.data.jpa.repository.JpaRepository

interface TodoMaterialRepository : JpaRepository<TodoMaterial, Long>
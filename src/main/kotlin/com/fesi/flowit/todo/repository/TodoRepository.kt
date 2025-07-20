package com.fesi.flowit.todo.repository

import com.fesi.flowit.todo.entity.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<Todo, Long>
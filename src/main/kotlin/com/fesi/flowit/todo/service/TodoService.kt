package com.fesi.flowit.todo.service

import com.fesi.flowit.todo.dto.TodoCreateResponseDto

interface TodoService {
    fun createTodo(name: String, goalId: Long): TodoCreateResponseDto
}
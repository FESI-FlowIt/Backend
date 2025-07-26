package com.fesi.flowit.todo.service

import com.fesi.flowit.todo.dto.TodoCreateResponseDto
import com.fesi.flowit.todo.dto.TodoModifyResponseDto

interface TodoService {
    fun createTodo(userId: Long, name: String, goalId: Long): TodoCreateResponseDto
    fun modifyTodo(todoId: Long, userId: Long, name: String, goalId: Long): TodoModifyResponseDto
    fun deleteTodoById(userId: Long, todoId: Long)
}
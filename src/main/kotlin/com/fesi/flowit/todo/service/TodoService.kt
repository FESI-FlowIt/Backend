package com.fesi.flowit.todo.service

import com.fesi.flowit.todo.dto.TodoChangeDoneResponseDto
import com.fesi.flowit.todo.dto.TodoCreateResponseDto
import com.fesi.flowit.todo.dto.TodoFileResponseDto
import com.fesi.flowit.todo.dto.TodoModifyResponseDto
import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.todo.vo.TodoSummaryWithDateVo
import com.fesi.flowit.user.entity.User
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

interface TodoService {
    fun createTodo(userId: Long, name: String, goalId: Long): TodoCreateResponseDto
    fun modifyTodo(todoId: Long, userId: Long, name: String, goalId: Long): TodoModifyResponseDto
    fun changeDoneStatus(todoId: Long, userId: Long, isDone: Boolean): TodoChangeDoneResponseDto
    fun deleteTodoById(userId: Long, todoId: Long)
    fun getTodoById(todoId: Long): Todo
    fun getTodosByIds(todoIds: List<Long>): List<Todo>
    fun getTodoSummariesWithDateFromDueDate(user: User, date: LocalDate): MutableList<TodoSummaryWithDateVo>
    fun getTodoSummariesWithDateFromDueDate(userId: Long, date: LocalDate): MutableList<TodoSummaryWithDateVo>
    fun uploadTodoFile(userId: Long, todoId: Long, file: MultipartFile): TodoFileResponseDto
}
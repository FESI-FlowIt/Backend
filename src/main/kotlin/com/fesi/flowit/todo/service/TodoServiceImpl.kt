package com.fesi.flowit.todo.service

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.TodoException
import com.fesi.flowit.goal.service.GoalService
import com.fesi.flowit.todo.dto.TodoCreateResponseDto
import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.todo.repository.TodoRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TodoServiceImpl(
    private val goalService: GoalService,
    private val todoRepository: TodoRepository
) : TodoService {

    /**
     * 할 일 생성
     */
    @Transactional
    override fun createTodo(name: String, goalId: Long): TodoCreateResponseDto {
        val createdDateTime = LocalDateTime.now()
        val goal = goalService.getGoalById(goalId) ?: throw TodoException.fromCode(ApiResultCode.TODO_NOT_FOUND)

        val todo = todoRepository.save(Todo.withGoal(
            name = name,
            isDone = false,
            createdDateTime = createdDateTime,
            modifiedDateTime = createdDateTime,
            goal = goal
        ))

        return TodoCreateResponseDto.fromTodo(todo)
    }
}
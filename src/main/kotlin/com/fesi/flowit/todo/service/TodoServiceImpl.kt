package com.fesi.flowit.todo.service

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.TodoException
import com.fesi.flowit.goal.service.GoalService
import com.fesi.flowit.todo.dto.TodoCreateResponseDto
import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.todo.repository.TodoRepository
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TodoServiceImpl(
    private val userService: UserService,
    private val goalService: GoalService,
    private val todoRepository: TodoRepository
) : TodoService {

    /**
     * 할 일 생성
     */
    @Transactional
    override fun createTodo(userId: Long, name: String, goalId: Long): TodoCreateResponseDto {
        val user: User = userService.findUserById(userId)

        val createdDateTime = LocalDateTime.now()
        val goal = goalService.getGoalById(goalId)

        val todo = todoRepository.save(Todo.withGoal(
            user = user,
            name = name,
            isDone = false,
            createdDateTime = createdDateTime,
            modifiedDateTime = createdDateTime,
            goal = goal
        ))

        return TodoCreateResponseDto.fromTodo(todo)
    }
}
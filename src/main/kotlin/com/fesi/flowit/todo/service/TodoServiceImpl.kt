package com.fesi.flowit.todo.service

import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.TodoException
import com.fesi.flowit.goal.service.GoalService
import com.fesi.flowit.todo.dto.TodoCreateResponseDto
import com.fesi.flowit.todo.dto.TodoModifyResponseDto
import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.todo.repository.TodoRepository
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

private val log = loggerFor<TodoServiceImpl>()

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

    /**
     * 할 일 수정
     */
    @Transactional
    override fun modifyTodo(todoId: Long, userId: Long, name: String, goalId: Long): TodoModifyResponseDto {
        val user: User = userService.findUserById(userId)
        val todo: Todo = getTodoById(todoId)

        if (doesNotUserOwnTodo(user, todo)) {
            throw TodoException.fromCode(ApiResultCode.TODO_NOT_MATCH_USER)
        }

        val targetGoal = goalService.getGoalById(goalId)
        if (goalService.doesNotUserOwnGoal(user, targetGoal)) {
            throw TodoException.fromCode(ApiResultCode.GOAL_NOT_MATCH_USER)
        }

        log.debug("""
            modifyTodo(todoId=${todoId}, userId=${userId})..
            name: ${todo.name} -> ${name},
            goalId: ${todo.goal?.id} -> ${goalId}
        """.trimIndent())

        todo.name = name
        todo.goal = targetGoal
        todo.modifiedDateTime = LocalDateTime.now()

        return TodoModifyResponseDto.fromTodo(todo)
    }

    /**
     * 할 일 삭제
     */
    @Transactional
    override fun deleteTodoById(userId: Long, todoId: Long) {
        val user: User = userService.findUserById(userId)
        val todo: Todo = getTodoById(todoId)

        if (doesNotUserOwnTodo(user, todo)) {
            throw TodoException.fromCode(ApiResultCode.TODO_NOT_MATCH_USER)
        }

        todoRepository.deleteById(todoId)
        log.debug("Deleted todo(id=${todo.id}, name=${todo.name}, isDone=${todo.isDone}")
    }

    private fun doesNotUserOwnTodo(user: User, todo: Todo): Boolean {
        return todo.user != user
    }

    private fun getTodoById(todoId: Long): Todo {
        return todoRepository.findById(todoId).orElseThrow { TodoException.fromCode(ApiResultCode.TODO_NOT_FOUND) }
    }
}
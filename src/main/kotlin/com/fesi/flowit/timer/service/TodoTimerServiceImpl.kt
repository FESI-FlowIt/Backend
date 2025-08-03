package com.fesi.flowit.timer.service

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.TodoTimerException
import com.fesi.flowit.timer.dto.TodoTimerTotalRunningTime
import com.fesi.flowit.timer.dto.TodoTimerUserInfo
import com.fesi.flowit.timer.repository.TodoTimerRepository
import com.fesi.flowit.timer.vo.TodoTimerTotalTimeVo
import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.todo.service.TodoService
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.service.UserService
import org.springframework.stereotype.Service

@Service
class TodoTimerServiceImpl(
    private val userService: UserService,
    private val todoService: TodoService,
    private val todoTimerRepository: TodoTimerRepository
) : TodoTimerService {

    override fun hasUserTodoTimer(userId: Long): TodoTimerUserInfo {
        val user: User = userService.findUserById(userId)

        return todoTimerRepository.hasUserTodoTimer(user) ?: TodoTimerUserInfo.createIfNotExist(userId)
    }

    override fun getTotalRunningTimeByTodo(userId: Long, todoId: Long): TodoTimerTotalRunningTime {
        val user: User = userService.findUserById(userId)
        val todo: Todo = todoService.getTodoById(todoId)

        if (todo.doesNotUserOwnTodo(user)) {
            throw TodoTimerException.fromCode(ApiResultCode.TODO_NOT_MATCH_USER)
        }

        val totalTimeVo: TodoTimerTotalTimeVo? = todoTimerRepository.calculateTotalRunningTime(todo)
        return TodoTimerTotalRunningTime.fromTotalTimeVo(
            todoId, totalTimeVo ?: TodoTimerTotalTimeVo.emptyRecord(todoId)
        )
    }
}
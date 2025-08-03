package com.fesi.flowit.timer.service

import com.fesi.flowit.timer.dto.TodoTimerUserInfo
import com.fesi.flowit.timer.repository.TodoTimerRepository
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.service.UserService
import org.springframework.stereotype.Service

@Service
class TodoTimerServiceImpl(
    private val userService: UserService,
    private val todoTimerRepository: TodoTimerRepository
) : TodoTimerService {

    override fun hasUserTodoTimer(userId: Long): TodoTimerUserInfo {
        val user: User = userService.findUserById(userId)

        return todoTimerRepository.hasUserTodoTimer(user) ?: TodoTimerUserInfo.createIfNotExist(userId)
    }
}
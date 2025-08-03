package com.fesi.flowit.timer.service

import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.TodoTimerException
import com.fesi.flowit.timer.dto.TodoTimerPauseResponseDto
import com.fesi.flowit.timer.dto.TodoTimerStartResponseDto
import com.fesi.flowit.timer.dto.TodoTimerTotalRunningTime
import com.fesi.flowit.timer.dto.TodoTimerUserInfo
import com.fesi.flowit.timer.entity.TodoTimer
import com.fesi.flowit.timer.entity.TodoTimerPauseHistory
import com.fesi.flowit.timer.repository.TodoTimerPausedHistoryRepository
import com.fesi.flowit.timer.repository.TodoTimerRepository
import com.fesi.flowit.timer.vo.TodoTimerTotalTimeVo
import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.todo.service.TodoService
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

private val log = loggerFor<TodoTimerServiceImpl>()


@Service
class TodoTimerServiceImpl(
    private val userService: UserService,
    private val todoService: TodoService,
    private val todoTimerRepository: TodoTimerRepository,
    private val todoTimerPausedHistoryRepository: TodoTimerPausedHistoryRepository
) : TodoTimerService {

    /**
     * 회원이 할 일 타이머 갖고 있는지 확인
     */
    override fun hasUserTodoTimer(userId: Long): TodoTimerUserInfo {
        val user: User = userService.findUserById(userId)
        return todoTimerRepository.hasUserTodoTimer(user) ?: TodoTimerUserInfo.createIfNotExist(userId)
    }

    /**
     * 할 일 별 누적 작업 시간 조회
     */
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

    /**
     * 할 일 타이머 시작
     */
    @Transactional
    override fun startTodoTimer(userId: Long, todoId: Long): TodoTimerStartResponseDto {
        val user: User = userService.findUserById(userId)
        val todo: Todo = todoService.getTodoById(todoId)

        if (hasUserTodoTimer(userId).isRunningTimer) {
            throw TodoTimerException.fromCode(ApiResultCode.TIMER_TODO_ALREADY_RUNNING)
        }

        if (todo.doesNotUserOwnTodo(user)) {
            throw TodoTimerException.fromCode(ApiResultCode.TODO_NOT_MATCH_USER)
        }


        val startedTimerDateTime = LocalDateTime.now()
        val todoTimer = todoTimerRepository.save(TodoTimer.startTimer(user, todo, startedTimerDateTime))
        todoTimer.setUser(user)
        todoTimer.setTodo(todo)

        log.debug("TodoTimer Started.. todoTimerId=${todoTimer.id}, userId=${todoTimer.user.id}, todoId: ${todo.id}, started: ${todoTimer.startedDateTime}")

        return TodoTimerStartResponseDto.of(
            todoTimer.id ?: throw TodoTimerException.fromCode(ApiResultCode.TIMER_TODO_INVALID_ID),
            todoTimer.todo.id ?: throw TodoTimerException.fromCode(ApiResultCode.TODO_INVALID_ID),
            todoTimer.startedDateTime
        )
    }

    /**
     * 할 일 타이머 일시 정지
     */
    @Transactional
    override fun pauseTodoTimer(userId: Long, todoTimerId: Long): TodoTimerPauseResponseDto {
        val user: User = userService.findUserById(userId)
        val todoTimer: TodoTimer = todoTimerRepository.findById(todoTimerId)
            .orElseThrow { throw TodoTimerException.fromCode(ApiResultCode.TIMER_TODO_NOT_FOUND) }

        if (todoTimer.doesNotUserOwnTodoTimer(user)) {
            throw TodoTimerException.fromCode(ApiResultCode.TIMER_TODO_NOT_MATCH_USER)
        }

        if (!isRunningTimer(todoTimer)) {
            throw TodoTimerException.fromCode(ApiResultCode.TIMER_TODO_NOT_RUNNING)
        }

        // 중지 중인 타이머 확인
        val existNotEndedPausedTimer = todoTimerPausedHistoryRepository.existPausedTimerNotEndedByTimer(todoTimer)
        if (existNotEndedPausedTimer) {
            log.warn("Todo Timer is already paused..  userId=${userId}, todoTimerId=${todoTimerId}, todoTimerStatus=${todoTimer.status}")
            throw TodoTimerException.fromCode(ApiResultCode.TIMER_TODO_NOT_ENDED_PAUSED)
        }

        val pausedDateTime = LocalDateTime.now()

        val todoTimerPausedHistory = todoTimerPausedHistoryRepository.save(
            TodoTimerPauseHistory.createPauseTimerHistory(todoTimer, pausedDateTime)
        )

        todoTimer.pauseTimer()

        log.debug("Todo Timer(id=${todoTimerPausedHistory.id}) is paused.")

        return TodoTimerPauseResponseDto.of(
            todoTimerPausedHistory.id ?: throw TodoTimerException.fromCode(ApiResultCode.TIMER_TODO_PAUSED_ID_INVALID),
            todoTimerPausedHistory.timer.todo.id ?: throw TodoTimerException.fromCode(ApiResultCode.TIMER_TODO_INVALID_ID),
            todoTimerPausedHistory.pauseStartedDateTime
        )
    }

    private fun isRunningTimer(todoTimer: TodoTimer): Boolean {
        return todoTimer.status.isRunning()
    }
}
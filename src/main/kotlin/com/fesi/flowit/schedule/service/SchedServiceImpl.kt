package com.fesi.flowit.schedule.service

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.ScheduleException
import com.fesi.flowit.common.response.exceptions.TodoException
import com.fesi.flowit.schedule.dto.SchedCreateRequestDto
import com.fesi.flowit.schedule.dto.SchedCreateResponseDto
import com.fesi.flowit.schedule.dto.SchedUnassignedTodosResponseDto
import com.fesi.flowit.schedule.entity.Schedule
import com.fesi.flowit.schedule.repository.ScheduleRepository
import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.todo.service.TodoService
import com.fesi.flowit.todo.vo.TodoSummaryWithDateVo
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class SchedServiceImpl(
    private val userService: UserService,
    private val todoService: TodoService,
    private val scheduleRepository: ScheduleRepository
) : SchedService {

    @Transactional
    override fun createSchedules(request: SchedCreateRequestDto): SchedCreateResponseDto {
        val user: User = userService.findUserById(request.userId)

        val todoMap: Map<Long, Todo> = todoService
            .getTodosByIds(request.scheduleInfos.map { it.todoId })
            .associateBy { it.id ?: throw TodoException.fromCode(ApiResultCode.TODO_INVALID_ID) }

        val createdDateTime = LocalDateTime.now()

        val schedules: List<Schedule> = request.scheduleInfos.map {sched ->
            val todo = todoMap[sched.todoId] ?: throw ScheduleException.fromCode(ApiResultCode.SCHED_INVALID_TODO)

            if (todo.doesNotUserOwnTodo(user)) {
                throw ScheduleException.fromCode(ApiResultCode.TODO_NOT_MATCH_USER)
            }

            Schedule.of(
                user = user,
                todo = todo,
                startedDateTime = sched.startedDateTime,
                endedDateTime = sched.endedDateTime,
                createdDateTime = createdDateTime
            )
        }

        val savedSchedules: List<Schedule> = scheduleRepository.saveAll(schedules)

        return SchedCreateResponseDto.fromSchedules(user.id, savedSchedules)
    }

    override fun getUnassignedTodo(userId: Long, date: LocalDate): SchedUnassignedTodosResponseDto {
        val user: User = userService.findUserById(userId)

        val unassignedTodos: MutableList<TodoSummaryWithDateVo> = todoService.getTodoSummariesWithDateFromDueDate(user, date)
        return SchedUnassignedTodosResponseDto.fromTodoSummaryWithDateList(date, unassignedTodos)
    }
}
package com.fesi.flowit.schedule.service

import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.ScheduleException
import com.fesi.flowit.common.response.exceptions.TodoException
import com.fesi.flowit.schedule.dto.*
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

private val log = loggerFor<SchedServiceImpl>()

@Service
class SchedServiceImpl(
    private val userService: UserService,
    private val todoService: TodoService,
    private val scheduleRepository: ScheduleRepository
) : SchedService {

    /**
     * 일정 생성 (Bulk)
     */
    @Transactional
    override fun createSchedules(request: SchedCreateRequestDto): SchedCreateResponseDto {
        val user: User = userService.findUserById(request.userId)
        val todoIds: List<Long> = request.scheduleInfos.map { it.todoId }

        val todoMap: Map<Long, Todo> = todoService
            .getTodosByIds(todoIds)
            .associateBy { it.id ?: throw TodoException.fromCode(ApiResultCode.TODO_INVALID_ID) }

        val createdDateTime = LocalDateTime.now()

        val schedules: List<Schedule> = request.scheduleInfos.map { sched ->
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
        val createdSchedIds = savedSchedules.map { it.id }
        log.debug("(userId=${request.userId} ) Created schedules: ${createdSchedIds}")

        return SchedCreateResponseDto.fromSchedules(user.id, savedSchedules)
    }

    /**
     * 미배치 할 일 조회
     */
    override fun getUnassignedTodos(userId: Long, date: LocalDate): SchedUnassignedTodosResponseDto {
        val user: User = userService.findUserById(userId)

        val unassignedTodos: MutableList<TodoSummaryWithDateVo> = todoService.getTodoSummariesWithDateFromDueDate(user, date)
        return SchedUnassignedTodosResponseDto.fromTodoSummaryWithDateList(date, unassignedTodos)
    }

    /**
     * 배치된 일정 조회
     */
    override fun getAssignedSched(userId: Long, date: LocalDate): SchedAssignedSchedResponseDto {
        val user: User = userService.findUserById(userId)

        val assignedSchedules: List<AssignedSched> = scheduleRepository.findAssignedSchedByDate(user, date)

        return SchedAssignedSchedResponseDto.of(date, assignedSchedules)
    }
}
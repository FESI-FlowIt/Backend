package com.fesi.flowit.schedule.service

import com.fesi.flowit.common.extensions.removeLastComma
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
     * 일정 저장
     * - schedId is null     > 생성
     * - schedId is non-null > 수정
     * - isRemoved=true      > 삭제
     *
     * @return created or updated schedules
     */
    @Transactional
    override fun saveSchedules(userId: Long, request: SchedSaveRequestDto): SchedCreateResponseDto {
        val user: User = userService.findUserById(userId)
        val todoMap: Map<Long, Todo> = todoService
            .getTodosByIds(request.scheduleInfos.map { it.todoId })
            .associateBy { it.id ?: throw TodoException.fromCode(ApiResultCode.TODO_INVALID_ID) }

        val processedDateTime = LocalDateTime.now()

        log.debug("Saved schedules.. userId=${userId}, a number of targets=${request.scheduleInfos.size}")
        val createdLogBuilder: StringBuilder = StringBuilder("\"created\": [")
        val updatedLogBuilder: StringBuilder = StringBuilder("\"updated\": [")
        val deletedLogBuilder: StringBuilder = StringBuilder("\"deleted\": ")

        // 일정 삭제
        val scheduleIdsForDelete = request.scheduleInfos
            .filter { it.isRemoved }
            .map { it.schedId ?: throw ScheduleException.fromCode(ApiResultCode.SCHED_INVALID_ID) }

        scheduleRepository.deleteAllByIds(scheduleIdsForDelete)
        deletedLogBuilder.append("${scheduleIdsForDelete}")

        // 일정 추가 or 수정
        val saveOrUpdateSchedules: MutableList<Schedule> = mutableListOf()

        val updateSchedIds: List<Long> = request.scheduleInfos.mapNotNull { it.schedId }
        val schedulesForUpdateMap: Map<Long, Schedule> = scheduleRepository.findAllById(updateSchedIds).associateBy { it.id!! }

        saveOrUpdateSchedules.addAll(
            request.scheduleInfos
                .filterNot { it.isRemoved }
                .map { requestSched ->
                val todo = todoMap[requestSched.todoId] ?: throw ScheduleException.fromCode(ApiResultCode.SCHED_INVALID_TODO)

                if (todo.doesNotUserOwnTodo(user)) {
                    throw ScheduleException.fromCode(ApiResultCode.TODO_NOT_MATCH_USER)
                }

                if (isSchedForUpdate(requestSched.schedId)) {
                    // 일정 수정
                    val scheduleForUpdate: Schedule = schedulesForUpdateMap[requestSched.schedId]
                        ?: throw ScheduleException.fromCode(ApiResultCode.SCHED_INVALID_ID)

                    scheduleForUpdate.startedDateTime = requestSched.startedDateTime
                    scheduleForUpdate.endedDateTime = requestSched.endedDateTime
                    scheduleForUpdate.modifiedDateTime = processedDateTime

                    updatedLogBuilder
                        .append("{")
                        .append("\"id\": ").append(requestSched.schedId).append(",")
                        .append("\"started\": \"").append(requestSched.startedDateTime).append("\",")
                        .append("\"ended\": \"").append(requestSched.endedDateTime).append("\"")
                        .append("},")

                    scheduleForUpdate
                } else {
                    // 일정 추가
                    val scheduleForCreate = Schedule.of(
                        user = user,
                        todo = todo,
                        startedDateTime = requestSched.startedDateTime,
                        endedDateTime = requestSched.endedDateTime,
                        createdDateTime = processedDateTime
                    )

                    createdLogBuilder
                        .append("{")
                        .append("\"todoId\": ").append(scheduleForCreate.todo.id).append(",")
                        .append("\"started\": \"").append(scheduleForCreate.startedDateTime).append("\",")
                        .append("\"ended\": \"").append(scheduleForCreate.endedDateTime).append("\"")
                        .append("},")

                    scheduleForCreate
                }
            }
        )

        createdLogBuilder.removeLastComma()
        updatedLogBuilder.removeLastComma()
        log.debug("\"result\" : {${createdLogBuilder}], ${updatedLogBuilder}], ${deletedLogBuilder}}")

        val savedSchedules: List<Schedule> = scheduleRepository.saveAll(saveOrUpdateSchedules)
        return SchedCreateResponseDto.fromSchedules(user.id, savedSchedules)
    }

    /**
     * 미배치 할 일 조회
     */
    override fun getUnassignedTodos(userId: Long, date: LocalDate): SchedUnassignedTodosResponseDto {
        val user: User = userService.findUserById(userId)

        val unassignedTodos: MutableList<TodoSummaryWithDateVo> = todoService.getTodoSummariesWithDateFromDueDate(user, date)
        return SchedUnassignedTodosResponseDto.fromTodoSummaryWithDateList(date, unassignedTodos.filter { !it.isDone })
    }

    /**
     * 배치된 일정 조회
     */
    override fun getAssignedSched(userId: Long, date: LocalDate): SchedAssignedSchedResponseDto {
        val user: User = userService.findUserById(userId)

        val assignedSchedules: List<AssignedSched> = scheduleRepository.findAssignedSchedByDate(user, date)

        return SchedAssignedSchedResponseDto.of(date, assignedSchedules)
    }

    private fun isSchedForUpdate(schedId: Long?): Boolean {
        return schedId != null
    }
}
package com.fesi.flowit.todo.service

import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.TodoException
import com.fesi.flowit.goal.service.GoalService
import com.fesi.flowit.note.vo.NoteInfoVo
import com.fesi.flowit.todo.dto.TodoChangeDoneResponseDto
import com.fesi.flowit.todo.dto.TodoCreateResponseDto
import com.fesi.flowit.todo.dto.TodoModifyResponseDto
import com.fesi.flowit.todo.vo.TodoSummaryWithNoteVo
import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.todo.repository.TodoRepository
import com.fesi.flowit.todo.vo.TodoSummaryWithDateVo
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
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

        if (todo.doesNotUserOwnTodo(user)) {
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
     * 할 일 완료 상태 변경
     * 완료를 취소(false) 했을 때 완료 시간을 초기화 하지 않습니다.
     */
    @Transactional
    override fun changeDoneStatus(todoId: Long, userId: Long, isDone: Boolean): TodoChangeDoneResponseDto {
        val user: User = userService.findUserById(userId)
        val todo: Todo = getTodoById(todoId)

        if (todo.doesNotUserOwnTodo(user)) {
            throw TodoException.fromCode(ApiResultCode.TODO_NOT_MATCH_USER)
        }

        val doneDateTime = LocalDateTime.now()

        todo.isDone = isDone
        todo.doneDateTime = doneDateTime
        log.debug("Todo(id=${todoId}) is changed isDone status to ${isDone} at ${doneDateTime}")

        return TodoChangeDoneResponseDto.of(todoId, isDone)
    }

    /**
     * 할 일 삭제
     */
    @Transactional
    override fun deleteTodoById(userId: Long, todoId: Long) {
        val user: User = userService.findUserById(userId)
        val todo: Todo = getTodoById(todoId)

        if (todo.doesNotUserOwnTodo(user)) {
            throw TodoException.fromCode(ApiResultCode.TODO_NOT_MATCH_USER)
        }

        todoRepository.deleteById(todoId)
        log.debug("Deleted todo(id=${todo.id}, name=${todo.name}, isDone=${todo.isDone}")
    }

    /**
     * 할 일 목록 조회 by id list
     */
    override fun getTodosByIds(todoIds: List<Long>): List<Todo> {
        return todoRepository.findAllByIdIn(todoIds)
    }

    /**
     * 목표 마감일에 따른 할 일 목록 조회
     */
    override fun getTodoSummariesWithDateFromDueDate(userId: Long, date: LocalDate): MutableList<TodoSummaryWithDateVo> {
        val user: User = userService.findUserById(userId)
        return getTodoSummariesWithDateFromDueDate(user, date)
    }

    /**
     * 노트를 갖는 할 일 목록 조회
     */
    override fun getTodosSummariesThatHasNote(
        userId: Long,
        goalId: Long
    ): List<TodoSummaryWithNoteVo> {
        val user: User = userService.findUserById(userId)

        return todoRepository.findTodosThatHasNote(user, goalId)
            .map { todo ->
                TodoSummaryWithNoteVo(
                    todoId = todo.id!!,
                    name = todo.name,
                    isDone = todo.isDone,
                    note = listOf(
                        NoteInfoVo(
                            id = todo.note!!.id!!,
                            title = todo.note!!.title,
                            link = todo.note!!.link,
                            content = todo.note!!.content
                        )
                    )
                )
            }
    }

    override fun getTodoSummariesWithDateFromDueDate(user: User, date: LocalDate): MutableList<TodoSummaryWithDateVo> {
        return todoRepository.findTodosByDueDate(user, date.atStartOfDay())
    }

    override fun getTodoById(todoId: Long): Todo {
        return todoRepository.findById(todoId).orElseThrow { TodoException.fromCode(ApiResultCode.TODO_NOT_FOUND) }
    }
}
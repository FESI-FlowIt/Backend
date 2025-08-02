package com.fesi.flowit.schedule.dto

import com.fesi.flowit.common.util.REGEX_RGB_CODE
import com.fesi.flowit.todo.vo.TodoSummaryWithDateVo
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime

data class SchedUnassignedTodosResponseDto(
    @field:Schema(
        description = "조회 날짜",
        example = "2025-07-19",
    )
    var date: LocalDate,

    val unassignedTodos: List<SchedUnassignedTodo>
) {
    companion object {
        fun fromTodoSummaryWithDateList(date: LocalDate, todos: List<TodoSummaryWithDateVo>): SchedUnassignedTodosResponseDto {
            return SchedUnassignedTodosResponseDto(
                date, todos.map { SchedUnassignedTodo.fromTodoSummaryWithDateVo(it) }
            )
        }
    }
}

data class SchedUnassignedTodo(
    @field:Schema(
        description = "할 일 ID",
        example = "1",
    )
    val todoId: Long,

    @field:Schema(
        description = "목표 색상 코드",
        example = "#000000",
        pattern = REGEX_RGB_CODE,
    )
    val color: String,

    @field:Schema(
        description = "할 일 이름",
        example = "할 일 이름",
        minLength = 1,
        maxLength = 30,
    )
    val name: String,

    @field:Schema(
        description = "마감 시간",
        example = "2025-07-19T14:29:00",
    )
    val dueDateTime: LocalDateTime,
) {
    companion object {
        fun fromTodoSummaryWithDateVo(todoSummary: TodoSummaryWithDateVo): SchedUnassignedTodo {
            return SchedUnassignedTodo(todoSummary.todoId, todoSummary.color, todoSummary.name, todoSummary.dueDateTime)
        }
    }
}
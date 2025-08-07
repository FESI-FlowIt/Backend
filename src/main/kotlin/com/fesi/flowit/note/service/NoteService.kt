package com.fesi.flowit.note.service

import com.fesi.flowit.note.dto.NoteInfoResponseDto
import com.fesi.flowit.note.entity.Note
import com.fesi.flowit.note.repository.NoteRepository
import com.fesi.flowit.todo.service.TodoService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class NoteService(
    private val todoService: TodoService,
    private val noteRepository: NoteRepository
) {
    @Transactional
    fun createNote(
        userId: Long,
        todoId: Long,
        title: String,
        link: String,
        content: String
    ): NoteInfoResponseDto {
        val createdDateTime = LocalDateTime.now()
        val todo = todoService.getTodoById(todoId)

        val note = noteRepository.save(
            Note.withTodo(
                title = title,
                link = link,
                content = content,
                createdDateTime = createdDateTime,
                modifiedDateTime = createdDateTime,
                todo = todo
            )
        )

        return NoteInfoResponseDto.fromNote(note)
    }
}

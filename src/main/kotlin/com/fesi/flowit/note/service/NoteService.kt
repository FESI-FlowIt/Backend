package com.fesi.flowit.note.service

import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.NoteException
import com.fesi.flowit.note.dto.NoteDetailResponseDto
import com.fesi.flowit.note.dto.NoteFindAllResponseDto
import com.fesi.flowit.note.dto.NoteInfoResponseDto
import com.fesi.flowit.note.entity.Note
import com.fesi.flowit.note.repository.NoteRepository
import com.fesi.flowit.todo.entity.Todo
import com.fesi.flowit.todo.service.TodoService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

private val log = loggerFor<NoteService>()

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

    @Transactional
    fun getNoteDetail(todoId: Long, noteId: Long): NoteDetailResponseDto {
        val goal = todoService.getTodoById(todoId).goal ?: throw NoteException.fromCode(
            ApiResultCode.NOTE_GOAL_NOT_FOUND
        )
        val note = noteRepository.findById(noteId)
            .orElseThrow { NoteException.fromCode(ApiResultCode.NOTE_NOT_FOUND) }

        return NoteDetailResponseDto.fromNoteWithGoal(note, goal)
    }

    @Transactional
    fun modifyNote(
        todoId: Long,
        noteId: Long,
        title: String,
        link: String,
        content: String
    ): NoteInfoResponseDto? {
        val todo = todoService.getTodoById(todoId)
        val note = getNoteById(noteId)

        if (!todoOwnNote(todo, note)) {
            throw NoteException.fromCode(ApiResultCode.NOTE_TODO_NOT_FOUND)
        }

        log.debug(
            """
            modifyGoal(todoId=${todoId}, noteId=${noteId})..
            title: ${note.title} -> ${title},
            link: ${note.link} -> ${link},
            content: ${note.content} -> ${content},
        """.trimIndent()
        )

        note.title = title
        note.link = link
        note.content = content
        note.modifiedDateTime = LocalDateTime.now()

        return NoteInfoResponseDto.fromNote(note)
    }

    @Transactional
    fun deleteNote(todoId: Long, noteId: Long) {
        val todo = todoService.getTodoById(todoId)
        val note: Note = getNoteById(noteId)

        if (!todoOwnNote(todo, note)) {
            throw NoteException.fromCode(ApiResultCode.NOTE_TODO_NOT_FOUND)
        }

        log.debug("Deleted note=(todoId=${todoId}, noteId=${noteId})")

        noteRepository.deleteById(noteId)
    }

    @Transactional
    fun getAllNotes(todoId: Long): List<NoteFindAllResponseDto>? {
        val todo = todoService.getTodoById(todoId)

        val note = todo.note ?: throw NoteException.fromCode(ApiResultCode.NOTE_NOT_FOUND)

        return listOf(NoteFindAllResponseDto.fromNoteWithTodoId(note, todo.id!!))
    }

    fun getNoteById(todoId: Long): Note {
        return noteRepository.findById(todoId)
            .orElseThrow { NoteException.fromCode(ApiResultCode.NOTE_NOT_FOUND) }
    }

    fun todoOwnNote(todo: Todo, note: Note): Boolean {
        return note.todo == todo
    }
}

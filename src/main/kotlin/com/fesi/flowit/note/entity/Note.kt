package com.fesi.flowit.note.entity

import com.fesi.flowit.todo.entity.Todo
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name = "notes")
class Note(
    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var link: String,

    @Column(nullable = false, length = 10000)
    var content: String,

    @CreatedDate
    @Column(nullable = false)
    val createdDateTime: LocalDateTime,

    @Column(nullable = false)
    var modifiedDateTime: LocalDateTime
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @OneToOne(mappedBy = "note", fetch = FetchType.LAZY)
    var todo: Todo? = null

    companion object {
        fun of(title: String, link: String, content: String, createdDateTime: LocalDateTime, modifiedDateTime: LocalDateTime): Note {
            return Note(title, link, content, createdDateTime, modifiedDateTime)
        }

        fun withTodo(title: String, link: String, content: String, createdDateTime: LocalDateTime, modifiedDateTime: LocalDateTime, todo: Todo): Note {
            val note = of(title, link, content, createdDateTime, modifiedDateTime)
            note.todo = todo
            return note
        }
    }
}

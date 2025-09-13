package com.fesi.flowit.todo.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "todo_materials")
class TodoMaterial private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    val todo: Todo,

    @Enumerated(EnumType.STRING)
    val todoMaterialType: TodoMaterialType,

    @Column
    val name: String? = null,

    @Column(nullable = false)
    val url: String,

    @Column
    val uniqueKey: String? = null,

    @Column(nullable = false)
    val createdDateTime: LocalDateTime
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    companion object {
        fun createFileMaterial(
            todo: Todo,
            todoMaterialType: TodoMaterialType = TodoMaterialType.FILE,
            name: String,
            url: String,
            uniqueKey: String,
            createdDateTime: LocalDateTime
        ): TodoMaterial {
            return TodoMaterial(
                todo = todo,
                todoMaterialType = todoMaterialType,
                name = name,
                url = url,
                uniqueKey = uniqueKey,
                createdDateTime = createdDateTime)
        }

        fun createLinkMaterial(
            todo: Todo,
            todoMaterialType: TodoMaterialType = TodoMaterialType.LINK,
            name: String? = null,
            url: String,
            uniqueKey: String? = null,
            createdDateTime: LocalDateTime
        ): TodoMaterial {
            return TodoMaterial(
                todo = todo,
                todoMaterialType = todoMaterialType,
                name = name,
                url = url,
                uniqueKey = uniqueKey,
                createdDateTime = createdDateTime
            )
        }
    }
}
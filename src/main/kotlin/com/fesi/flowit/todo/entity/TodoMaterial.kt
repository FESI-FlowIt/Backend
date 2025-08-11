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

    @Column(nullable = false)
    val fileName: String,

    @Column(nullable = false)
    val url: String,

    @Column(nullable = false)
    val uniqueKey: String,

    @Column(nullable = false)
    val createdDateTime: LocalDateTime
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    companion object {
        fun of(
            todo: Todo,
            todoMaterialType: TodoMaterialType,
            name: String,
            url: String,
            uniqueKey: String,
            createdDateTime: LocalDateTime
        ): TodoMaterial {
            return TodoMaterial(todo, todoMaterialType, name, url, uniqueKey, createdDateTime)
        }
    }
}
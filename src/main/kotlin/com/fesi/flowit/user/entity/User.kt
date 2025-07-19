package com.fesi.flowit.user.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.Objects

@Entity
@Table(name = "users")
class User(
    @Column(nullable = false)
    val email: String,
    @Column(nullable = false)
    val name: String,
    @Column(nullable = false)
    val password: String,
    @Column(nullable = false)
    val createdAt: LocalDateTime,
    @Column(nullable = true)
    val updatedAt: LocalDateTime,
    @Column(nullable = true)
    val deletedAt: LocalDateTime?,
    @Column(nullable = false)
    val isDeleted: Boolean = false,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    companion object {
        fun of(
            email: String,
            name: String,
            encrypted: String,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
            deletedAt: LocalDateTime?,
            isDeleted: Boolean = false
        ): User {
            return User(email, name, encrypted, createdAt, updatedAt, deletedAt, isDeleted)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (this::class != other::class) {
            return false
        }

        return id == (other as User).id
    }

    override fun hashCode() = Objects.hashCode(id)
}

package com.fesi.flowit.user.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    @Column(nullable = false)
    val email: String,
    @Column(nullable = false)
    val name: String,
    @Column(nullable = false)
    val password: String,
    @Column(nullable = false)
    val createdAt: LocalDateTime,
    @Column(nullable = true)
    val updatedAt: LocalDateTime?,
    @Column(nullable = true)
    val deletedAt: LocalDateTime?
) {
    constructor(
        email: String,
        name: String,
        password: String,
        createdAt: LocalDateTime,
        updatedAt: LocalDateTime?,
        deletedAt: LocalDateTime?
    ) : this(0L, email, name, password, createdAt, updatedAt, deletedAt)

    companion object {
        fun of(
            email: String,
            name: String,
            encrypted: String,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime?,
            deletedAt: LocalDateTime?
        ): User {
            return User(email, name, encrypted, createdAt, updatedAt, deletedAt)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (id != other.id) return false
        if (email != other.email) return false
        if (name != other.name) return false
        if (password != other.password) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false
        if (deletedAt != other.deletedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + (updatedAt?.hashCode() ?: 0)
        result = 31 * result + (deletedAt?.hashCode() ?: 0)
        return result
    }
}

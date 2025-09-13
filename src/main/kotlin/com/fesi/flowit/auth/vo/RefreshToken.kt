package com.fesi.flowit.auth.vo

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tokens")
class RefreshToken(
    @Column(nullable = false)
    val userId: Long,
    @Column(nullable = false)
    val token: String,
    @Column(nullable = false)
    val expiresAt: LocalDateTime,
    @Column(nullable = false)
    val revoked: Boolean = false
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    companion object {
        fun of(
            userId: Long,
            token: String,
            expiresAt: LocalDateTime,
            revoked: Boolean
        ): RefreshToken {
            return RefreshToken(userId, token, expiresAt, revoked)
        }
    }
}

package com.fesi.flowit.auth.repository

import com.fesi.flowit.auth.vo.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface TokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByUserIdAndRevoked(userId: Long, revoked: Boolean): RefreshToken?
    fun findByTokenAndRevoked(token: String, revoked: Boolean): RefreshToken?

    @Query(
        """
           SELECT new com.fesi.flowit.auth.vo.RefreshToken(
              rt.userId,
              rt.token,
              rt.expiresAt,
              rt.revoked
           )
            FROM RefreshToken rt
            WHERE rt.userId = :userId AND rt.revoked = :revoked
            ORDER BY rt.id DESC 
            LIMIT 1
        """
    )
    fun findFreshByUserIdAndRevoked(
        @Param("userId") userId: Long,
        @Param("revoked") revoked: Boolean
    ): List<RefreshToken>

    @Modifying
    @Query("update RefreshToken rt set rt.revoked = :revoked where rt.token = :token")
    fun updateRevoked(
        @Param(value = "token") token: String,
        @Param(value = "revoked") revoked: Boolean
    )
}

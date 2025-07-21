package com.fesi.flowit.auth.repository

import com.fesi.flowit.auth.vo.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface TokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByUserId(userId: Long): RefreshToken?
    fun findByUserIdAndRevoked(userId: Long, revoked: Boolean): RefreshToken?
    @Modifying
    @Query("update RefreshToken rt set rt.revoked = :revoked where rt.userId = :userId")
    fun updateRevoked(
        @Param(value = "userId") userId: Long,
        @Param(value = "revoked") revoked: Boolean
    )
}

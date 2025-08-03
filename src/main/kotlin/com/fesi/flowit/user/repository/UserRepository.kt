package com.fesi.flowit.user.repository

import com.fesi.flowit.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    override fun findById(id: Long): Optional<User>
    fun findByEmail(email: String): User?
}

package com.fesi.flowit.user.service

import com.fesi.flowit.common.auth.PasswordEncryptor
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.exception.UserAlreadySignedUpException
import com.fesi.flowit.user.repository.UserRepository
import com.fesi.flowit.user.service.dto.UserDto
import com.fesi.flowit.user.web.response.UserResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Transactional
@Service
class UserService(
    private val repository: UserRepository,
    private val encryptor: PasswordEncryptor
) {
    fun add(dto: UserDto): UserResponse {
        val (email, name, password) = dto

        if (repository.findByEmail(email) != null) {
            throw UserAlreadySignedUpException()
        }

        val encrypted = encryptor.encrypt(password)
        val user = User.of(email, name, encrypted, LocalDateTime.now(), null, null)

        val addedUser = repository.save(user)

        return UserResponse.from(addedUser)
    }
}

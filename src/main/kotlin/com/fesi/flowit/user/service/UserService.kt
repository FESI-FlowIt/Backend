package com.fesi.flowit.user.service

import com.fesi.flowit.common.auth.PasswordEncryptor
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.common.response.exceptions.UserAlreadySignedUpException
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
    /**
     * 회원가입
     * 이미 등록된 회원일 경우 회원가입 실패
     * 사용자 정보 db에 저장 시 비밀번호는 암호화한다
     */
    fun add(dto: UserDto): UserResponse {
        val (email, name, password) = dto

        if (repository.findByEmail(email) != null) {
            throw UserAlreadySignedUpException()
        }

        val encrypted = encryptor.encrypt(password)
        val user = User.of(email, name, encrypted, LocalDateTime.now(), LocalDateTime.now(), null)

        val addedUser = repository.save(user)

        return UserResponse.from(addedUser)
    }
}

package com.fesi.flowit.user.service

import com.fesi.flowit.common.auth.JwtProcessor
import com.fesi.flowit.common.auth.PasswordEncryptor
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.common.response.exceptions.UserAlreadySignedUpException
import com.fesi.flowit.common.response.exceptions.UserNotExistsException
import com.fesi.flowit.user.repository.UserRepository
import com.fesi.flowit.user.service.dto.UserDto
import com.fesi.flowit.user.web.response.UserResponse
import com.fesi.flowit.user.web.response.UserSignedUpResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService(
    private val repository: UserRepository,
    private val encryptor: PasswordEncryptor,
    private val jwtProcessor: JwtProcessor
) {
    /**
     * 회원가입
     * 이미 등록된 회원일 경우 회원가입 실패
     * 사용자 정보 db에 저장 시 비밀번호는 암호화한다
     */
    @Transactional
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

    /**
     * 주어진 이메일로 회원가입한 사용자가 있는지 확인
     */
    fun hasUserWithEmail(email: String): UserSignedUpResponse {
        if (repository.findByEmail(email) != null) {
            return UserSignedUpResponse(true)
        }
        return UserSignedUpResponse(false)
    }

    /**
     * 주어진 액세스 토큰으로 회원을 검색
     */
    fun findUserByToken(accessToken: String): UserResponse {
        val tokenInfo = jwtProcessor.unpack(accessToken)
        val authentication = jwtProcessor.getAuthentication(tokenInfo)

        val user = authentication.principal as User

        return UserResponse.from(user)
    }

    fun findUserById(userId: Long): User {
        return repository.findById(userId).orElseThrow { UserNotExistsException.fromCode(ApiResultCode.AUTH_USER_NOT_EXISTS) }
    }
}

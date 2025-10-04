package com.fesi.flowit.user.service

import com.fesi.flowit.auth.service.JwtProcessor
import com.fesi.flowit.auth.service.PasswordEncryptor
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.repository.UserRepository
import com.fesi.flowit.user.dto.SignUpResponseDto
import com.fesi.flowit.user.dto.UserExistCheckResponseDto
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncryptor: PasswordEncryptor,
    private val jwtProcessor: JwtProcessor
) {
    /**
     * 회원가입
     * 이미 등록된 회원일 경우 회원가입 실패
     * 사용자 정보 db에 저장 시 비밀번호는 암호화한다
     */
    @Transactional
    fun signUp(email: String, name: String, password: String): SignUpResponseDto {
        if (userRepository.findByEmail(email) != null) {
            throw AuthException.withCodeAndStatus(ApiResultCode.AUTH_USER_ALREADY_EXISTS, HttpStatus.CONFLICT)
        }

        val encrypted = passwordEncryptor.encrypt(password)
        val user = User.of(email, name, encrypted, LocalDateTime.now(), LocalDateTime.now(), null)

        val addedUser = userRepository.save(user)

        return SignUpResponseDto.from(addedUser)
    }

    /**
     * 주어진 이메일로 회원가입한 사용자가 있는지 확인
     */
    fun checkExistUserByEmail(email: String): UserExistCheckResponseDto {
        if (userRepository.findByEmail(email) != null) {
            return UserExistCheckResponseDto(true)
        }
        return UserExistCheckResponseDto(false)
    }

    /**
     * 주어진 액세스 토큰으로 회원을 검색
     */
    fun findUserByToken(accessToken: String): SignUpResponseDto {
        val tokenInfo = jwtProcessor.unpack(accessToken)
        val authentication = jwtProcessor.getAuthentication(tokenInfo)

        val user = authentication.principal as User

        return SignUpResponseDto.from(user)
    }

    fun findUserById(userId: Long): User {
        return userRepository.findById(userId).orElseThrow { AuthException.fromCode(ApiResultCode.AUTH_USER_NOT_EXISTS) }
    }
}

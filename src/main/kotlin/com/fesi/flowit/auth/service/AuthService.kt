package com.fesi.flowit.auth.service

import com.fesi.flowit.common.response.exceptions.InvalidPasswordException
import com.fesi.flowit.common.response.exceptions.UserNotExistsException
import com.fesi.flowit.auth.service.dto.SignInDto
import com.fesi.flowit.auth.web.response.RegenerateResponse
import com.fesi.flowit.auth.web.response.SignInResponse
import com.fesi.flowit.common.auth.JwtProcessor
import com.fesi.flowit.common.auth.PasswordEncryptor
import com.fesi.flowit.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class AuthService(
    private val repository: UserRepository,
    private val encryptor: PasswordEncryptor,
    private val jwtGenerator: JwtGenerator,
    private val jwtProcessor: JwtProcessor
) {
    /**
     * 로그인
     * 요청 정보의 이메일로 등록된 회원이 없거나 비밀번호가 다르면 로그인 실패
     * access token은 로그인 시마다 생성
     * refresh token은 상태에 따라 처리
     */
    fun signIn(dto: SignInDto): Pair<SignInResponse, String> {
        val userFoundByEmail = repository.findByEmail(dto.email) ?: throw UserNotExistsException()

        if (!encryptor.encrypt(dto.password).equals(userFoundByEmail.password)) {
            throw InvalidPasswordException()
        }

        val accessToken = jwtGenerator.generateToken(userFoundByEmail)
        jwtGenerator.handleRefreshToken(userFoundByEmail)

        return Pair(SignInResponse.of(userFoundByEmail), accessToken)
    }

    /**
     * 토큰을 재발급한다. refresh 토큰은 생성 후 db에만 저장하고, access token은 응답에 넣어서 클라이언트에 준다
     */
    fun regenerate(accessToken: String): RegenerateResponse {
        val userEmail = jwtProcessor.verifyForRegenerate(accessToken)
        val user = repository.findByEmail(userEmail) ?: throw UserNotExistsException()

        val newAccessToken = jwtGenerator.generateToken(user)
        jwtGenerator.handleRefreshToken(user)

        return RegenerateResponse.of(newAccessToken)
    }
}

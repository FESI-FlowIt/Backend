package com.fesi.flowit.auth.service

import com.fesi.flowit.auth.exception.InvalidPasswordException
import com.fesi.flowit.auth.exception.UserNotExistsException
import com.fesi.flowit.auth.service.dto.SignInDto
import com.fesi.flowit.auth.web.response.SignInResponse
import com.fesi.flowit.common.auth.PasswordEncryptor
import com.fesi.flowit.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class AuthService(
    private val repository: UserRepository,
    private val encryptor: PasswordEncryptor,
    private val jwtGenerator: JwtGenerator
) {
    fun signIn(dto: SignInDto): Pair<SignInResponse, String> {
        val userFoundByEmail = repository.findByEmail(dto.email)

        if (userFoundByEmail == null) {
            throw UserNotExistsException()
        }

        if (!encryptor.encrypt(dto.password).equals(userFoundByEmail.password)) {
            throw InvalidPasswordException()
        }

        val accessToken = jwtGenerator.generateToken(userFoundByEmail)
        jwtGenerator.handleRefreshToken(userFoundByEmail)

        return Pair(SignInResponse.of(userFoundByEmail), accessToken)
    }
}

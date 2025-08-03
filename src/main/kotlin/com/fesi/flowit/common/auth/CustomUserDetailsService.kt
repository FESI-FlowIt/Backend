package com.fesi.flowit.common.auth

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import com.fesi.flowit.common.response.exceptions.UserNotExistsException
import com.fesi.flowit.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val repository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        return repository.findByEmail(email)
            ?: throw UserNotExistsException.fromCode(ApiResultCode.AUTH_USER_NOT_EXISTS)
    }

    fun loadUserById(id: Long): UserDetails {
        return repository.findById(id).orElseThrow {
            AuthException.fromCode(ApiResultCode.AUTH_USER_NOT_EXISTS)
        }
    }
}
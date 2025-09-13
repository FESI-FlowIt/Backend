package com.fesi.flowit.common.auth

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import com.fesi.flowit.user.entity.User
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class SocialAuthenticationProvider(
    private val userDetailsService: UserDetailsService
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val principal = authentication.principal as User
        val user = userDetailsService.loadUserByUsername(principal.username)
            ?: throw AuthException.fromCode(ApiResultCode.UNAUTHORIZED)

        return SocialAuthenticationToken(user).apply {
            isAuthenticated = true
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return SocialAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}

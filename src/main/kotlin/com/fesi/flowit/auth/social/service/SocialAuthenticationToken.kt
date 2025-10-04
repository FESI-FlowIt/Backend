package com.fesi.flowit.auth.social.service

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails

class SocialAuthenticationToken(
    private val principal: UserDetails
) : AbstractAuthenticationToken(emptyList()) {

    override fun getPrincipal(): Any = principal
    override fun getCredentials(): Any = ""

    init {
        isAuthenticated = false
    }
}
package com.fesi.flowit.auth.service

import com.fesi.flowit.auth.dto.AccessTokenReissueResponseDto
import com.fesi.flowit.auth.dto.SignInResponseDto
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import com.fesi.flowit.user.entity.User
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtGenerator: JwtGenerator,
    private val jwtProcessor: JwtProcessor,
    private val authenticationManager: AuthenticationManager
) {
    /**
     * 로그인
     * 요청 정보의 이메일로 등록된 회원이 없거나 비밀번호가 다르면 로그인 실패
     * access token은 로그인 시마다 생성
     * refresh token은 상태에 따라 처리
     */
    fun signIn(email: String, password: String): SignInResponseDto {
        val authentication = authenticate(email, password)
        val tokens = jwtGenerator.generateToken(authentication)
        val refreshToken = jwtGenerator.handleRefreshToken(authentication)

        return SignInResponseDto.withRefreshToken(
            authentication.principal as User,
            tokens.token,
            tokens.expiresIn,
            refreshToken
        )
    }

    /**
     * 토큰을 재발급한다
     */
    fun regenerate(refreshToken: String): AccessTokenReissueResponseDto {
        validateRefreshToken(refreshToken)

        val tokenInfo = jwtProcessor.unpackRefreshToken(refreshToken)
        val authentication = jwtProcessor.getAuthenticationFromId(tokenInfo.userId)
        val tokens = jwtGenerator.generateToken(authentication)
        val newRefreshToken = jwtGenerator.handleRefreshTokenWith(refreshToken)

        return AccessTokenReissueResponseDto.withRefreshToken(
            accessToken = tokens.token,
            expiresIn = tokens.expiresIn,
            refreshToken = newRefreshToken
        )
    }

    private fun authenticate(email: String, password: String): Authentication {
        val authenticationToken = UsernamePasswordAuthenticationToken(email, password)

        return try {
            authenticationManager.authenticate(authenticationToken)
        } catch (_: AuthenticationException) {
            throw AuthException.fromCode(ApiResultCode.UNAUTHORIZED)
        }
    }

    private fun validateRefreshToken(refreshToken: String) {
        if (!jwtProcessor.verify(refreshToken)) {
            throw AuthException.fromCode(ApiResultCode.AUTH_TOKEN_INVALID)
        }
    }
}
package com.fesi.flowit.auth.service

import com.fesi.flowit.auth.repository.TokenRepository
import com.fesi.flowit.auth.service.dto.SignInDto
import com.fesi.flowit.auth.web.response.RegenerateResponse
import com.fesi.flowit.auth.web.response.SignInResponse
import com.fesi.flowit.common.auth.JwtProcessor
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import com.fesi.flowit.user.entity.User
import jakarta.transaction.Transactional
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service

@Transactional
@Service
class AuthService(
    private val jwtGenerator: JwtGenerator,
    private val jwtProcessor: JwtProcessor,
    private val authenticationManager: AuthenticationManager,
    private val refreshTokenRepository: TokenRepository
) {
    /**
     * 로그인
     * 요청 정보의 이메일로 등록된 회원이 없거나 비밀번호가 다르면 로그인 실패
     * access token은 로그인 시마다 생성
     * refresh token은 상태에 따라 처리
     */
    fun signIn(dto: SignInDto): Triple<SignInResponse, String, String> {
        val authenticationToken = UsernamePasswordAuthenticationToken(dto.email, dto.password)

        val accessToken: String
        val refreshToken: String
        val authentication: Authentication
        try {
            authentication = authenticationManager.authenticate(authenticationToken)
            accessToken = jwtGenerator.generateToken(authentication)
            refreshToken = jwtGenerator.handleRefreshToken(authentication) ?: ""
        } catch (e: AuthenticationException) {
            throw AuthException.fromCode(ApiResultCode.UNAUTHORIZED)
        }

        return Triple(
            SignInResponse.of(authentication.principal as User),
            accessToken,
            refreshToken
        )
    }

    /**
     * 토큰을 재발급한다
     */
    fun regenerate(accessToken: String, refreshToken: String): RegenerateResponse {
        if (!jwtProcessor.verify(refreshToken)) {
            throw AuthException.fromCode(ApiResultCode.AUTH_TOKEN_INVALID)
        }

        val tokenInfo = jwtProcessor.unpackExpired(accessToken)
        val authentication = jwtProcessor.getAuthentication(tokenInfo)
        val userDetails = authentication.principal as User
        val storedRefreshToken = refreshTokenRepository.findByUserId(userDetails.id)

        if (!storedRefreshToken?.token.equals(refreshToken)) {
            throw AuthException.fromCodeWithMsg(
                ApiResultCode.AUTH_USER_INFO_NOT_MATCH,
                "User info of given refresh token not match with user info in database"
            )
        }

        val newAccessToken = jwtGenerator.generateToken(authentication)
        val newRefreshToken = jwtGenerator.handleRefreshToken(authentication) ?: ""

        if (newRefreshToken == "") {
            return RegenerateResponse.of(accessToken = newAccessToken)
        } else {
            return RegenerateResponse.of(accessToken = newAccessToken)
                .with(refreshToken = newRefreshToken)
        }
    }
}

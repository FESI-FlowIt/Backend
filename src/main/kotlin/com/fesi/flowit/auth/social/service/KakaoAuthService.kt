package com.fesi.flowit.auth.social.service

import com.fesi.flowit.auth.service.JwtGenerator
import com.fesi.flowit.auth.social.dto.KakaoSignInResponse
import com.fesi.flowit.auth.social.dto.KakaoTokenRequestDto
import com.fesi.flowit.auth.social.dto.KakaoTokenResponseDto
import com.fesi.flowit.auth.social.dto.KakaoUserInfoResponseDto
import com.fesi.flowit.common.auth.SocialAuthenticationToken
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import com.fesi.flowit.user.entity.User
import com.fesi.flowit.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service
import org.springframework.web.util.DefaultUriBuilderFactory
import java.time.LocalDateTime

@Service
class KakaoAuthService(
    @Value("\${auth.kakao.client-id}")
    private val clientId: String,
    @Value("\${auth.kakao.request-uri}")
    private val requestUri: String,
    private val kakaoApiRequester: RestClientKakaoApiRequester,
    private val userRepository: UserRepository,
    private val jwtGenerator: JwtGenerator,
    private val authenticationManager: AuthenticationManager
) {
    private val KAUTH_TOKEN_URL_HOST = "kauth.kakao.com"
    private val KAUTH_USER_URL_HOST = "kapi.kakao.com"
    private val KAUTH_TOKEN_GRANT_TYPE = "authorization_code"
    private val LOCAL_PROVIDER = "local"
    private val KAKAO_PROVIDER = "kakao"

    @Transactional
    fun authenticate(code: String): KakaoSignInResponse {
        val accessToken = fetchAccessToken(code)
        val userInfo = fetchUserInfo(accessToken)
        if (validateUserInfo(userInfo)) {
        }
        val email = userInfo.kakaoAccount?.email!!
        val nickname = userInfo.kakaoAccount.profile?.nickname ?: "닉네임을 등록해주세요"
        if (isLocalAccountExists(email)) {
            throw AuthException.fromCode(ApiResultCode.AUTH_FAIL_TO_SIGNUP_DUPLICATE_USER)
        }
        val user: User = User.of(
            email,
            nickname,
            "",
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            provider = KAKAO_PROVIDER
        )

        if (!isKakaoAccountExists(email)) {
            // 회원가입
            userRepository.save(user)
        }

        // 로그인
        val authenticationToken = SocialAuthenticationToken(user)

        val accessTokenAndExpiresIn: Pair<String, Long>
        val refreshToken: String
        val authentication: Authentication
        try {
            authentication = authenticationManager.authenticate(authenticationToken)
            accessTokenAndExpiresIn = jwtGenerator.generateToken(authentication)
            refreshToken = jwtGenerator.handleRefreshToken(authentication)
        } catch (e: AuthenticationException) {
            throw AuthException.fromCode(ApiResultCode.UNAUTHORIZED)
        }

        return KakaoSignInResponse.of(
            authentication.principal as User,
            accessTokenAndExpiresIn.first,
            accessTokenAndExpiresIn.second
        ).with(refreshToken = refreshToken)
    }

    fun fetchAccessToken(code: String): String {
        val uri = makeReqUri(code)
        val body = makeReqBody(code)
        val kakaoAuthResponse = reqTokenToExtServer(uri, body)

        return kakaoAuthResponse.accessToken
    }

    internal fun makeReqBody(code: String): KakaoTokenRequestDto {
        return KakaoTokenRequestDto(
            grantType = KAUTH_TOKEN_GRANT_TYPE,
            clientId = clientId,
            redirectUri = requestUri,
            code = code
        )
    }

    internal fun makeReqUri(code: String): String {
        val factory = DefaultUriBuilderFactory()
        factory.encodingMode = DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY
        val uriBuilder = factory.builder()

        return uriBuilder.scheme("https")
            .host(KAUTH_TOKEN_URL_HOST)
            .path("/oauth/token")
            .toUriString()
    }

    internal fun reqTokenToExtServer(uri: String, body: KakaoTokenRequestDto): KakaoTokenResponseDto {
        return kakaoApiRequester.requestAccessToken(uri, body) ?: throw AuthException.fromCodeWithMsg(
            ApiResultCode.AUTH_FAIL_TO_FETCH_TOKEN,
            "Failed to fetch token from external authentication server"
        )
    }

    fun fetchUserInfo(accessToken: String): KakaoUserInfoResponseDto {
        val uri = makeUserInfoReqUri()
        return reqUserInfoToExtServer(uri, accessToken)
    }

    private fun makeUserInfoReqUri(): String {
        val factory = DefaultUriBuilderFactory()
        factory.encodingMode = DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY
        val uriBuilder = factory.builder()

        return uriBuilder.scheme("https")
            .host(KAUTH_USER_URL_HOST)
            .path("/v2/user/me")
            .toUriString()
    }

    internal fun reqUserInfoToExtServer(uri: String, accessToken: String): KakaoUserInfoResponseDto {
        val response = kakaoApiRequester.requestUserInfo(uri, accessToken)
            ?: throw AuthException.fromCodeWithMsg(
                ApiResultCode.AUTH_FAIL_TO_FETCH_USER_INFO,
                "Failed to fetch user info from external authentication server"
            )
        return response
    }

    fun validateUserInfo(userInfo: KakaoUserInfoResponseDto): Boolean {
        val kakaoAccount = userInfo.kakaoAccount ?: throw AuthException.fromCodeWithMsg(
            ApiResultCode.AUTH_FAIL_TO_FETCH_USER_INFO,
            "Fail to fetch kakao account info"
        )

        val isEmailValid = kakaoAccount.isEmailValid
        val isEmailVerified = kakaoAccount.isEmailVerified
        val email = kakaoAccount.email

        if (isEmailValid == true && isEmailVerified == true && email != null) {
            return true
        }

        throw AuthException.fromCodeWithMsg(
            ApiResultCode.AUTH_FAIL_TO_FETCH_USER_INFO,
            buildString {
                append("Invalid kakao account email:")
                if (isEmailValid != true) append(" isEmailValid=$isEmailValid;")
                if (isEmailVerified != true) append(" isEmailVerified=$isEmailVerified;")
                if (email == null) append(" email=null;")
            }
        )
    }

    fun isLocalAccountExists(email: String): Boolean {
        return userRepository.findByEmailAndProvider(email, LOCAL_PROVIDER) != null
    }

    fun isKakaoAccountExists(email: String): Boolean {
        return userRepository.findByEmailAndProvider(email, KAKAO_PROVIDER) != null
    }
}
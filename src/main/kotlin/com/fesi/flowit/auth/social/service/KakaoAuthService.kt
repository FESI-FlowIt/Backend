package com.fesi.flowit.auth.social.service

import com.fesi.flowit.auth.social.dto.KakaoTokenRequestDto
import com.fesi.flowit.auth.social.dto.KakaoTokenResponseDto
import com.fesi.flowit.auth.social.dto.KakaoUserInfoResponseDto
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.util.DefaultUriBuilderFactory

@Service
class KakaoAuthService(
    @Value("\${auth.kakao.client-id}")
    private val clientId: String,
    @Value("\${auth.kakao.request-uri}")
    private val requestUri: String,
    private val kakaoApiRequester: KakaoApiRequester
) {
    private val KAUTH_TOKEN_URL_HOST = "kauth.kakao.com"
    private val KAUTH_USER_URL_HOST = "kapi.kakao.com"
    private val KAUTH_TOKEN_GRANT_TYPE = "authorization_code"

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
}
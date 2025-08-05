package com.fesi.flowit.auth.social.service

import com.fesi.flowit.auth.social.dto.KakaoTokenRequestDto
import com.fesi.flowit.auth.social.dto.KakaoTokenResponseDto
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
        val kakaoAuthResponse = reqToExtServer(uri, body)

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

    internal fun reqToExtServer(uri: String, body: KakaoTokenRequestDto): KakaoTokenResponseDto {
        return kakaoApiRequester.requestAccessToken(uri, body) ?: throw AuthException.fromCodeWithMsg(
            ApiResultCode.AUTH_FAIL_TO_FETCH_TOKEN,
            "Failed to fetch token from external authentication server"
        )
    }
}
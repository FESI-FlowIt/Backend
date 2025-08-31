package com.fesi.flowit.auth.social.service

import com.fesi.flowit.auth.social.dto.KakaoTokenRequestDto
import com.fesi.flowit.auth.social.dto.KakaoTokenResponseDto
import com.fesi.flowit.auth.social.dto.KakaoUserInfoResponseDto
import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestClient

private val log = loggerFor<RestClientKakaoApiRequester>()

@Component
class RestClientKakaoApiRequester(
    private val restClient: RestClient
) : KakaoApi {
    private val CONTENT_TYPE_FORM_UTF8 = "application/x-www-form-urlencoded; charset=UTF-8"
    private val BEARER_AUTH_TYPE = "Bearer "

    override fun requestAccessToken(
        uri: String,
        body: KakaoTokenRequestDto
    ): KakaoTokenResponseDto? {
        val formData = body.toFormData()
        log.debug(">>request access token for kakao service: formData ${formData}")

        return try {
            restClient.post()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_FORM_UTF8)
                .body(formData)
                .retrieve()
                .body(KakaoTokenResponseDto::class.java)
        } catch (ex: HttpClientErrorException) {
            val message =
                "Failed to fetch token from external authentication server. Status: ${ex.statusCode}, Body: ${{ ex.responseBodyAsString }}"
            log.error(message, ex)
            throw AuthException.fromCodeWithMsg(ApiResultCode.AUTH_FAIL_TO_FETCH_TOKEN, message)
        } catch (ex: HttpServerErrorException) {
            val message =
                "Failed to fetch token from external authentication server. Status: ${ex.statusCode}, Body: ${{ ex.responseBodyAsString }}"
            log.error(message, ex)
            throw AuthException.fromCodeWithMsg(ApiResultCode.AUTH_FAIL_TO_FETCH_TOKEN, message)
        }
    }

    override fun requestUserInfo(uri: String, accessToken: String): KakaoUserInfoResponseDto? {
        log.debug(">>request user info for kakao service")

        return try {
            restClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, BEARER_AUTH_TYPE + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_FORM_UTF8)
                .retrieve()
                .body(KakaoUserInfoResponseDto::class.java)
        } catch (ex: HttpClientErrorException) {
            val message =
                "Failed to fetch user info from external authentication server. Status: ${ex.statusCode}, Body: ${ex.responseBodyAsString}"
            log.error(message, ex)
            throw AuthException.fromCodeWithMsg(ApiResultCode.AUTH_FAIL_TO_FETCH_USER_INFO, message)
        } catch (ex: HttpServerErrorException) {
            val message =
                "Failed to fetch user info from external authentication server. Status: ${ex.statusCode}, Body: ${ex.responseBodyAsString}"
            log.error(message, ex)
            throw AuthException.fromCodeWithMsg(ApiResultCode.AUTH_FAIL_TO_FETCH_USER_INFO, message)
        }
    }
}

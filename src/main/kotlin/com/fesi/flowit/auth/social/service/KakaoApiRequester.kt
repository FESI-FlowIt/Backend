package com.fesi.flowit.auth.social.service

import com.fesi.flowit.auth.social.dto.KakaoTokenRequestDto
import com.fesi.flowit.auth.social.dto.KakaoTokenResponseDto
import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private val log = loggerFor<KakaoApiRequester>()

@Component
class KakaoApiRequester(
    private val webClient: WebClient
) {
    private val CONTENT_TYPE_FORM_UTF8 = "application/x-www-form-urlencoded; charset=UTF-8"

    fun requestAccessToken(uri: String, body: KakaoTokenRequestDto): KakaoTokenResponseDto? {
        val formData = body.toFormData()
        log.debug(">>request access token for kakao service: formData ${formData}")

        val response = webClient.post()
            .uri(uri)
            .header(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_FORM_UTF8)
            .bodyValue(formData)
            .retrieve()
            .onStatus(
                { status -> status.is4xxClientError || status.is5xxServerError },
                { response ->
                    response.bodyToMono(String::class.java)
                        .defaultIfEmpty("No response body")
                        .flatMap { responseBody ->
                            val statusCode = response.statusCode().toString()
                            val message =
                                "Failed to fetch token from external authentication server. Status: $statusCode, Body: ${responseBody}"
                            Mono.error(
                                AuthException.fromCodeWithMsg(
                                    ApiResultCode.AUTH_FAIL_TO_FETCH_TOKEN,
                                    message
                                )
                            )
                        }
                }
            )
            .bodyToMono(KakaoTokenResponseDto::class.java)
            .block()

        return response
    }
}
package com.fesi.flowit.auth.social.service

import com.fesi.flowit.auth.social.dto.KakaoTokenRequestDto
import com.fesi.flowit.auth.social.dto.KakaoTokenResponseDto
import com.fesi.flowit.auth.social.dto.KakaoUserInfoResponseDto
import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private val log = loggerFor<RestClientKakaoApiRequester>()

@Component
class RestClientKakaoApiRequester(
    private val restClient: RestClient
) : KakaoApi {
    override fun requestAccessToken(uri: String, body: KakaoTokenRequestDto): KakaoTokenResponseDto? {
        return restClient.post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body.toFormData())
            .retrieve()
            .body(KakaoTokenResponseDto::class.java)
    }

    override fun requestUserInfo(uri: String, accessToken: String): KakaoUserInfoResponseDto? {
        return restClient.get()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .retrieve()
            .body(KakaoUserInfoResponseDto::class.java)
    }
}

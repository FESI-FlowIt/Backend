package com.fesi.flowit.auth.social.kakao.dto

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

data class KakaoTokenRequestDto(
    val grantType: String,
    val clientId: String,
    val redirectUri: String,
    val code: String
) {
    /**
     * application/x-www-form-urlencoded 방식 요청 본문을 만든다
     */
    fun toFormData(): MultiValueMap<String, String> {
        return LinkedMultiValueMap<String, String>().apply {
            add("grant_type", grantType)
            add("client_id", clientId)
            add("redirect_uri", redirectUri)
            add("code", code)
        }
    }
}
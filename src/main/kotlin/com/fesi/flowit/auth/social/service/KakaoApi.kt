package com.fesi.flowit.auth.social.service

import com.fesi.flowit.auth.social.dto.KakaoTokenRequestDto
import com.fesi.flowit.auth.social.dto.KakaoTokenResponseDto
import com.fesi.flowit.auth.social.dto.KakaoUserInfoResponseDto

interface KakaoApi {
    fun requestAccessToken(uri: String, body: KakaoTokenRequestDto): KakaoTokenResponseDto?
    fun requestUserInfo(uri: String, accessToken: String): KakaoUserInfoResponseDto?
}

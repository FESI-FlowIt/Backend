package com.fesi.flowit.auth.social.kakao.service

import com.fesi.flowit.auth.social.kakao.dto.KakaoTokenRequestDto
import com.fesi.flowit.auth.social.kakao.dto.KakaoTokenResponseDto
import com.fesi.flowit.auth.social.kakao.dto.KakaoUserInfoResponseDto

/**
 * @TODO 외부 서비스 공통 인터페이스로 개선 필요
 */

interface KakaoApi {
    fun requestAccessToken(uri: String, body: KakaoTokenRequestDto): KakaoTokenResponseDto?
    fun requestUserInfo(uri: String, accessToken: String): KakaoUserInfoResponseDto?
}

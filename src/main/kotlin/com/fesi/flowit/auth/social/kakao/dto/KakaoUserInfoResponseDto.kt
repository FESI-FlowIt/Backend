package com.fesi.flowit.auth.social.kakao.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class KakaoUserInfoResponseDto(
    val id: Long,
    @JsonProperty("has_signed_up")
    val hasSignedUp: Boolean?,
    @JsonProperty("connected_at")
    val connectedAt: Date?,
    val properties: Map<String, String>?,
    @JsonProperty("kakao_account")
    val kakaoAccount: KakaoAccount?,
)

data class KakaoAccount(
    @JsonProperty("profile_nickname_needs_agreement")
    val profileNicknameNeedsAgreement: Boolean?,
    val profile: Profile?,
    @JsonProperty("email_needs_agreement")
    val emailNeedsAgreement: Boolean? = null,
    @JsonProperty("is_email_valid")
    val isEmailValid: Boolean?,
    @JsonProperty("is_email_verified")
    val isEmailVerified: Boolean?,
    val email: String?
)

data class Profile(
    @JsonProperty("nickname")
    val nickname: String?
)
package com.fesi.flowit.common.auth

import com.fesi.flowit.common.auth.dto.TokenInfo
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.*
import com.fesi.flowit.user.repository.UserRepository
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component


@Component
class JwtProcessor(
    @Value("\${auth.secret-key}")
    private val secretKey: String,
    private val userRepository: UserRepository,
    private val customUserDetailsService: CustomUserDetailsService
) {
    val key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

    fun getAuthentication(tokenInfo: TokenInfo): Authentication {
        val userDetails = customUserDetailsService.loadUserByUsername(tokenInfo.email)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getAuthenticationFromId(id: Long): Authentication {
        val userDetails = customUserDetailsService.loadUserById(id)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun verify(token: String): Boolean {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
            return true
        } catch (e: ExpiredJwtException) {
            throw FailToParseJwtException.fromCode(
                ApiResultCode.AUTH_TOKEN_EXPIRED
            )
        } catch (ex: JwtException) {
            throw FailToParseJwtException.fromCodeWithMsg(
                ApiResultCode.AUTH_FAIL_TO_PARSE_JWT,
                "Fail to parse JWT token for token regenerate"
            )
        }
    }

    /**
     * 인증이 필요한 API 호출 시 클라이언트로부터 받은 토큰을 처리한다
     * 등록되지 않은 정보로 만들어진 토큰일 경우 예외 처리한다
     */
    fun handle(token: String): TokenInfo {
        val tokenInfo = unpack(token)

        if (!isTokenStored(tokenInfo)) {
            throw InvalidUserException()
        }

        return tokenInfo
    }

    /**
     * 문자열로 된 토큰을 파싱한다
     */
    fun unpack(token: String): TokenInfo {
        var unpacked: Jws<Claims>

        try {
            unpacked = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
        } catch (e: ExpiredJwtException) {
            throw TokenExpiredException()
        } catch (ex: JwtException) {
            throw FailToParseJwtException()
        }

        val claims = unpacked.payload

        return TokenInfo(
            email = claims.subject,
            userId = (claims["userId"] as String).toLong(), // String으로 저장된 userId를 Long으로 복원
            issuedAt = claims.issuedAt,
            expiration = claims.expiration
        )
    }

    fun unpackRefreshToken(token: String): TokenInfo {
        val unpacked = try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
        } catch (e: ExpiredJwtException) {
            throw AuthException.fromCode(ApiResultCode.AUTH_TOKEN_EXPIRED)
        } catch (ex: JwtException) {
            throw FailToParseJwtException.fromCodeWithMsg(
                ApiResultCode.AUTH_FAIL_TO_PARSE_JWT,
                "Fail to parse JWT token for token regenerate"
            )
        }

        val claims=unpacked.payload

        return TokenInfo.fromRefreshTokenClaims(claims)
    }

    fun isTokenStored(tokenInfo: TokenInfo): Boolean {
        return userRepository.findById(tokenInfo.userId).isPresent
    }
}

package com.fesi.flowit.common.auth

import com.fesi.flowit.common.auth.dto.TokenInfo
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.FailToParseJwtException
import com.fesi.flowit.common.response.exceptions.InvalidUserException
import com.fesi.flowit.common.response.exceptions.TokenExpiredException
import com.fesi.flowit.common.response.exceptions.UserNotExistsException
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

    /**
     * 토큰 재발급에 필요한 정보를 가져온다
     * 클라이언트가 보낼 수 있는 토큰의 유효 기간이 만료된 상태일 수 있기 때문에 예외처리하지 않는다
     */
    fun verifyForRegenerate(token: String): String {
        val claims = try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            e.claims
        } catch (ex: JwtException) {
            throw FailToParseJwtException.fromCodeWithMsg(
                ApiResultCode.AUTH_FAIL_TO_PARSE_JWT,
                "Fail to parse JWT token for token regenerate"
            )
        }

        val tokenInfo = TokenInfo.fromClaims(claims)

        if (!isTokenStored(tokenInfo)) {
            throw UserNotExistsException.fromCodeWithMsg(
                ApiResultCode.AUTH_USER_NOT_EXISTS,
                "Cannot find user from given token"
            )
        }

        return tokenInfo.email
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
        return false
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

    fun unpackExpired(token: String): TokenInfo {
        val unpacked = try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            e.claims
        } catch (ex: JwtException) {
            throw FailToParseJwtException.fromCodeWithMsg(
                ApiResultCode.AUTH_FAIL_TO_PARSE_JWT,
                "Fail to parse JWT token for token regenerate"
            )
        }

        return TokenInfo(
            email = unpacked.subject,
            userId = (unpacked["userId"] as String).toLong(), // String으로 저장된 userId를 Long으로 복원
            issuedAt = unpacked.issuedAt,
            expiration = unpacked.expiration
        )
    }

    fun isTokenStored(tokenInfo: TokenInfo): Boolean {
        return userRepository.findById(tokenInfo.userId).isPresent
    }
}

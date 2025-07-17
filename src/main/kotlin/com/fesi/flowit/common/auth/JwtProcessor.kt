package com.fesi.flowit.common.auth

import com.fesi.flowit.common.auth.dto.TokenInfo
import com.fesi.flowit.common.auth.exception.FailToParseJwtException
import com.fesi.flowit.common.auth.exception.InvalidUserException
import com.fesi.flowit.common.auth.exception.TokenExpiredException
import com.fesi.flowit.user.repository.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtProcessor(
    @Value("\${auth.secret-key}")
    private val secretKey: String,
    private val userRepository: UserRepository
) {
    val key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

    /**
     * 인증이 필요한 API 호출 시 클라이언트로부터 받은 토큰을 처리한다
     * unpack 메서드로 문자열 형태의 토큰을 파싱해 TokenInfo 타입 객체로 만들고,
     * 이 객체를 받아서 토큰이 유효한지 검증한다
     * 토큰의 유효 기간이 지났거나, 등록되지 않은 정보로 만들어진 토큰일 경우 예외 처리한다
     */
    fun handle(token: String): TokenInfo {
        val tokenInfo = unpack(token)

        if (isTokenExpired(tokenInfo)) {
            throw TokenExpiredException()
        }
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

            val claims = unpacked.payload

            return TokenInfo(
                email = claims.subject,
                userId = (claims["userId"] as String).toLong(),
                issuedAt = claims.issuedAt,
                expiration = claims.expiration
            )
        } catch (ex: JwtException) {
            throw FailToParseJwtException()
        }
    }

    fun isTokenExpired(tokenInfo: TokenInfo): Boolean {
        return tokenInfo.expiration.before(Date())
    }

    fun isTokenStored(tokenInfo: TokenInfo): Boolean {
        return userRepository.findById(tokenInfo.userId).isPresent
    }
}

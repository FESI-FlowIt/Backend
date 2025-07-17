package com.fesi.flowit.common.auth

import com.fesi.flowit.common.auth.exception.FailToParseJwtException
import com.fesi.flowit.common.auth.exception.InvalidUserException
import com.fesi.flowit.common.auth.exception.TokenExpiredException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter @Autowired constructor(
    private val jwtProcessor: JwtProcessor
) : OncePerRequestFilter() {
    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        // JWT가 없거나 Bearer가 아닌 경우
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.removePrefix("Bearer ").trim()
        try {
            val tokenInfo = jwtProcessor.handle(token) // 유효한 JWT인지 검증한다

            val authentication = UsernamePasswordAuthenticationToken(
                tokenInfo,
                null,
                emptyList()
            )

            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: FailToParseJwtException) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Unauthorized: Failed to parse JWT - ${e.message}")
            return
        } catch (e: TokenExpiredException) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Unauthorized: Token has expired - ${e.message}")
            return
        } catch (e: InvalidUserException) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Unauthorized: User not found or invalid - ${e.message}")
            return
        }

        filterChain.doFilter(request, response)
    }
}

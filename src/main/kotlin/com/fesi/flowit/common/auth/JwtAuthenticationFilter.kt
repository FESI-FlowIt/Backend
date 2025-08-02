package com.fesi.flowit.common.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.common.response.exceptions.BaseException
import com.fesi.flowit.common.response.exceptions.FailToParseJwtException
import com.fesi.flowit.common.response.exceptions.InvalidUserException
import com.fesi.flowit.common.response.exceptions.TokenExpiredException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtProcessor: JwtProcessor,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {
    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeaderName = "Authorization"
        val authHeader = request.getHeader(authHeaderName)

        // JWT가 없거나 Bearer가 아닌 경우
        val bearerKeyword = "Bearer "
        if (authHeader?.startsWith(bearerKeyword) != true) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.removePrefix(bearerKeyword).trim()
        try {
            val tokenInfo = jwtProcessor.handle(token) // 유효한 JWT인지 검증한다

            val authentication = jwtProcessor.getAuthentication(tokenInfo)

            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: FailToParseJwtException) {
            val apiResult = ApiResult.Exception<FailToParseJwtException>(
                code = e.code.code,
                message = e.message,
                httpStatus = e.httpStatus
            )
            handleJwtException(response, apiResult)
            return
        } catch (e: TokenExpiredException) {
            val apiResult = ApiResult.Exception<TokenExpiredException>(
                code = e.code.code,
                message = e.message,
                httpStatus = e.httpStatus
            )
            handleJwtException(response, apiResult)
            return
        } catch (e: InvalidUserException) {
            val apiResult = ApiResult.Exception<InvalidUserException>(
                code = e.code.code,
                message = e.message,
                httpStatus = e.httpStatus
            )
            handleJwtException(response, apiResult)
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun handleJwtException(response: HttpServletResponse, apiResult: ApiResult<BaseException>) {
        response.status = apiResult.httpStatus.value()
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(objectMapper.writeValueAsString(apiResult))
    }
}

package com.fesi.flowit.common.response.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component


@Component
class AuthenticationEntryPointHandlerImpl(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val message = authException.message.toString()
        val e = AuthException.fromCode(ApiResultCode.UNAUTHORIZED)
        val apiResult = ApiResult.Exception<AuthException>(
            code = e.code.code,
            message = message,
            httpStatus = e.httpStatus
        )

        response.status = apiResult.httpStatus.value()
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(objectMapper.writeValueAsString(apiResult))
    }
}
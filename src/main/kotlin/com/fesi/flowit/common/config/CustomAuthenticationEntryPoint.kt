package com.fesi.flowit.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import com.fesi.flowit.common.response.exceptions.BaseException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val e = AuthException.fromCode(ApiResultCode.UNAUTHORIZED)
        val apiResult = ApiResult.Exception<AuthException>(
            code = e.code.code,
            message = e.message,
            httpStatus = e.httpStatus
        )
        handleException(response, apiResult)
    }

    private fun handleException(response: HttpServletResponse, apiResult: ApiResult<BaseException>) {
        response.status = apiResult.httpStatus.value()
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(objectMapper.writeValueAsString(apiResult))
    }

}
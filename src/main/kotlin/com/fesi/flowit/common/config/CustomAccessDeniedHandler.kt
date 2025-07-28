package com.fesi.flowit.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.AuthException
import com.fesi.flowit.common.response.exceptions.BaseException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        val e = AuthException(code = ApiResultCode.FORBIDDEN, httpStatus = HttpStatus.FORBIDDEN)
        val apiResult = ApiResult.Exception<AuthException>(
            code = e.code.code,
            message = e.message,
            httpStatus = e.httpStatus
        )
        handleException(response, apiResult)
    }

    private fun handleException(
        response: HttpServletResponse,
        apiResult: ApiResult<BaseException>
    ) {
        response.status = apiResult.httpStatus.value()
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(objectMapper.writeValueAsString(apiResult))
    }
}

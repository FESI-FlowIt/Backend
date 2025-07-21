package com.fesi.flowit.common.response.exceptions

import com.fesi.flowit.common.response.ApiResultCode
import org.springframework.http.HttpStatus

class FailToParseJwtException(
    override val code: ApiResultCode = ApiResultCode.UNAUTHORIZED,
    override val httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED,
    override val message: String = "Failed to parse JWT"
) : BaseException(code, httpStatus)

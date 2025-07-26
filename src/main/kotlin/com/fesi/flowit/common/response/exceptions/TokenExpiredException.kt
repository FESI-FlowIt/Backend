package com.fesi.flowit.common.response.exceptions

import com.fesi.flowit.common.response.ApiResultCode
import org.springframework.http.HttpStatus

class TokenExpiredException(
    override val code: ApiResultCode = ApiResultCode.UNAUTHORIZED,
    override val httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED,
    override val message: String = "Token has expired"
) : BaseException(code, httpStatus)

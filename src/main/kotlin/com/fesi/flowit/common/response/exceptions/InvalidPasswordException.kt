package com.fesi.flowit.common.response.exceptions

import com.fesi.flowit.common.response.ApiResultCode
import org.springframework.http.HttpStatus

class InvalidPasswordException(
    override val code: ApiResultCode = ApiResultCode.UNAUTHORIZED,
    override val httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED
) : BaseException(code, httpStatus)

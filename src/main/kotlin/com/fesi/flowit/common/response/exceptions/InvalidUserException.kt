package com.fesi.flowit.common.response.exceptions

import com.fesi.flowit.common.response.ApiResultCode
import org.springframework.http.HttpStatus

class InvalidUserException(
    override val code: ApiResultCode = ApiResultCode.UNAUTHORIZED,
    override val httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED,
    override val message: String = "Invalid user"
) : BaseException(code, httpStatus)
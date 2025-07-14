package com.fesi.flowit.common.response.exceptions

import com.fesi.flowit.common.response.ApiResultCode
import org.springframework.http.HttpStatus

class ValidationException(
    override val code: ApiResultCode,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
) : BaseException(code, httpStatus)
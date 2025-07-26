package com.fesi.flowit.common.response.exceptions

import com.fesi.flowit.common.response.ApiResultCode
import org.springframework.http.HttpStatus

class UserAlreadySignedUpException(
    override val code: ApiResultCode = ApiResultCode.CONFLICT,
    override val httpStatus: HttpStatus = HttpStatus.CONFLICT
) : BaseException(code, httpStatus)

package com.fesi.flowit.auth.exception

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.BaseException
import org.springframework.http.HttpStatus

class InvalidPasswordException(
    override val code: ApiResultCode = ApiResultCode.UNAUTHORIZED,
    override val httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED
) : BaseException(code, httpStatus)

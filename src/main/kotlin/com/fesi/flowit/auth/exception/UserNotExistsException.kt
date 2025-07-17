package com.fesi.flowit.auth.exception

import com.fesi.flowit.common.response.ApiResultCode
import com.fesi.flowit.common.response.exceptions.BaseException
import org.springframework.http.HttpStatus

class UserNotExistsException(
    override val code: ApiResultCode = ApiResultCode.BAD_REQUEST,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
) : BaseException(code, httpStatus)

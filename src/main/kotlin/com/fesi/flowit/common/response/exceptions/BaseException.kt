package com.fesi.flowit.common.response.exceptions

import com.fesi.flowit.common.response.ApiResult
import com.fesi.flowit.common.response.ApiResultCode
import org.springframework.http.HttpStatus

fun BaseException.toApiResult() = ApiResult.Exception<BaseException>(
    code = code.code,
    message = code.message,
    httpStatus = httpStatus
)

sealed class BaseException(
    open val code: ApiResultCode,
    open val httpStatus: HttpStatus,
    override val message: String = code.message
) : RuntimeException(message)
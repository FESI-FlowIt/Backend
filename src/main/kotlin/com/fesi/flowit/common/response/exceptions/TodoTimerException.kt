package com.fesi.flowit.common.response.exceptions

import com.fesi.flowit.common.response.ApiResultCode
import org.springframework.http.HttpStatus

class TodoTimerException(
    override val code: ApiResultCode,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val message: String
) : BaseException(code, httpStatus) {
    companion object {
        fun fromCode(code: ApiResultCode): TodoTimerException {
            return TodoTimerException(code = code, message = code.message)
        }

        fun fromCodeWithMsg(code: ApiResultCode, msg: String): TodoTimerException {
            return TodoTimerException(code = code, message = msg)
        }
    }
}
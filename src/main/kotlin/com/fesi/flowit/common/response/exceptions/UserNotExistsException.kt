package com.fesi.flowit.common.response.exceptions

import com.fesi.flowit.common.response.ApiResultCode
import org.springframework.http.HttpStatus

class UserNotExistsException(
    override val code: ApiResultCode = ApiResultCode.BAD_REQUEST,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val message: String = code.message
) : BaseException(code, httpStatus) {
    companion object {
        fun fromCode(code: ApiResultCode): UserNotExistsException {
            return UserNotExistsException(code = code, message = code.message)
        }

        fun fromCodeWithMsg(code: ApiResultCode, msg: String): UserNotExistsException {
            return UserNotExistsException(code = code, message = msg)
        }
    }
}

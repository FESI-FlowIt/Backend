package com.fesi.flowit.common.response.exceptions

import com.fesi.flowit.common.response.ApiResultCode
import org.springframework.http.HttpStatus

class AuthException(
    override val code: ApiResultCode,
    override val httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED,
    override val message: String
) : BaseException(code, httpStatus) {
    companion object {
        fun fromCode(code: ApiResultCode): AuthException {
            return AuthException(code = code, message = code.message)
        }

        fun fromCodeWithMsg(code: ApiResultCode, msg: String): AuthException {
            return AuthException(code = code, message = msg)
        }

        fun withCodeAndStatus(code: ApiResultCode, httpStatus: HttpStatus): AuthException {
            return AuthException(code = code, httpStatus = httpStatus, message = code.message)
        }
    }
}
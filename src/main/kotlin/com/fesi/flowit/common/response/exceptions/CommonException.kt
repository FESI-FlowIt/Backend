package com.fesi.flowit.common.response.exceptions

import com.fesi.flowit.common.response.ApiResultCode
import org.springframework.http.HttpStatus

class CommonException(
    override val code: ApiResultCode = ApiResultCode.BAD_REQUEST,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val message: String = code.message
) : BaseException(code, httpStatus) {
    companion object {
        fun fromCode(code: ApiResultCode): CommonException {
            return CommonException(code = code, message = code.message)
        }

        fun fromCodeWithMsg(code: ApiResultCode, msg: String): CommonException {
            return CommonException(code = code, message = msg)
        }
    }
}
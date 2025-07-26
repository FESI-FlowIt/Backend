package com.fesi.flowit.common.response.exceptions

import com.fesi.flowit.common.response.ApiResultCode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus

class BaseExceptionTest : StringSpec({
    "message가 없는 경우, code.message를 쓴다" {
        val code = ApiResultCode.BAD_REQUEST
        val exception = UserNotExistsException(
            code = code,
            httpStatus = HttpStatus.BAD_REQUEST
        )
        exception.toApiResult().message shouldBe code.message
    }

    "message가 있는 경우, code.message가 아니라 message를 쓴다" {
        val code = ApiResultCode.BAD_REQUEST
        val message = "blahblah"
        val exception = UserNotExistsException(
            code = code,
            httpStatus = HttpStatus.BAD_REQUEST,
            message = message
        )
        exception.toApiResult().message shouldBe message
    }
})

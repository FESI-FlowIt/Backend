package com.fesi.flowit.common.response

import com.fesi.flowit.common.response.exceptions.BaseException
import com.fesi.flowit.common.response.exceptions.ValidationException
import com.fesi.flowit.common.response.exceptions.toApiResult
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.Exception

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleValidation(ex: BaseException): ResponseEntity<ApiResult<Exception>> {
        return when (ex) {
            is ValidationException -> {
                println("Validation is failed")
                ex.toApiResult()
            }
            else -> ex.toApiResult()
        }.toResponseEntity()
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ApiResult<Error>> {
        return ApiResult.Error<Error>(
            code = ApiResultCode.INTERNAL_ERROR.code,
            message = ApiResultCode.INTERNAL_ERROR.message,
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        ).toResponseEntity()
    }
}
package com.fesi.flowit.common.response

import com.fesi.flowit.common.logging.loggerFor
import com.fesi.flowit.common.response.exceptions.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

private val log = loggerFor<GlobalExceptionHandler>()

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleValidation(ex: BaseException): ResponseEntity<ApiResult<Exception>> {
        return when (ex) {
            is CommonException -> {
                /* You can handle exceptions case-by-case */
                ex.toApiResult()
            }
            else -> ex.toApiResult()
        }.toResponseEntity()
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceException(ex: Exception): ResponseEntity<ApiResult<Exception>> {
        return ApiResult.Exception<BaseException>(
            code = ApiResultCode.NOT_FOUND.code,
            message = ex.message ?: ApiResultCode.NOT_FOUND.message,
            httpStatus = HttpStatus.NOT_FOUND
        ).toResponseEntity()
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ApiResult<Error>> {
        log.error(ex.stackTraceToString())

        return ApiResult.Error<Error>(
            code = ApiResultCode.INTERNAL_ERROR.code,
            message = ex.message ?: ApiResultCode.INTERNAL_ERROR.message,
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        ).toResponseEntity()
    }
}
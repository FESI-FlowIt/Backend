package com.fesi.flowit.common.response

import com.fesi.flowit.common.response.exceptions.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleValidation(ex: BaseException): ResponseEntity<ApiResult<Exception>> {
        return when (ex) {
            is ValidationException -> {
                println("Validation is failed")
                ex.toApiResult()
            }
            is InvalidPasswordException -> {
                println("SignIn: Password is invalid")
                ex.toApiResult()
            }
            is UserNotExistsException -> {
                println("SignIn: User not exists")
                ex.toApiResult()
            }
            is UserAlreadySignedUpException -> {
                println("SignUp: User for given email already signed up")
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
        return ApiResult.Error<Error>(
            code = ApiResultCode.INTERNAL_ERROR.code,
            message = ex.message ?: ApiResultCode.INTERNAL_ERROR.message,
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        ).toResponseEntity()
    }
}
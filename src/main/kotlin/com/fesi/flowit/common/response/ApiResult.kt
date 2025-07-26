package com.fesi.flowit.common.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fesi.flowit.common.response.exceptions.BaseException
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * Wraps ApiResult in a ResponseEntity, setting the HTTP status and response body
 */
fun <T> ApiResult<T>.toResponseEntity(): ResponseEntity<ApiResult<T>> {
    return ResponseEntity.status(this.httpStatus).body(this)
}

/**
 * Factory Method for creating ResponseEntity wrapping ApiResult
 */
object ApiResponse {
    fun <T> ok(result: T): ResponseEntity<ApiResult<T>> = ApiResult.Success(result = result).toResponseEntity()
    fun <T> created(result: T): ResponseEntity<ApiResult<T>> = ApiResult.Created(result = result).toResponseEntity()
    fun noContent(): ResponseEntity<ApiResult<Unit>> = ResponseEntity.noContent().build()
}

/**
 * API 응답 모델 정의
 * 성공 예시)
 *  HTTP/1.1 200 OK
 *  {
 *   "code" : "0000",
 *   "message" : "OK",
 *   "data" : { .. }
 *  }
 *
 *  실패 예시)
 *  HTTP/1.1 400 BAD REQUEST
 *  {
 *    "code" : "0400",
 *    "message" : "Not found user",
 *  }
 */
sealed class ApiResult<out T> (
    open val code: String,
    open val message: String,

    @get:JsonIgnore
    open val httpStatus: HttpStatus,
) {
    /**
     * Related to success
     */
    data class Success<T>(
        override val code: String = ApiResultCode.SUCCESS.code,
        override val message: String = ApiResultCode.SUCCESS.message,
        override val httpStatus: HttpStatus = HttpStatus.OK,
        val result: T,
    ) : ApiResult<T>(code, message, httpStatus)

    data class Created<T>(
        override val code: String = ApiResultCode.CREATED.code,
        override val message: String = ApiResultCode.CREATED.message,
        override val httpStatus: HttpStatus = HttpStatus.CREATED,
        val result: T,
    ) : ApiResult<T>(code, message, httpStatus)

    /**
     * Related to failed
     */
    data class Error<T>(
        override val code: String,
        override val message: String,
        override val httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
    ) : ApiResult<T>(code, message, httpStatus)

    data class Exception<T : BaseException>(
        @field:Schema(
            description = "에러 코드",
            example = "0400",
        )
        override val code: String,

        @field:Schema(
            description = "에러 메시지",
            example = "Bad Request",
        )
        override val message: String,
        override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
    ) : ApiResult<T>(code, message, httpStatus)
}
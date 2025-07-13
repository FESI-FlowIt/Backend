package com.fesi.flowit.auth.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthControllerAdvice {
    @ExceptionHandler(UserNotExistsException::class)
    fun handleUserNotExistsException(
        userNotExistsException: UserNotExistsException
    ): ResponseEntity<Void> {
        return ResponseEntity.badRequest().build()
    }

    @ExceptionHandler(InvalidPasswordException::class)
    fun handleInvalidPasswordException(
        invalidPasswordException: InvalidPasswordException
    ): ResponseEntity<Void> {
        return ResponseEntity.badRequest().build()
    }
}
